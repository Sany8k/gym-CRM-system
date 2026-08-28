package com.project.gymcrmsystem.service;

import com.project.gymcrmsystem.dao.TrainingDao;
import com.project.gymcrmsystem.model.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TrainingServiceImpl implements TrainingService {
  private TrainingDao trainingDao;

  @Autowired
  public void setTrainingService(TrainingDao trainingService) {
    this.trainingDao = trainingService;
  }

  @Override
  public Optional<Training> findById(Long id) {
    return trainingDao.findById(id);
  }

  @Override
  public Training save(Training training) {
    return null;
  }
}
