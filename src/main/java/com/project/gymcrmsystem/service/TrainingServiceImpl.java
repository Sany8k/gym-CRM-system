package com.project.gymcrmsystem.service;

import com.project.gymcrmsystem.dao.TraineeDao;
import com.project.gymcrmsystem.dao.TrainerDao;
import com.project.gymcrmsystem.dao.TrainingDao;
import com.project.gymcrmsystem.model.Training;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class TrainingServiceImpl implements TrainingService {
  private TrainingDao trainingDao;
  private TraineeDao traineeDao;
  private TrainerDao trainerDao;

  @Autowired
  public void setTrainingDao(TrainingDao trainingDao) {
    this.trainingDao = trainingDao;
  }

  @Autowired
  public void setTraineeDao(TraineeDao traineeDao) {
    this.traineeDao = traineeDao;
  }

  @Autowired
  public void setTrainerDao(TrainerDao trainerDao) {
    this.trainerDao = trainerDao;
  }

  @Override
  public Optional<Training> findById(Long id) {
    return trainingDao.findById(id);
  }

  @Override
  public Training save(Training training) {
    log.debug("Creating new training for traineeId={} and trainerId={}",
        training.getTraineeId(), training.getTrainerId());
    if (traineeDao.findById(training.getTraineeId()).isEmpty()) {
      log.warn("Trainee with id={} not found", training.getTraineeId());
      throw new IllegalArgumentException("Trainee not found");
    }

    if (trainerDao.findById(training.getTrainerId()).isEmpty()) {
      log.warn("Trainer with id={} not found", training.getTrainerId());
      throw new IllegalArgumentException("Trainer not found");
    }

    log.info("Training created with id={} and traineeId={} and trainerId={}", training.getId(), training.getTraineeId(), training.getTrainerId());
    return trainingDao.save(training);
  }
}
