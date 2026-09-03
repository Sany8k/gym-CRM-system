package com.project.gymcrmsystem.config;

import com.project.gymcrmsystem.model.Trainee;
import com.project.gymcrmsystem.model.Trainer;
import com.project.gymcrmsystem.model.Training;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class StorageConfig {

  @Bean
  public Map<Long, Trainee> traineeStorage() {
    return new HashMap<>();
  }

  @Bean
  public Map<Long, Trainer> trainerStorage() {
    return new HashMap<>();
  }

  @Bean
  public Map<Long, Training> trainingStorage() {
    return new HashMap<>();
  }
}
