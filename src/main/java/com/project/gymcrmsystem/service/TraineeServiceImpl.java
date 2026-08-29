package com.project.gymcrmsystem.service;

import com.project.gymcrmsystem.dao.TraineeDao;
import com.project.gymcrmsystem.dao.TrainerDao;
import com.project.gymcrmsystem.model.Trainee;
import com.project.gymcrmsystem.util.PasswordGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
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
    log.debug("Creating new trainee for {} {}", trainee.getFirstName(), trainee.getLastName());
    String username = trainee.getFirstName() + "." + trainee.getLastName();
    int counter = 1;
    String originalUsername = username;
    while (isUsernameTaken(username)) {
      username = originalUsername + counter;
      counter++;
    }
    trainee.setUsername(username);
    trainee.setPassword(passwordGenerator.generateRandomPassword());
    log.info("Trainee created with id={} and username={}", trainee.getId(), trainee.getUsername());
    return traineeDao.save(trainee);
  }

  @Override
  public Trainee update(Trainee trainee) {
    log.debug("Updating trainee with id={}", trainee.getId());
    Trainee existing = traineeDao.findById(trainee.getId())
        .orElseThrow(() -> {
          log.warn("Trainee with id={} not found", trainee.getId());
          return new IllegalArgumentException("Trainee not found");
        });

    trainee.setUsername(existing.getUsername());
    trainee.setPassword(existing.getPassword());

    log.info("Trainee updated with id={} and username={}", trainee.getId(), trainee.getUsername());
    return traineeDao.update(trainee);
  }

  @Override
  public void deleteById(Long id) {
    log.info("Deleting trainee with id={}", id);
    traineeDao.deleteById(id);
  }

  private boolean isUsernameTaken(String username) {
    return traineeDao.findByUsername(username).isPresent() || trainerDao.findByUsername(username).isPresent();
  }
}
