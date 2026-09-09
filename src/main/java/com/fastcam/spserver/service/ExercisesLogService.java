package com.fastcam.spserver.service;

import com.fastcam.spserver.dto.ExerciseDto;
import com.fastcam.spserver.entity.Exercise;
import com.fastcam.spserver.entity.ExercisesLog;
import com.fastcam.spserver.entity.Member;
import com.fastcam.spserver.repository.ExerciseRepository;
import com.fastcam.spserver.repository.ExercisesLogRepository;
import com.fastcam.spserver.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ExercisesLogService {
    @Autowired
    ExercisesLogRepository elr;

    @Autowired
    MemberRepository mr;

    @Autowired
    ExerciseRepository er;

    @Autowired
    AIService as;

    public void addExercisesLog(ExercisesLog exLog, int mnum) {
        Member member = mr.findByNum(mnum);
        exLog.setMember(member);
        Exercise ex = er.findByExName(exLog.getExName());
        if(ex == null){
            ExerciseDto edto = as.findExercise(exLog.getExName());
            ex = new Exercise();
            ex.setExName(edto.getExName());
            ex.setKcal(edto.getKcal());
            er.save(ex);
        }
        exLog.setCalories((int)(ex.getKcal() * (exLog.getExerciseTime() / 60.0)));
        elr.save(exLog);
    }

    public List<ExercisesLog> getExercisesLogs(int mnum) {
        return elr.findByMemberNum(mnum);
    }

    public void deleteExercisesLog(int num) {
        elr.deleteById(num);
    }
}
