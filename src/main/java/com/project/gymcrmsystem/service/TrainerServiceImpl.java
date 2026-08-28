package com.project.gymcrmsystem.service;

import com.project.gymcrmsystem.dao.TraineeDao;
import com.project.gymcrmsystem.dao.TrainerDao;
import com.project.gymcrmsystem.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
public class TrainerServiceImpl implements TrainerService {
  private TrainerDao trainerDao;
  private TraineeDao traineeDao;

  private static final String PASSWORD_SYMBOLS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
      "abcdefghijklmnopqrstuvwxyz" +
      "0123456789!@#$%^&*()_+-=[]{}|;:,.<>?";
  private static final SecureRandom RANDOM = new SecureRandom();

  @Autowired
  public void setTrainerDao(TrainerDao trainerDao, TraineeDao traineeDao) {
    this.trainerDao = trainerDao;
    this.traineeDao = traineeDao;
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
    trainer.setPassword(generateRandomPassword());
    return trainerDao.save(trainer);
  }

  @Override
  public Trainer update(Trainer trainer) {
    return null;
  }

  private boolean isUsernameTaken(String username) {
    return traineeDao.findByUsername(username).isPresent() || trainerDao.findByUsername(username).isPresent();
  }

  private String generateRandomPassword() {
    StringBuilder password = new StringBuilder(10);

    for (int i = 0; i < 10; i++) {
      int index = RANDOM.nextInt(PASSWORD_SYMBOLS.length());
      password.append(PASSWORD_SYMBOLS.charAt(index));
    }

    return password.toString();
  }
}
