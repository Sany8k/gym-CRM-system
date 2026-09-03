package com.project.gymcrmsystem.facade;

import com.project.gymcrmsystem.model.Trainee;
import com.project.gymcrmsystem.model.Trainer;
import com.project.gymcrmsystem.model.Training;
import com.project.gymcrmsystem.service.TraineeService;
import com.project.gymcrmsystem.service.TrainerService;
import com.project.gymcrmsystem.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GymFacade {

  private final TraineeService traineeService;
  private final TrainerService trainerService;
  private final TrainingService trainingService;

  @Autowired
  public GymFacade(
      TraineeService traineeService,
      TrainerService trainerService,
      TrainingService trainingService
  ) {
    this.traineeService = traineeService;
    this.trainerService = trainerService;
    this.trainingService = trainingService;
  }

  public List<Trainee> getAllTrainees() {
    return traineeService.findAll();
  }

  public Optional<Trainee> getTraineeById(Long id) {
    return traineeService.findById(id);
  }

  public Trainee createTrainee(Trainee trainee) {
    return traineeService.save(trainee);
  }

  public Trainee updateTrainee(Trainee trainee) {
    return traineeService.update(trainee);
  }

  public void deleteTrainee(Long id) {
    traineeService.deleteById(id);
  }

  public List<Trainer> getAllTrainers() {
    return trainerService.findAll();
  }

  public Optional<Trainer> getTrainerById(Long id) {
    return trainerService.findById(id);
  }

  public Trainer createTrainer(Trainer trainer) {
    return trainerService.save(trainer);
  }

  public Trainer updateTrainer(Trainer trainer) {
    return trainerService.update(trainer);
  }

  public Optional<Training> getTrainingById(Long id) {
    return trainingService.findById(id);
  }

  public Training createTraining(Training training) {
    return trainingService.save(training);
  }
}
