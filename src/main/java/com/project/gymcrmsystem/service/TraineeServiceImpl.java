package com.project.gymcrmsystem.service;

import com.project.gymcrmsystem.dao.TraineeDao;
import com.project.gymcrmsystem.dao.TrainerDao;
import com.project.gymcrmsystem.model.Trainee;
import com.project.gymcrmsystem.util.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TraineeServiceImpl implements TraineeService {
  private TraineeDao traineeDao;
  private TrainerDao trainerDao;
  private PasswordGenerator passwordGenerator;

  @Autowired
  public void setTraineeDao(TraineeDao traineeDao) {
    this.traineeDao = traineeDao;
  }

  @Autowired
  public void setTrainerDao(TrainerDao trainerDao) {
    this.trainerDao = trainerDao;
  }

  @Autowired
  public void setPasswordGenerator(PasswordGenerator passwordGenerator) {
    this.passwordGenerator = passwordGenerator;
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
    String username = trainee.getFirstName() + "." + trainee.getLastName();
    int counter = 1;
    String originalUsername = username;
    while (isUsernameTaken(username)) {
      username = originalUsername + counter;
      counter++;
    }
    trainee.setUsername(username);
    trainee.setPassword(passwordGenerator.generateRandomPassword());
    return traineeDao.save(trainee);
  }

  @Override
  public Trainee update(Trainee trainee) {
    Trainee existing = traineeDao.findById(trainee.getId())
        .orElseThrow(() -> new IllegalArgumentException("Trainee not found"));

    trainee.setUsername(existing.getUsername());
    trainee.setPassword(existing.getPassword());

    return traineeDao.update(trainee);
  }

  @Override
  public void deleteById(Long id) {
    traineeDao.deleteById(id);
  }

  private boolean isUsernameTaken(String username) {
    return traineeDao.findByUsername(username).isPresent() || trainerDao.findByUsername(username).isPresent();
  }
}
