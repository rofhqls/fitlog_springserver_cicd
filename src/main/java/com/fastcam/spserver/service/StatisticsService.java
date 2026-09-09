package com.fastcam.spserver.service;

import com.fastcam.spserver.entity.ExercisesLog;
import com.fastcam.spserver.entity.FoodLog;
import com.fastcam.spserver.entity.WeightLog;
import com.fastcam.spserver.repository.ExercisesLogRepository;
import com.fastcam.spserver.repository.FoodLogRepository;
import com.fastcam.spserver.repository.WeightLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class StatisticsService {
    @Autowired
    WeightLogRepository wlr;

    @Autowired
    ExercisesLogRepository elr;

    @Autowired
    FoodLogRepository flr;


    public HashMap<String, Object> getSummary(int id, String period, String date) {
        HashMap<String, Object> result = new HashMap<>();

        //건강 통계 요약 - 현재 체중
        WeightLog weightLog = wlr.findTopByMember_NumOrderByIndateDesc(id);
        if(weightLog != null) {
            result.put("currentWeight", weightLog.getWeight());
        } else {
            result.put("currentWeight", null);
        }

        //통계 조회 기간(주간/월간)
        LocalDate selectedDate = LocalDate.parse(date);
        LocalDate startDate;
        LocalDate endDate;
        if("month".equals(period)) {
            YearMonth yearMonth = YearMonth.from(selectedDate);
            startDate = yearMonth.atDay(1);
            endDate = yearMonth.atEndOfMonth();

        } else {
            startDate = selectedDate.minusDays(
                    selectedDate.getDayOfWeek().getValue() - 1);

            endDate = startDate.plusDays(6);
        }
        Timestamp start =
                Timestamp.valueOf(startDate.atStartOfDay());

        Timestamp end =
                Timestamp.valueOf(endDate.plusDays(1).atStartOfDay().minusNanos(1));

        //현재 체중 기록
        List<WeightLog> weightList =
                wlr.findByMemberNumAndIndateBetweenOrderByIndateDesc(
                        id,
                        start,
                        end
                );

        result.put("weightList", weightList);

        //최근 운동 기록
        List<ExercisesLog> exerciseList =
                elr.findByMemberNumAndIndateBetweenOrderByIndateDesc(
                        id,
                        start,
                        end
                );

        //총 운동 소비 칼로리
        int totalExerciseCalories = exerciseList.stream()
                .mapToInt(ExercisesLog::getCalories)
                .sum();

        result.put(
                "totalExerciseCalories",
                totalExerciseCalories
        );

        result.put("exerciseList", exerciseList);

        //최근 식사 기록
        List<FoodLog> foodList =
                flr.findByMemberNumAndIndateBetweenOrderByIndateDesc(
                        id,
                        start,
                        end
                );

        result.put("foodList", foodList);

        //총 식사 섭취 칼로리
        int totalFoodCalories = foodList.stream()
                .mapToInt(FoodLog::getCalories)
                .sum();

        result.put(
                "totalFoodCalories",
                totalFoodCalories
        );

        //일평균 식사 섭취
        int days =
                (int) (endDate.toEpochDay()
                        - startDate.toEpochDay()) + 1;

        double averageFoodCalories =
                days > 0
                        ? (double) totalFoodCalories / days
                        : 0;

        result.put(
                "averageFoodCalories",
                averageFoodCalories
        );



        return result;
    }
}
