package com.project.gymcrmsystem.util;

import com.project.gymcrmsystem.model.Trainee;
import com.project.gymcrmsystem.model.Trainer;
import com.project.gymcrmsystem.model.Training;
import com.project.gymcrmsystem.model.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Map;

@Component
public class StorageInitializationPostProcessor implements BeanPostProcessor {

  @Value("${storage.data.file}")
  private Resource dataFile;

  private Map<Long, Trainee> traineeMap;
  private Map<Long, Trainer> trainerMap;
  private Map<Long, Training> trainingMap;

  @Autowired
  public void setTraineeMap(Map<Long, Trainee> traineeMap) {
    this.traineeMap = traineeMap;
  }

  @Autowired
  public void setTrainerMap(Map<Long, Trainer> trainerMap) {
    this.trainerMap = trainerMap;
  }

  @Autowired
  public void setTrainingMap(Map<Long, Training> trainingMap) {
    this.trainingMap = trainingMap;
  }

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) {
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(dataFile.getInputStream(), StandardCharsets.UTF_8))) {
      String line;

      while ((line = reader.readLine()) != null) {
        String[] parts = line.split("\\|");
        switch (beanName) {
          case "traineeStorage": {
            if (!"TRAINEE".equals(parts[0])) {
              break;
            }

            String id = parts[1];
            String name = parts[2];
            String surname = parts[3];
            String username = parts[4];
            String password = parts[5];
            String active = parts[6];
            String date = parts[7];
            String address = parts[8];

            Trainee trainee = new Trainee();
            trainee.assignId(Long.parseLong(id));
            trainee.setFirstName(name);
            trainee.setLastName(surname);
            trainee.setUsername(username);
            trainee.setPassword(password);
            trainee.setActive(Boolean.parseBoolean(active));
            trainee.setDateOfBirth(LocalDate.parse(date));
            trainee.setAddress(address);

            traineeMap.put(trainee.getId(), trainee);
            break;
          }

          case "trainerStorage": {
            if (!"TRAINER".equals(parts[0])) {
              break;
            }

            String id = parts[1];
            String name = parts[2];
            String surname = parts[3];
            String username = parts[4];
            String password = parts[5];
            String active = parts[6];
            String specialization = parts[7];

            Trainer trainer = new Trainer();
            TrainingType trainingType = new TrainingType();
            trainingType.setName(specialization);
            trainer.setSpecialization(trainingType);
            trainer.assignId(Long.parseLong(id));
            trainer.setFirstName(name);
            trainer.setLastName(surname);
            trainer.setUsername(username);
            trainer.setPassword(password);
            trainer.setActive(Boolean.parseBoolean(active));

            trainerMap.put(trainer.getId(), trainer);
            break;
          }
          case "trainingStorage": {
            if (!"TRAINING".equals(parts[0])) {
              break;
            }

            String id = parts[1];
            String traineeId = parts[2];
            String trainerId = parts[3];
            String name = parts[4];
            String trainingTypeName = parts[5];
            String date = parts[6];
            String duration = parts[7];

            Training training = new Training();
            TrainingType trainingType = new TrainingType();
            trainingType.setName(trainingTypeName);
            training.setTrainingType(trainingType);
            training.assignId(Long.parseLong(id));
            training.setTraineeId(Long.parseLong(traineeId));
            training.setTrainerId(Long.parseLong(trainerId));
            training.setName(name);
            training.setDate(LocalDate.parse(date));
            training.setDuration(Integer.parseInt(duration));

            trainingMap.put(training.getId(), training);
            break;
          }
          }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return bean;
  }
}
