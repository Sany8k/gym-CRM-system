package com.project.gymcrmsystem.service;

import com.project.gymcrmsystem.dao.TraineeDao;
import com.project.gymcrmsystem.dao.TrainerDao;
import com.project.gymcrmsystem.dao.TrainingDao;
import com.project.gymcrmsystem.model.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
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
    if (traineeDao.findById(training.getTraineeId()).isEmpty()) {
      throw new IllegalArgumentException("Trainee not found");
    }

    if (trainerDao.findById(training.getTrainerId()).isEmpty()) {
      throw new IllegalArgumentException("Trainer not found");
    }

    return trainingDao.save(training);
  }
}
