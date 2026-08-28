package com.project.gymcrmsystem.service;

import com.project.gymcrmsystem.dao.TraineeDao;
import com.project.gymcrmsystem.dao.TrainerDao;
import com.project.gymcrmsystem.model.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TraineeServiceImpl implements TraineeService {
  private TraineeDao traineeDao;
  private TrainerDao trainerDao;

  @Autowired
  public void setTraineeDao(TraineeDao traineeDao, TrainerDao trainerDao) {
    this.traineeDao = traineeDao;
    this.trainerDao = trainerDao;
  }

  @Override
  public List<Trainee> findAll() {
    return traineeDao.findAll();
  }

  @Override
  public Optional<Trainee> findById(Long id) {
    return traineeDao.findById(id);
  }

  @Override
  public Trainee save(Trainee trainee) {
    String username = trainee.getUsername() + "." + trainee.getLastName();
    if (traineeDao.findByUsername(username).isPresent()) {
      throw new IllegalArgumentException("Trainee with this username already exists.");
    }
    if (trainerDao.findByUsername(username).isPresent()) {
      throw new IllegalArgumentException("Trainee with this username already exists.");
    }
    trainee.setUsername(username);
    return traineeDao.save(trainee);
  }

  @Override
  public Trainee update(Trainee trainee) {
    return null;
  }

  @Override
  public void deleteById(Long id) {
    traineeDao.deleteById(id);
  }
}
