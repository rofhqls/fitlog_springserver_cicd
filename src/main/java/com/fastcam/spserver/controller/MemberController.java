package com.fastcam.spserver.controller;

import com.fastcam.spserver.dto.KakaoProfile;
import com.fastcam.spserver.dto.MemberDto;
import com.fastcam.spserver.dto.OAuthToken;
import com.fastcam.spserver.entity.Follow;
import com.fastcam.spserver.entity.Member;
import com.fastcam.spserver.entity.MemberRole;
import com.fastcam.spserver.security.util.CustomJWTException;
import com.fastcam.spserver.security.util.JWTUtil;
import com.fastcam.spserver.service.EmailService;
import com.fastcam.spserver.service.MemberService;
import com.fastcam.spserver.service.SMSService;
import com.google.gson.Gson;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@RestController
@RequestMapping("/member")
@CrossOrigin({ "http://localhost:8000", "http://localhost:3000" })
public class MemberController {

    @Autowired
    MemberService ms;

    @PostMapping("/join")
    public HashMap<String, Object> join(@RequestBody Member member) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        ms.insertMember(member);
        ms.insertMemberRole(member);
        map.put("msg", "OK");
        return map;
    }

    // @PostMapping("/loginLocal")
    // public HashMap<String, Object> loginLocal(@RequestBody Member member){
    // HashMap<String, Object> map = new HashMap<String, Object>();
    // Member mdto = ms.getMemberById( member.getId() );
    // if( mdto == null)
    // map.put("msg", "아이디 패스워드를 확인하세요");
    // else if( !mdto.getPass().equals( member.getPass() ) )
    // map.put("msg", "아이디 패스워드를 확인하세요");
    // else{
    // List<MemberRole> memberRole = ms.getMemberRole(mdto.getNum());
    // MemberDto res = new MemberDto();
    // res.setNum(mdto.getNum());
    // res.setId(mdto.getId());
    // res.setPass(mdto.getPass());
    // res.setName(mdto.getName());
    // res.setPhone(mdto.getPhone());
    // res.setProfileImg(mdto.getProfileImg());
    // res.setProvider(mdto.getProvider());
    // List<String> roles = new ArrayList<>();
    // for(MemberRole mr : memberRole){
    // roles.add(mr.getRoleName());
    // }
    // res.setRole_names(roles);
    // map.put("msg", "OK");
    // map.put("loginUser", res);
    // }
    // return map;
    // }

    @Autowired
    ServletContext sc;

    @Value("${file.upload.path}")
    private String uploadPath;

    @PostMapping("/fileupload")
    public HashMap<String, Object> fileupload(@RequestParam("image") MultipartFile file) {
        HashMap<String, Object> map = new HashMap<String, Object>();

        String path = uploadPath + "/member";

        Calendar today = Calendar.getInstance();
        long dt = today.getTimeInMillis();

        String filename = file.getOriginalFilename();
        String f1 = filename.substring(0, filename.lastIndexOf("."));
        String f2 = filename.substring(filename.lastIndexOf("."));

        String newFilename = f1 + dt + f2;
        String filePath = path + "/" + newFilename;

        try {
            File dir = new File(path);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            file.transferTo(new File(filePath));

            map.put("filename", newFilename);

        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    @PostMapping("/idcheck")
    public HashMap<String, Object> idcheck(@RequestParam("id") String id) {
        HashMap<String, Object> map = new HashMap<>();
        Member mdto = ms.getMemberById(id);
        if (mdto == null)
            map.put("msg", "OK");
        else
            map.put("msg", "notOK");
        return map;
    }

    @Value("${kakao.client_id}")
    private String client_id;

    @Value("${kakao.redirect_uri}")
    private String redirect_uri;

    @GetMapping("/kakaostart")
    public @ResponseBody String kakaostart() {
        String a = "<script type='text/javascript'>"
                + "location.href='https://kauth.kakao.com/oauth/authorize?"
                + "client_id=" + client_id
                + "&redirect_uri=" + redirect_uri
                + "&response_type=code'" + "</script>";
        return a;
    }

    @GetMapping("/kakaoLogin")
    public void kakaoLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        String endpoint = "https://kauth.kakao.com/oauth/token";
        URL url = new URL(endpoint);
        String bodyData = "grant_type=authorization_code&";
        bodyData += "client_id=" + client_id + "&";
        bodyData += "redirect_uri=" + redirect_uri + "&";
        bodyData += "code=" + code;

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        conn.setDoOutput(true);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
        bw.write(bodyData);
        bw.flush();
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String input = "";
        StringBuilder sb = new StringBuilder();
        while ((input = br.readLine()) != null) {
            sb.append(input);
        }
        Gson gson = new Gson();
        OAuthToken oAuthToken = gson.fromJson(sb.toString(), OAuthToken.class);
        String endpoint2 = "https://kapi.kakao.com/v2/user/me";
        URL url2 = new URL(endpoint2);

        HttpsURLConnection conn2 = (HttpsURLConnection) url2.openConnection();
        conn2.setRequestProperty("Authorization", "Bearer " + oAuthToken.getAccess_token());
        conn2.setDoOutput(true);
        BufferedReader br2 = new BufferedReader(new InputStreamReader(conn2.getInputStream(), "UTF-8"));
        String input2 = "";
        StringBuilder sb2 = new StringBuilder();
        while ((input2 = br2.readLine()) != null) {
            sb2.append(input2);
            // System.out.println(input2);
        }

        Gson gson2 = new Gson();
        KakaoProfile kakaoProfile = gson2.fromJson(sb2.toString(), KakaoProfile.class);
        KakaoProfile.KakaoAccount ac = kakaoProfile.getAccount();
        KakaoProfile.KakaoAccount.Profile pf = ac.getProfile();

        System.out.println("id : " + kakaoProfile.getId());
        System.out.println("Profile-Nickname : " + ac.getProfile().getNickname());
        System.out.println("Profile-pfimg : " + pf.getProfile_image_url());

        Member mdto = ms.getMemberBySnsid(kakaoProfile.getId());

        if (mdto == null) {
            mdto = new Member();
            mdto.setId(kakaoProfile.getId());
            mdto.setPass("KAKAO");
            mdto.setSnsid(kakaoProfile.getId());
            mdto.setName(ac.getProfile().getNickname());
            mdto.setProfileImg(pf.getProfile_image_url());
            mdto.setProvider("KAKAO");
            ms.insertMember(mdto);
            mdto = ms.getMemberById(kakaoProfile.getId());
            ms.insertMemberRole(mdto);
            response.sendRedirect("http://13.125.244.199/savekakaoinfo/" + mdto.getNum());

        } else {
            // 기존 회원 → 바로 로그인 처리 페이지
            response.sendRedirect(
                    "http://13.125.244.199/kakaologin/" + mdto.getNum());
        }
    }

    @GetMapping("/getMemberByNum")
    public HashMap<String, Object> getMemberByNum(@RequestParam("num") int num) {
        HashMap<String, Object> map = new HashMap<>();
        Member member = ms.getMemberByNum(num);
        List<MemberRole> mrList = ms.getMemberRole(num);
        List<String> mrsList = new ArrayList<>();
        for (MemberRole mr : mrList) {
            mrsList.add(mr.getRoleName());
        }
        MemberDto mdto = new MemberDto(member, mrsList);
        String accessToken = JWTUtil.generateToken(mdto.getClaims(), 1);
        String refreshToken = JWTUtil.generateToken(mdto.getClaims(), 60 * 24);

        map.put("loginUser", mdto.getClaims());
        map.put("accessToken", accessToken);
        map.put("refreshToken", refreshToken);
        return map;
    }

    @PostMapping("/kakaoinfoUpdate")
    public HashMap<String, Object> kakaoinfoUpdate(@RequestBody Member member) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        Member loginUser = ms.updateKakaoInfo(member);

        if (loginUser == null) {
            map.put("msg", "회원 정보를 찾을 수 없습니다.");
        } else {

            map.put("msg", "OK");

            List<MemberRole> mrList = ms.getMemberRole(loginUser.getNum());
            List<String> mrsList = new ArrayList<>();
            for (MemberRole mr : mrList) {
                mrsList.add(mr.getRoleName());
            }
            MemberDto mdto = new MemberDto(loginUser, mrsList);
            String accessToken = JWTUtil.generateToken(mdto.getClaims(), 1);
            String refreshToken = JWTUtil.generateToken(mdto.getClaims(), 60 * 24);

            map.put("loginUser", mdto.getClaims());
            map.put("accessToken", accessToken);
            map.put("refreshToken", refreshToken);
            // map.put("loginUser", loginUser);
        }
        return map;
    }

    @PostMapping("/updateMember")
    public HashMap<String, Object> updateMember(@RequestBody Member member) {
        HashMap<String, Object> map = new HashMap<>();
        ms.updateMember(member);
        map.put("msg", "OK");
        return map;
    }

    @DeleteMapping("/deleteMember")
    public HashMap<String, Object> deleteMember(@RequestParam("id") String id) {
        HashMap<String, Object> map = new HashMap<>();
        ms.deleteMember(id);
        map.put("msg", "OK");
        return map;
    }

    @GetMapping("/getFollowings")
    public HashMap<String, Object> getFollowings(@RequestParam("ffrom") int ffrom) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("followings", ms.getFollowings(ffrom));
        return map;
    }

    @GetMapping("/getFollowers")
    public HashMap<String, Object> getFollwers(@RequestParam("fto") int fto) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("followers", ms.getFollowers(fto));
        return map;
    }

    @PostMapping("/follow")
    public HashMap<String, Object> follow(@RequestBody Follow follow) {
        HashMap<String, Object> map = new HashMap<>();
        ms.onFollow(follow);
        return map;
    }

    @GetMapping("/findId")
    public HashMap<String, Object> findId(
            @RequestParam("name") String name,
            @RequestParam("phone") String phone) {
        HashMap<String, Object> map = new HashMap<>();
        String id = ms.findId(name, phone);
        map.put("id", id);
        return map;
    }

    @GetMapping("/findPassCheck")
    public HashMap<String, Object> findPassCheck(
            @RequestParam String id,
            @RequestParam String name,
            @RequestParam String phone) {
        HashMap<String, Object> map = new HashMap<>();
        int result = ms.checkUser(id, name, phone);
        if (result > 0)
            map.put("msg", "OK");
        return map;
    }

    @PostMapping("/resetPass")
    public HashMap<String, Object> resetPass(@RequestBody Member member) {
        HashMap<String, Object> map = new HashMap<>();
        if (member.getId() == null) {
            map.put("success", false);
            map.put("msg", "존재하지 않는 이메일");
            return map;
        }

        int result = ms.resetPass(member);
        map.put("success", result > 0);
        map.put("msg", result > 0 ? "비밀번호 변경 성공" : "회원 정보 없음");

        return map;
    }

    @GetMapping("/refresh/{refreshToken}")
    public HashMap<String, Object> refresh(
            @PathVariable("refreshToken") String refreshToken,
            @RequestHeader("Authorization") String authHeader) throws CustomJWTException {
        HashMap<String, Object> result = new HashMap<>();

        if (refreshToken == null || refreshToken.equals(""))
            throw new CustomJWTException("NULL_REFRESH");
        if (authHeader == null || authHeader.length() < 7)
            throw new CustomJWTException("INVALID_HEADER");

        String accessToken = authHeader.substring(7);

        // 기한 만료 체크
        boolean expiredResult = true;
        try {
            JWTUtil.validateToken(accessToken);
        } catch (CustomJWTException e) {
            if (e.getMessage().equals("Expired"))
                expiredResult = false;
        }

        if (expiredResult) { // 유효기한 만료전
            System.out.println("토큰 유료기간 만료전... 계속 사용");
            result.put("accessToken", accessToken);
            result.put("refreshToken", refreshToken);
        } else { // 유효기한 만료 후
            System.out.println("토큰 유료기간 만료후... 토큰 교체");
            // 리프레시 토큰에서 claims 를 추출
            Map<String, Object> claims = JWTUtil.validateToken(refreshToken);
            // 추출한 claims 로 accessToken 재발급
            String newAccessToken = JWTUtil.generateToken(claims, 1);

            String newRefreshToken = "";
            int exp = (Integer) claims.get("exp");
            java.util.Date expDate = new java.util.Date((long) exp * (1000));// 밀리초로 변환
            long gap = expDate.getTime() - System.currentTimeMillis();// 현재 시간과의 차이 계산
            long leftMin = gap / (1000 * 60); // 분단위 변환
            if (leftMin < 60) // 한시간 미만으로 남았으면 토큰 교체
                newRefreshToken = JWTUtil.generateToken(claims, 60 * 24);
            else
                newRefreshToken = refreshToken;

            result.put("accessToken", newAccessToken);
            result.put("refreshToken", newRefreshToken);
        }
        return result;
    }

    @Autowired
    SMSService ss;

    @PostMapping("/sendSMS")
    public HashMap<String, Object> sendSMS(@RequestParam("phone") String phone){
        HashMap<String, Object> result = new HashMap<>();
        ss.sendSMS(phone);
        result.put("msg","ok");
        return result;
    }

    @PostMapping("/confirmSMSCode")
    public HashMap<String, Object> confirmSMSCode(@RequestParam("userNumber") String userNumber, @RequestParam("phone") String phone){
        HashMap<String, Object> result = new HashMap<>();
        result.put("msg", ss.confirmSMSCode(userNumber, phone));
        return result;
    }

    @Autowired
    EmailService es;

    @PostMapping("/sendEmail")
    public HashMap<String, Object> sendEmail(@RequestParam("email") String email, @RequestParam("id") String id){
        HashMap<String, Object> result = new HashMap<>();
        if(ms.getMemberByIdAndEmail(id, email) == null) {
            result.put("msg", "아이디나 이메일을 확인하세요.");
        } else{
            result.put("msg",es.sendEmail(email));
        }
        return result;
    }

    @PostMapping("/confirmEmailCode")
    public HashMap<String, Object> confirmEmailCode(@RequestParam("userNumber") String userNumber, @RequestParam("email") String email){
        HashMap<String, Object> result = new HashMap<>();
        result.put("msg",es.confirmEmailCode(userNumber, email));
        return result;
    }
}
