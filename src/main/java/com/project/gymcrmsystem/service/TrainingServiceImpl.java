package com.project.gymcrmsystem.service;

import com.project.gymcrmsystem.dao.TraineeDao;
import com.project.gymcrmsystem.dao.TrainerDao;
import com.project.gymcrmsystem.dao.TrainingDao;
import com.project.gymcrmsystem.model.Training;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  @Transactional
  public Training save(Training training) {
    log.debug("Creating new training for traineeId={} and trainerId={}",
        training.getTrainee().getId(), training.getTrainer().getId());
    if (traineeDao.findById(training.getTrainee().getId()).isEmpty()) {
      log.warn("Trainee with id={} not found", training.getTrainee().getId());
      throw new IllegalArgumentException("Trainee not found");
    }

    if (trainerDao.findById(training.getTrainer().getId()).isEmpty()) {
      log.warn("Trainer with id={} not found", training.getTrainer().getId());
      throw new IllegalArgumentException("Trainer not found");
    }

    Training saved = trainingDao.save(training);

    log.info("Training created with id={} and traineeId={} and trainerId={}",
        saved.getId(), saved.getTrainee().getId(), saved.getTrainer().getId());

    return saved;
  }
}
