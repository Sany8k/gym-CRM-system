package com.project.gymcrmsystem.dao;

import com.project.gymcrmsystem.model.Training;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Slf4j
public class TrainingDaoImpl implements TrainingDao {

  private Map<Long, Training> storage;
  private AtomicLong sequence;

  @Autowired
  public void setStorage(@Qualifier("trainingStorage") Map<Long, Training> storage) {
    this.storage = storage;
    long maxId = storage.keySet().stream().mapToLong(Long::longValue).max().orElse(0L);
    this.sequence = new AtomicLong(maxId);
  }

  @Override
  public Optional<Training> findById(Long id) {
    return Optional.ofNullable(storage.get(id));
  }

  @Override
  public Training save(Training training) {
    log.debug("Saving training with traineeId={} and trainerId={}",
        training.getTraineeId(), training.getTrainerId());
    long id = sequence.incrementAndGet();
    training.assignId(id);
    storage.put(id, training);
    log.info("Training saved with id={} and traineeId={} and trainerId={}",
        training.getId(), training.getTraineeId(), training.getTrainerId());
    return training;
  }
}
