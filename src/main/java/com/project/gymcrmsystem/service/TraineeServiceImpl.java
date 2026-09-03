package com.project.gymcrmsystem.service;

import com.project.gymcrmsystem.dao.TraineeDao;
import com.project.gymcrmsystem.dao.TrainerDao;
import com.project.gymcrmsystem.model.Trainee;
import com.project.gymcrmsystem.util.PasswordGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
  @Transactional
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
    Trainee saved = traineeDao.save(trainee);

    log.info("Trainee created with id={} and username={}", saved.getId(), saved.getUsername());

    return saved;
  }

  @Override
  @Transactional
  public Trainee update(Trainee trainee) {
    log.debug("Updating trainee with id={}", trainee.getId());
    Trainee existing = traineeDao.findById(trainee.getId())
        .orElseThrow(() -> {
          log.warn("Trainee with id={} not found", trainee.getId());
          return new IllegalArgumentException("Trainee not found");
        });

    trainee.setUsername(existing.getUsername());
    trainee.setPassword(existing.getPassword());

    Trainee updated = traineeDao.update(trainee);

    log.info("Trainee updated with id={} and username={}", updated.getId(), updated.getUsername());

    return updated;
  }

  @Transactional
  @Override
  public void changePassword(String username, String oldPassword, String newPassword) {
    log.debug("Changing password for trainee with username={}", username);
    Trainee trainee = authenticateOrThrow(username, oldPassword);

    if (!validatePassword(newPassword)) {
      log.warn("Invalid password for trainee with username={}", username);
      throw new IllegalArgumentException("New Password must be at least 10 characters long");
    }

    log.info("Changing password for trainee with username={}", username);
    trainee.setPassword(newPassword);
  }

  @Transactional
  @Override
  public void toggleActive(String username, String password) {
    log.debug("Toggling trainee status for username={}", username);
    Trainee trainee = authenticateOrThrow(username, password);

    trainee.setActive(!trainee.isActive());
    log.info("Trainee with username={} is now {}", username, trainee.isActive());

  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    log.info("Deleting trainee with id={}", id);
    traineeDao.deleteById(id);
  }

  private boolean isUsernameTaken(String username) {
    return traineeDao.findByUsername(username).isPresent() || trainerDao.findByUsername(username).isPresent();
  }

  private Trainee authenticateOrThrow(String username, String password) {
    log.debug("Authenticating trainee with username={}", username);
    if (!authenticate(username, password)) {
      throw new IllegalArgumentException("Invalid username or password");
    }

    log.info("Authenticating trainee with username={}", username);
    return traineeDao.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("Trainee not found"));
  }

  @Override
  public boolean authenticate(String username, String password) {
    Optional<Trainee> trainee = traineeDao.findByUsername(username);
    return trainee.map(value -> value.getPassword().equals(password)).orElse(false);
  }

  @Override
  public Optional<Trainee> findByUsername(String username, String password) {
    Optional<Trainee> trainee = traineeDao.findByUsername(username);

    if (trainee.isEmpty()) {
      throw new IllegalArgumentException("Trainee not found");
    }

    if (!trainee.get().getPassword().equals(password)) {
      throw new IllegalArgumentException("Invalid password");
    }
    return trainee;
  }

  @Override
  @Transactional
  public Trainee updateProfile(String username, String password, Trainee changes) {
    log.debug("Updating trainee with username={}", username);
    Trainee trainee = authenticateOrThrow(username, password);


    if (!StringUtils.hasText(changes.getFirstName())) {
      log.warn("Trainee with username={} is empty", username);
      throw new IllegalArgumentException("Invalid first name: First name cannot be empty");
    }

    if (!StringUtils.hasText(changes.getLastName())) {
      log.warn("Trainee with username={} is empty", username);
      throw new IllegalArgumentException("Invalid last name: Last name cannot be empty");
    }

    trainee.setFirstName(changes.getFirstName());
    trainee.setLastName(changes.getLastName());
    trainee.setDateOfBirth(changes.getDateOfBirth());
    trainee.setAddress(changes.getAddress());

    return trainee;
  }

  @Override
  @Transactional
  public void deleteByUsername(String username, String password) {
    log.info("Deleting trainee with username={}", username);
    Trainee trainee = authenticateOrThrow(username, password);
    traineeDao.deleteById(trainee.getId());
  }

  private boolean validatePassword(String password) {
    return password != null &&  !password.isBlank() && password.length() >= 10;
  }
}
