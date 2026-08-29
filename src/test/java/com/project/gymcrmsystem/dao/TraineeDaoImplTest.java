package com.project.gymcrmsystem.dao;

import com.project.gymcrmsystem.model.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TraineeDaoImplTest {

  private final Map<Long, Trainee> storage = new LinkedHashMap<>();
  private TraineeDaoImpl dao;

  @BeforeEach
  void setUp() {
    storage.clear();
    dao = new TraineeDaoImpl();
    dao.setStorage(storage);
  }

  @Test
  void shouldSaveTraineeAssignIdAndStoreIt() {
    Trainee trainee = trainee("Jane", "Doe", "jane.doe");

    Trainee saved = dao.save(trainee);

    assertSame(trainee, saved);
    assertEquals(1L, trainee.getId());
    assertSame(trainee, storage.get(1L));
  }

  @Test
  void shouldContinueIdSequenceFromExistingStorage() {
    Trainee existing = trainee("Existing", "User", "existing.user");
    existing.assignId(5L);
    storage.put(5L, existing);
    dao.setStorage(storage);
    Trainee newTrainee = trainee("Jane", "Doe", "jane.doe");

    dao.save(newTrainee);

    assertEquals(6L, newTrainee.getId());
    assertSame(newTrainee, storage.get(6L));
  }

  @Test
  void shouldUpdateExistingTrainee() {
    Trainee existing = trainee("Jane", "Doe", "jane.doe");
    existing.assignId(1L);
    storage.put(1L, existing);
    Trainee updated = trainee("Jane", "Smith", "jane.smith");
    updated.assignId(1L);

    Trainee result = dao.update(updated);

    assertSame(updated, result);
    assertSame(updated, storage.get(1L));
  }

  @Test
  void shouldNotUpdateTraineeThatDoesNotExist() {
    Trainee trainee = trainee("Jane", "Doe", "jane.doe");
    trainee.assignId(99L);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> dao.update(trainee));

    assertEquals("Trainee not found.", exception.getMessage());
    assertTrue(storage.isEmpty());
  }

  @Test
  void shouldDeleteExistingTrainee() {
    Trainee trainee = trainee("Jane", "Doe", "jane.doe");
    trainee.assignId(1L);
    storage.put(1L, trainee);

    dao.deleteById(1L);

    assertFalse(storage.containsKey(1L));
  }

  @Test
  void shouldLeaveOtherTraineesWhenDeletingUnknownId() {
    Trainee trainee = trainee("Jane", "Doe", "jane.doe");
    trainee.assignId(1L);
    storage.put(1L, trainee);

    dao.deleteById(99L);

    assertSame(trainee, storage.get(1L));
  }

  private Trainee trainee(String firstName, String lastName, String username) {
    Trainee trainee = new Trainee();
    trainee.setFirstName(firstName);
    trainee.setLastName(lastName);
    trainee.setUsername(username);
    return trainee;
  }
}
