package com.project.gymcrmsystem.dao;

import com.project.gymcrmsystem.model.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TrainingDaoImpl implements TrainingDao {

  private Map<Long, Training> storage;
  private final AtomicLong sequence = new AtomicLong(0);

  @Autowired
  public void setStorage(@Qualifier("trainingStorage") Map<Long, Training> storage) {
    this.storage = storage;
  }

  @Override
  public Optional<Training> findById(Long id) {
    return Optional.ofNullable(storage.get(id));
  }

  @Override
  public Training save(Training training) {
    long id = sequence.incrementAndGet();
    training.assignId(id);
    storage.put(id, training);
    return training;
  }
}
