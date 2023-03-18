package com.example.batch.service;

import com.example.batch.repository.PostRepository;

import lombok.extern.slf4j.Slf4j;


import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class PostScheduler {
//    batch를 호출하기 위한 lancher 클래스
    private final PostRepository postRepository;
    private final JobLauncher jobLauncher;
    private final PostBatch postBatch;

    public PostScheduler(PostRepository postRepository, JobLauncher jobLauncher, PostBatch postBatch){
        this.postRepository=postRepository;
        this.jobLauncher = jobLauncher;
        this.postBatch = postBatch;
    }

    @Scheduled(cron="0 0/1 * * * *") //초 분 시 일 월 요일
    public void postSchedule() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
//      현재시간을 배치로 넘겨, 배치에서 DB update를 하도록 배치를 호출하는 logic
//        LocalDateTime.now() 대신 system.currentTimeMillis()를 사용
        Map<String, JobParameter> mp=new HashMap<>();

        mp.put("test",new JobParameter("test"));
        JobParameters jobParameter =new JobParameters(mp);

//       o jobLancher에 매개변수로 batch프로그램명과 배치에 넘길 파라미터를 주입
        jobLauncher.run(postBatch.excuteJob(),jobParameter);
    }
}
