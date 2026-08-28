package com.project.gymcrmsystem.service;

import com.project.gymcrmsystem.dao.TraineeDao;
import com.project.gymcrmsystem.model.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TraineeServiceImpl implements TraineeService {
  private TraineeDao traineeDao;

  @Autowired
  public void setTraineeService(TraineeDao traineeService) {
    this.traineeDao = traineeService;
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
    return null;
  }

  @Override
  public Trainee update(Trainee trainee) {
    return null;
  }

  @Override
  public void deleteById(Long id) {
    traineeDao.deleteById(id);
  }
}
