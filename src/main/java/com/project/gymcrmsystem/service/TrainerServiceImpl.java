package com.project.gymcrmsystem.service;

import com.project.gymcrmsystem.dao.TraineeDao;
import com.project.gymcrmsystem.dao.TrainerDao;
import com.project.gymcrmsystem.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerServiceImpl implements TrainerService {
  private TrainerDao trainerDao;
  private TraineeDao traineeDao;

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
    String username = trainer.getUsername() + "." + trainer.getLastName();
    if (trainerDao.findByUsername(username).isPresent()) {
      throw new IllegalArgumentException("Trainer with this username already exists.");
    }
    if (traineeDao.findByUsername(username).isPresent()) {
      throw new IllegalArgumentException("Trainer with this username already exists.");
    }
    trainer.setUsername(username);
    return trainerDao.save(trainer);
  }

  @Override
  public Trainer update(Trainer trainer) {
    return null;
  }
}
