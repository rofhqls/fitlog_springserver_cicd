package com.fastcam.spserver.service;

import com.fastcam.spserver.dto.ExerciseDto;
import com.fastcam.spserver.dto.NutritionDto;
import com.fastcam.spserver.dto.RequestDto;
import com.fastcam.spserver.dto.ResponseDto;
import com.fastcam.spserver.entity.*;
import com.fastcam.spserver.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.client.ReactorClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AIService {

    private final WebClient webClient;
    private final RestClient restClient;

    @Autowired
    FoodGoalRepository fgr;

    @Autowired
    FoodLogRepository flr;

    @Autowired
    NutritionRepository nr;

    @Autowired
    ChatRepository cr;

    @Autowired
    MemberRepository mr;

    @Autowired
    SubscriptionRepository sr;

    public AIService( @Value("${fastapi.base-url}") String fastApiBaseUrl) {


        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofMinutes(5));

        ReactorClientHttpRequestFactory factory =
                new ReactorClientHttpRequestFactory(httpClient);

        this.webClient = WebClient.builder()
                .baseUrl(fastApiBaseUrl)
                .build();

        this.restClient = RestClient.builder()
                .baseUrl(fastApiBaseUrl)
                .requestFactory(factory)
                .build();
    }


    // 일반 AI 질문
    public ResponseDto query(RequestDto req) {

        Member member = mr.findByNum(req.getUserId());

        // 사용자 질문 저장
        Chat userChat = new Chat();
        userChat.setMember(member);
        userChat.setContent(req.getUserChat());
        userChat.setSender("user");
        cr.save(userChat);

        // 구독 확인
        Subscription s = sr.findTopByMemberOrderByNumDesc(member);

        if (s == null || s.getSubEnd().isBefore(LocalDate.now())) {

            ResponseDto res = new ResponseDto();
            res.setAnswer("구독이 만료되었습니다. 구독 후 서비스를 이용해주세요.");

            // 만료 안내도 채팅 기록에 저장
            Chat aiChat = new Chat();
            aiChat.setMember(member);
            aiChat.setContent(res.getAnswer());
            aiChat.setSender("ai");
            cr.save(aiChat);

            return res;
        }

        // 구독이 유효한 경우에만 FastAPI 호출
        ResponseDto res = webClient.post()
                .uri("/query")
                .bodyValue(req)
                .retrieve()
                .bodyToMono(ResponseDto.class)
                .block();

        if (res != null) {
            Chat aiChat = new Chat();
            aiChat.setMember(member);
            aiChat.setContent(res.getAnswer());
            aiChat.setSender("ai");
            cr.save(aiChat);
        }

        return res;
    }


    // 음식 사진 분석
    public Map<String, Object> findFood(MultipartFile file, int mnum) {

        Member member = mr.findByNum(mnum);

        Subscription s = sr.findTopByMemberOrderByNumDesc(member);

        // 구독 만료 확인
        if (s == null || s.getSubEnd().isBefore(LocalDate.now())) {
            Map<String, Object> res = new HashMap<>();
            res.put("answer", "구독이 만료되었습니다. 구독 후 서비스를 이용해주세요.");
            return res;
        }

        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        builder.part("file", file.getResource())
                .filename(file.getOriginalFilename())
                .contentType(
                        MediaType.parseMediaType(file.getContentType())
                );

        return restClient.post()
                .uri("/findFood")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(builder.build())
                .retrieve()
                .body(Map.class);
    }


    // 음식 영양정보 추정
    public NutritionDto findNutrition(String foodName) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/findNutrition")
                        .queryParam("foodName", foodName)
                        .build())
                .retrieve()
                .body(NutritionDto.class);
    }

    // 운동 소모칼로리 추정
    public ExerciseDto findExercise(String exName) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/findExercise")
                        .queryParam("exName", exName)
                        .build())
                .retrieve()
                .body(ExerciseDto.class);
    }

    public HashMap<String, Object> getUserGoal(int mnum) {
        HashMap<String, Object> res = new HashMap<>();
        FoodGoal fg = fgr.findByMemberNum(mnum);
        if (fg == null) {
            res.put("targetCalories", null);
            res.put("targetCarbsG", null);
            res.put("targetProteinG", null);
            res.put("targetFatG", null);
            return res;
        }
        res.put("targetCalories",fg.getGoalCalories());
        res.put("targetCarbsG",fg.getGoalCarbs());
        res.put("targetProteinG",fg.getGoalProtein());
        res.put("targetFatG",fg.getGoalFat());
        return res;
    }

    public HashMap<String, Object> getConsumedToday(int mnum) {
        HashMap<String, Object> res = new HashMap<>();
        LocalDate today = LocalDate.now();

        List<FoodLog> flList =
                flr.findByMemberNumAndIndate(mnum, today);

        int cal = 0;
        float carbs = 0.0f;
        float protein = 0.0f;
        float fat = 0.0f;
        for(FoodLog fl : flList){
            cal += fl.getCalories();
            carbs += fl.getCarbs();
            protein += fl.getProtein();
            fat += fl.getFat();
        }

        res.put("calories",cal);
        res.put("carbsG",carbs);
        res.put("proteinG",protein);
        res.put("fatG",fat);

        return res;
    }

    public List<Nutrition> queryFoodCandidates(int mc) {
        HashMap<String, Object> res = new HashMap<>();
        return nr.findByKcalLessThanEqual(mc);
    }
}