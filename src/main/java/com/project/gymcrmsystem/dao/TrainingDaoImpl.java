package com.project.gymcrmsystem.dao;

import com.project.gymcrmsystem.model.Training;

import java.util.Optional;

public class TrainingDaoImpl implements TrainingDao {
  @Override
  public Optional<Training> findById(Long id) {
    return Optional.empty();
  }

  @Override
  public Training save(Training training) {
    return null;
  }
}
