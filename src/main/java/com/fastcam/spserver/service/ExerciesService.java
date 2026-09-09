package com.fastcam.spserver.service;

import com.fastcam.spserver.repository.ExerciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class ExerciesService {

    @Autowired
    ExerciesRepository er;


}
