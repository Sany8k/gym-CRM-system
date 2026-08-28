package com.project.gymcrmsystem.service;

import com.project.gymcrmsystem.dao.TraineeDao;
import com.project.gymcrmsystem.dao.TrainerDao;
import com.project.gymcrmsystem.model.Trainer;
import com.project.gymcrmsystem.util.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerServiceImpl implements TrainerService {
  private TrainerDao trainerDao;
  private TraineeDao traineeDao;
  private PasswordGenerator passwordGenerator;

  @Autowired
  public void setTrainerDao(TrainerDao trainerDao) {
    this.trainerDao = trainerDao;
  }

  @Autowired
  public void setTraineeDao(TraineeDao traineeDao) {
    this.traineeDao = traineeDao;
  }

  @Autowired
  public void setPasswordGenerator(PasswordGenerator passwordGenerator) {
    this.passwordGenerator = passwordGenerator;
  }

  @Override
  public List<Trainer> findAll() {
    return trainerDao.findAll();
  }

  @Override
  public Optional<Trainer> findById(Long id) {
    return trainerDao.findById(id);
  }

  @Override
  public Trainer save(Trainer trainer) {
    String username = trainer.getFirstName() + "." + trainer.getLastName();
    int counter = 1;
    String originalUsername = username;
    while (isUsernameTaken(username)) {
      username = originalUsername + counter;
      counter++;
    }
    trainer.setUsername(username);
    trainer.setPassword(passwordGenerator.generateRandomPassword());
    return trainerDao.save(trainer);
  }

  @Override
  public Trainer update(Trainer trainer) {
    Trainer existing = trainerDao.findById(trainer.getId())
        .orElseThrow(() -> new IllegalArgumentException("Trainer not found"));
    trainer.setUsername(existing.getUsername());
    trainer.setPassword(existing.getPassword());
    return trainerDao.update(trainer);
  }

  private boolean isUsernameTaken(String username) {
    return traineeDao.findByUsername(username).isPresent() || trainerDao.findByUsername(username).isPresent();
  }
}
