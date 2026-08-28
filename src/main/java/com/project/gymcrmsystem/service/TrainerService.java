package com.project.gymcrmsystem.service;

import com.project.gymcrmsystem.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerService {
  List<Trainer> findAll();
  Optional<Trainer> findById(Long id);
  Trainer save(Trainer trainer);
  Trainer update(Trainer trainer);
}
