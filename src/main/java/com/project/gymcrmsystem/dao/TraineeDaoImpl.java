package com.project.gymcrmsystem.dao;

import com.project.gymcrmsystem.model.Trainee;
import com.project.gymcrmsystem.model.Trainer;
import com.project.gymcrmsystem.model.Training;
import com.project.gymcrmsystem.model.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
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
  public List<Training> findByTraineeCriteria(
      String username,
      LocalDate fromDate,
      LocalDate toDate,
      String trainerName,
      String trainingTypeName
  ) {
    CriteriaBuilder cb = em.getCriteriaBuilder();

    CriteriaQuery<Training> query = cb.createQuery(Training.class);
    Root<Training> root = query.from(Training.class);

    List<Predicate> predicates = new ArrayList<>();

    Join<Training, Trainee> joinTrainee = root.join("trainee");

    predicates.add(
        cb.equal(joinTrainee.get("username"), username)
    );

    if (fromDate != null) {
      predicates.add(
          cb.greaterThanOrEqualTo(root.get("date"), fromDate)
      );
    }
    if (toDate != null) {
      predicates.add(
          cb.lessThanOrEqualTo(root.get("date"), toDate)
      );
    }

    if (StringUtils.hasText(trainerName)) {
      Join<Training, Trainer> joinTrainer = root.join("trainer");
      predicates.add(
          cb.equal(joinTrainer.get("firstName"), trainerName)
      );
    }

    if (StringUtils.hasText(trainingTypeName)) {
      Join<Training, TrainingType> joinTrainingType = root.join("trainingType");
      predicates.add(
          cb.equal(joinTrainingType.get("name"), trainingTypeName)
      );
    }

    query.select(root).where(predicates.toArray(new Predicate[0]));
    return em.createQuery(query).getResultList();
  }

  @Override
  public Trainee save(Trainee trainee) {
    log.debug("Saving trainee with firstName={} and lastName={}",
        trainee.getFirstName(), trainee.getLastName());

    em.persist(trainee);

    log.debug("Trainee saved with id={} and username={}",
        trainee.getId(), trainee.getUsername());

    return trainee;
  }

  @Override
  public Trainee update(Trainee trainee) {
    log.debug("Updating trainee with id={} and username={}",
        trainee.getId(), trainee.getUsername());

    if (em.find(Trainee.class, trainee.getId()) == null) {
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

    Trainee trainee = em.find(Trainee.class, id);

    if (trainee == null) {
      log.warn("Trainee with id={} not found", id);
      throw new IllegalArgumentException("Trainee not found.");
    }

    em.remove(trainee);
  }
}
