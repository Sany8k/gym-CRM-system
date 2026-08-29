package com.project.gymcrmsystem.util;

import com.project.gymcrmsystem.model.Trainee;
import com.project.gymcrmsystem.model.Trainer;
import com.project.gymcrmsystem.model.Training;
import com.project.gymcrmsystem.model.TrainingType;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class StorageInitializationPostProcessor implements BeanPostProcessor {

  @Value("${storage.data.file}")
  private Resource dataFile;

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) {
    if (!"traineeStorage".equals(beanName)
        && !"trainerStorage".equals(beanName)
        && !"trainingStorage".equals(beanName)) {
      return bean;
    }

    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(dataFile.getInputStream(), StandardCharsets.UTF_8))) {
      String line;

      while ((line = reader.readLine()) != null) {
        log.debug("Creating initial data for bean {} from line", beanName);
        String[] parts = line.split("\\|", -1);
        switch (beanName) {
          case "traineeStorage": {
            if (!"TRAINEE".equals(parts[0])) {
              break;
            }
            log.debug("Processing trainee data");
            if (parts.length != 9) {
              log.warn("Invalid initial data line for trainee");
              throw new IllegalArgumentException("Invalid initial data line");
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

            @SuppressWarnings("unchecked")
            Map<Long, Trainee> storage = (Map<Long, Trainee>) bean;
            storage.put(trainee.getId(), trainee);
            break;
          }

          case "trainerStorage": {
            if (!"TRAINER".equals(parts[0])) {
              break;
            }
            log.debug("Processing trainer data");
            if (parts.length != 8) {
              log.warn("Invalid initial data line for trainer");
              throw new IllegalArgumentException("Invalid initial data line");
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

            @SuppressWarnings("unchecked")
            Map<Long, Trainer> storage = (Map<Long, Trainer>) bean;
            storage.put(trainer.getId(), trainer);
            log.info("Trainer saved with id={} and username={}", trainer.getId(), trainer.getUsername());
            break;
          }
          case "trainingStorage": {
            if (!"TRAINING".equals(parts[0])) {
              break;
            }
            if (parts.length != 8) {
              log.warn("Invalid initial data line for training");
              throw new IllegalArgumentException("Invalid initial data line");
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

            @SuppressWarnings("unchecked")
            Map<Long, Training> storage = (Map<Long, Training>) bean;
            log.debug("Saving training with id={} and traineeId={} and trainerId={}",
                training.getId(), training.getTraineeId(), training.getTrainerId());
            storage.put(training.getId(), training);
            log.info("Training saved with id={} and traineeId={} and trainerId={}",
                training.getId(), training.getTraineeId(), training.getTrainerId());
            break;
          }
          }
      }
    } catch (IOException e) {
      throw new IllegalStateException("Failed to initialize storage from " + dataFile, e);
    }
    return bean;
  }
}
