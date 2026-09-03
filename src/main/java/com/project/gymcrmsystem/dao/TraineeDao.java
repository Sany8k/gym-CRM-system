package com.project.gymcrmsystem.dao;

import com.project.gymcrmsystem.model.Trainee;

import java.util.List;
import java.util.Optional;

public interface TraineeDao {

  List<Trainee> findAll();

  Optional<Trainee> findById(Long id);

  Optional<Trainee> findByUsername(String username);

  Trainee save(Trainee trainee);

  Trainee update(Trainee trainee);

  void deleteById(Long id);
}
