package com.fastcam.spserver.service;

import com.fastcam.spserver.dto.NutritionDto;
import com.fastcam.spserver.entity.FoodLog;
import com.fastcam.spserver.entity.Member;
import com.fastcam.spserver.entity.Nutrition;
import com.fastcam.spserver.repository.FoodLogRepository;
import com.fastcam.spserver.repository.MemberRepository;
import com.fastcam.spserver.repository.NutritionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FoodLogService {
    @Autowired
    FoodLogRepository flr;

    @Autowired
    MemberRepository mr;

    @Autowired
    NutritionRepository nr;

    @Autowired
    AIService as;


    public void addFoodLog(FoodLog foodLog, int mnum) {
        Member member = mr.findByNum(mnum);
        foodLog.setMember(member);
        Nutrition nt = nr.findByFname(foodLog.getMenu());
        if (nt == null){
            NutritionDto ndto = as.findNutrition(foodLog.getMenu());
            nt = new Nutrition();
            nt.setFname(ndto.getFname());
            nt.setUnit(ndto.getUnit());
            nt.setKcal(ndto.getKcal());
            nt.setCarbs(ndto.getCarbs());
            nt.setProtein(ndto.getProtein());
            nt.setFat(ndto.getFat());
            nr.save(nt);
        }
        float f = foodLog.getAmount() / (float) nt.getUnit();
        foodLog.setCalories((int)(nt.getKcal() * f));
        foodLog.setCarbs(nt.getCarbs() * f);
        foodLog.setProtein(nt.getProtein() * f);
        foodLog.setFat(nt.getFat() * f);
        flr.save(foodLog);
    }

    public List<FoodLog> getFoodLog(int mnum) {
        return flr.findByMemberNum(mnum);
    }

    public void deleteFoodLog(int num) {
        flr.deleteById(num);
    }
}
