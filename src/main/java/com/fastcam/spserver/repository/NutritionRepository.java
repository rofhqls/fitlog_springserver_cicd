package com.fastcam.spserver.repository;

import com.fastcam.spserver.entity.Nutrition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NutritionRepository extends JpaRepository<Nutrition, Integer> {

    Nutrition findByFname(String menu);

    List<Nutrition> findByKcalLessThanEqual(int mc);
}
