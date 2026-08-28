package com.project.gymcrmsystem.dao;

import com.project.gymcrmsystem.model.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TraineeDaoImpl implements TraineeDao {

  private final Map<Long, Trainee> storage;
  private final AtomicLong sequence = new AtomicLong(0);

  @Autowired
  public TraineeDaoImpl (@Qualifier("traineeStorage") Map<Long, Trainee> storage) {
    this.storage = storage;
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
  public Trainee save(Trainee trainee) {
    long id = sequence.incrementAndGet();
    trainee.assignId(id);
    storage.put(id, trainee);
    return trainee;
  }

  @Override
  public Trainee update(Trainee trainee) {
    storage.replace(trainee.getId(), trainee);
    return trainee;
  }

  @Override
  public void deleteById(Long id) {
    storage.remove(id);
  }
}
