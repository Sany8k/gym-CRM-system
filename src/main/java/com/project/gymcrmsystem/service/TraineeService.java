package com.project.gymcrmsystem.service;

import com.project.gymcrmsystem.model.Trainee;

import java.util.List;
import java.util.Optional;

public interface TraineeService {
  List<Trainee> findAll();
  Optional<Trainee> findById(Long id);
  Trainee save(Trainee trainee);
  Trainee update(Trainee trainee);
  void deleteById(Long id);
}
