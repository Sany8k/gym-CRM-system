package com.project.gymcrmsystem.service;

import com.project.gymcrmsystem.model.Training;

import java.util.Optional;

public interface TrainingService {
  Optional<Training> findById(Long id);
  Training save(Training training);
}
