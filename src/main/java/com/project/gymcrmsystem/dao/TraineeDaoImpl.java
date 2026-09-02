package com.project.gymcrmsystem.dao;

import com.project.gymcrmsystem.model.Trainee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class TraineeDaoImpl implements TraineeDao {

  @PersistenceContext
  private EntityManager em;

  @Override
  public List<Trainee> findAll() {
    TypedQuery<Trainee> query = em.createQuery("SELECT t FROM Trainee t", Trainee.class);
    return query.getResultList();
  }

  @Override
  public Optional<Trainee> findById(Long id) {
    return Optional.ofNullable(em.find(Trainee.class, id));
  }

  @Override
  public Optional<Trainee> findByUsername(String username) {
    TypedQuery<Trainee> query = em.createQuery("SELECT t FROM Trainee t WHERE t.username = :username",
            Trainee.class);
    query.setParameter("username", username);
    List<Trainee> results = query.getResultList();
    if (results.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(results.getFirst());
  }

  @Override
  public Trainee save(Trainee trainee) {
    log.debug("Saving trainee with firstName={} and lastName={}",
        trainee.getFirstName(), trainee.getLastName());
    Trainee savedTrainee = em.merge(trainee);
    log.debug("Trainee saved with id={} and username={}",
        savedTrainee.getId(), savedTrainee.getUsername());
    return savedTrainee;
  }

  @Override
  public Trainee update(Trainee trainee) {
    log.debug("Updating trainee with id={} and username={}",
        trainee.getId(), trainee.getUsername());
    if (!em.contains(trainee)) {
      log.warn("Trainee with id={} not found", trainee.getId());
      throw new IllegalArgumentException("Trainee not found.");
    }
     Trainee savedTrainee = em.merge(trainee);
    log.debug("Trainee updated with id={} and username={}",
        savedTrainee.getId(), savedTrainee.getUsername());
    return savedTrainee;
  }

  @Override
  public void deleteById(Long id) {
    log.debug("Deleting trainee with id={}", id);
    em.remove(em.find(Trainee.class, id));
  }
}
