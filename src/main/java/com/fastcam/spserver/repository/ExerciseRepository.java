package com.fastcam.spserver.repository;

import com.fastcam.spserver.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {
    Exercise findByExName(String exName);
}
