package com.example.batch.service;

import com.example.batch.domain.Post;
import com.example.batch.repository.PostRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Configuration //spring bean 등록하기 위한 어노테이션
public class PostBatch {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final PostRepository postRepository;

    public PostBatch(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, PostRepository postRepository) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.postRepository = postRepository;
    }

    public Job excuteJob(){
        return jobBuilderFactory.get("excuteJo").start(firstStep()).build();
    }
    @Bean
    public Step firstStep(){
        return stepBuilderFactory.get("fristStep")
                .tasklet((contribution, chunkContext) -> {
                    List<Post> posts=postRepository.findByScheduled("checked");
                    for(Post a : posts){
                        if(a.getScheduledTime().isBefore(LocalDateTime.now())){
                            a.setScheduled(null);
                            postRepository.save(a);
                        }
                    }
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
