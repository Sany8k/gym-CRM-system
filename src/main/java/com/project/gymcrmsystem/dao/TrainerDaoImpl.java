package com.project.gymcrmsystem.dao;

import com.project.gymcrmsystem.model.Trainer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class TrainerDaoImpl implements TrainerDao {

  @PersistenceContext
  private EntityManager em;

  @Override
  public List<Trainer> findAll() {
    TypedQuery<Trainer> query = em.createQuery("SELECT t FROM Trainer t",
            Trainer.class);
    return query.getResultList();
  }

  @Override
  public Optional<Trainer> findById(Long id) {
    return Optional.ofNullable(em.find(Trainer.class, id));
  }

  @Override
  public Optional<Trainer> findByUsername(String username) {
    TypedQuery<Trainer> query = em.createQuery("SELECT t FROM Trainer t WHERE t.username = :username",
            Trainer.class);
    query.setParameter("username", username);
    List<Trainer> results = query.getResultList();
    if (results.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(results.getFirst());
  }

  @Override
  public Trainer save(Trainer trainer) {
    log.debug("Saving trainer with firstName={} and lastName={}",
        trainer.getFirstName(), trainer.getLastName());
    em.persist(trainer);
    log.debug("Trainer saved with id={} and username={}",
        trainer.getId(), trainer.getUsername());
    return trainer;
  }

  @Override
  public Trainer update(Trainer trainer) {
    log.debug("Updating trainer with id={} and username={}",
        trainer.getId(), trainer.getUsername());
    if (em.find(Trainer.class, trainer.getId()) == null) {
      log.warn("Trainer with id={} not found", trainer.getId());
      throw new IllegalArgumentException("Trainer not found.");
    }
     Trainer updatedTrainer = em.merge(trainer);
    log.debug("Trainer updated with id={} and username={}",
        updatedTrainer.getId(), updatedTrainer.getUsername());
    return updatedTrainer;
  }
}
