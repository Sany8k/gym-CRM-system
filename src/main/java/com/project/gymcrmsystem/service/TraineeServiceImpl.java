package com.project.gymcrmsystem.service;

import com.project.gymcrmsystem.dao.TraineeDao;
import com.project.gymcrmsystem.dao.TrainerDao;
import com.project.gymcrmsystem.model.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
public class TraineeServiceImpl implements TraineeService {
  private TraineeDao traineeDao;
  private TrainerDao trainerDao;

  private static final String PASSWORD_SYMBOLS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
      "abcdefghijklmnopqrstuvwxyz" +
      "0123456789!@#$%^&*()_+-=[]{}|;:,.<>?";
  private static final SecureRandom RANDOM = new SecureRandom();

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
    String username = trainee.getFirstName() + "." + trainee.getLastName();
    int counter = 1;
    String originalUsername = username;
    while (isUsernameTaken(username)) {
      username = originalUsername + counter;
      counter++;
    }
    trainee.setUsername(username);
    trainee.setPassword(generateRandomPassword());
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
