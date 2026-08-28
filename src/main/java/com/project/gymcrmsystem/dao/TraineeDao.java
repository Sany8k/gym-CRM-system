package com.project.gymcrmsystem.dao;

import com.project.gymcrmsystem.model.Trainee;

import java.util.Optional;

public interface TraineeDao {

  Optional<Trainee> findById(Long id);

  Trainee save(Trainee trainee);

  Trainee update(Trainee trainee);

  void delete(Long id);
}
