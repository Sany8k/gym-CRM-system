package com.project.gymcrmsystem.dao;

import com.project.gymcrmsystem.model.Trainee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Slf4j
public class TraineeDaoImpl implements TraineeDao {

  private Map<Long, Trainee> storage;
  private AtomicLong sequence;

  @Autowired
  public void setStorage(@Qualifier("traineeStorage") Map<Long, Trainee> storage) {
    this.storage = storage;
    long maxId = storage.keySet().stream().mapToLong(Long::longValue).max().orElse(0L);
    this.sequence = new AtomicLong(maxId);
  }

  @Override
  public List<Trainee> findAll() {
    return List.copyOf(storage.values());
  }

  @Override
  public Optional<Trainee> findById(Long id) {
    return Optional.ofNullable(storage.get(id));
  }

  @Override
  public Optional<Trainee> findByUsername(String username) {
    for (Trainee trainee : storage.values()) {
      if (trainee.getUsername().equals(username)) {
        return Optional.of(trainee);
      }
    }
    return Optional.empty();
  }

  @Override
  public Trainee save(Trainee trainee) {
    log.debug("Saving trainee with firstName={} and lastName={}",
        trainee.getFirstName(), trainee.getLastName());
    long id = sequence.incrementAndGet();
    trainee.assignId(id);
    storage.put(id, trainee);
    log.debug("Trainee saved with id={} and username={}",
        trainee.getId(), trainee.getUsername());
    return trainee;
  }

  @Override
  public Trainee update(Trainee trainee) {
    log.debug("Updating trainee with id={} and username={}",
        trainee.getId(), trainee.getUsername());
    if (!storage.containsKey(trainee.getId())) {
      log.warn("Trainee with id={} not found", trainee.getId());
      throw new IllegalArgumentException("Trainee not found.");
    }
    storage.replace(trainee.getId(), trainee);
    log.debug("Trainee updated with id={} and username={}",
        trainee.getId(), trainee.getUsername());
    return trainee;
  }

  @Override
  public void deleteById(Long id) {
    log.debug("Deleting trainee with id={}", id);
    storage.remove(id);
  }
}
