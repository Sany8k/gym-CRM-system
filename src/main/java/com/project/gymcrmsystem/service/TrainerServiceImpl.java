package com.project.gymcrmsystem.service;

import com.project.gymcrmsystem.dao.TrainerDao;
import com.project.gymcrmsystem.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerServiceImpl implements TrainerService {
  private TrainerDao trainerDao;

  @Autowired
  public void setTrainerService(TrainerDao trainerService) {
    this.trainerDao = trainerService;
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
    return null;
  }

  @Override
  public Trainer update(Trainer trainer) {
    return null;
  }
}
