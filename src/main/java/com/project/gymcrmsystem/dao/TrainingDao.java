package com.project.gymcrmsystem.dao;

import com.project.gymcrmsystem.model.Training;

import java.util.Optional;

public interface TrainingDao {

  Optional<Training> findById(Long id);

  Training save(Training training);
}
