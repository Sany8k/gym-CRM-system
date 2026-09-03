package com.project.gymcrmsystem.service;

import com.project.gymcrmsystem.dao.TraineeDao;
import com.project.gymcrmsystem.dao.TrainerDao;
import com.project.gymcrmsystem.model.Trainer;
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
  @Transactional
  public Trainer save(Trainer trainer) {
    log.debug("Creating new trainer for {} {}", trainer.getFirstName(), trainer.getLastName());
    String username = trainer.getFirstName() + "." + trainer.getLastName();
    int counter = 1;
    String originalUsername = username;
    while (isUsernameTaken(username)) {
      username = originalUsername + counter;
      counter++;
    }
    trainer.setUsername(username);
    trainer.setPassword(passwordGenerator.generateRandomPassword());

    Trainer saved = trainerDao.save(trainer);

    log.info("Trainer created with id={} and username={}", saved.getId(), saved.getUsername());

    return saved;
  }

  @Override
  @Transactional
  public Trainer update(Trainer trainer) {
    log.debug("Updating trainer with id={}", trainer.getId());
    Trainer existing = trainerDao.findById(trainer.getId())
        .orElseThrow(() -> {
          log.warn("Trainer with id={} not found", trainer.getId());
          return new IllegalArgumentException("Trainer not found");
        });
    trainer.setUsername(existing.getUsername());
    trainer.setPassword(existing.getPassword());
    Trainer updated = trainerDao.update(trainer);

    log.info("Trainer updated with id={} and username={}", updated.getId(), updated.getUsername());

    return updated;
  }

  @Override
  @Transactional
  public void changePassword(String username, String oldPassword, String newPassword) {
    log.debug("Changing password for trainer with username={}", username);
    Trainer trainer = authenticateAndThrow(username, oldPassword);

    if (!validatePassword(newPassword)) {
      log.warn("New password does not meet the requirements for trainer with username={}", username);
      throw new IllegalArgumentException("New password does not meet the requirements");
    }

    trainer.setPassword(newPassword);
    log.info("Password changed for trainer with username={}", username);
  }

  @Override
  @Transactional
  public void toggleActive(String username, String password) {
    log.debug("Toggling active status for trainer with username={}", username);
    Trainer trainer = authenticateAndThrow(username, password);
    trainer.setActive(!trainer.isActive());
    log.info("Active status toggled for trainer with username={} to {}", username, trainer.isActive());
  }

  private boolean isUsernameTaken(String username) {
    return traineeDao.findByUsername(username).isPresent() || trainerDao.findByUsername(username).isPresent();
  }

  @Override
  public boolean authenticate(String username, String password) {
    Optional<Trainer> trainer = trainerDao.findByUsername(username);
      return trainer.map(value -> value.getPassword().equals(password)).orElse(false);
  }

  @Override
  public Optional<Trainer> findByUsername(String username, String password) {
    Optional<Trainer> trainer = trainerDao.findByUsername(username);

    if (trainer.isEmpty()) {
      log.warn("Trainer with username={} not found", username);
      throw new IllegalArgumentException("Trainer with username " + username + " not found");
    }

    if (!trainer.get().getPassword().equals(password)) {
      log.warn("Invalid password for trainer with username={}", username);
      throw new IllegalArgumentException("Invalid password");
    }

    log.info("Trainer found with id={} and username={}", trainer.get().getId(), username);
    return trainer;
  }

  @Override
  @Transactional
  public Trainer updateProfile(String username, String password, Trainer changes) {
    log.debug("Updating profile for trainer with username={}", username);
    Trainer trainer = authenticateAndThrow(username, password);

    if (!StringUtils.hasText(changes.getFirstName())) {
      log.warn("Trainer with username={} is empty", username);
      throw new IllegalArgumentException("Invalid first name: First name cannot be empty");
    }
    if (!StringUtils.hasText(changes.getLastName())) {
      log.warn("Trainer with username={} is empty", username);
      throw new IllegalArgumentException("Invalid last name: Last name cannot be empty");
    }

    if (changes.getSpecialization() == null) {
      log.warn("Trainer with username={} is empty", username);
      throw new IllegalArgumentException("Invalid specialization: Specialization cannot be null");
    }

    trainer.setFirstName(changes.getFirstName());
    trainer.setLastName(changes.getLastName());
    trainer.setSpecialization(changes.getSpecialization());

    log.info("Specialization changed for trainer with username={}", username);
    return trainer;
  }

  private Trainer authenticateAndThrow(String username, String password) {
    if (authenticate(username, password)) {
      return trainerDao.findByUsername(username)
          .orElseThrow(() -> {
            log.warn("Trainer with username={} not found", username);
            return new IllegalArgumentException("Trainer not found");
          });
    } else {
      throw new IllegalArgumentException("Invalid username or password");
    }
  }

  private boolean validatePassword(String password) {
    return password != null && !password.isBlank() && password.length() >= 10;
  }
}
