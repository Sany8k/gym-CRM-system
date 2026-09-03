package com.project.gymcrmsystem.dao;

import com.project.gymcrmsystem.model.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrainerDaoImplTest {

  private final Map<Long, Trainer> storage = new LinkedHashMap<>();
  private TrainerDaoImpl dao;

  @BeforeEach
  void setUp() {
    storage.clear();
    dao = new TrainerDaoImpl();
    dao.setStorage(storage);
  }

  @Test
  void shouldSaveTrainerAssignIdAndStoreIt() {
    Trainer trainer = trainer("John", "Doe", "john.doe");

    Trainer saved = dao.save(trainer);

    assertSame(trainer, saved);
    assertEquals(1L, trainer.getId());
    assertSame(trainer, storage.get(1L));
  }

  @Test
  void shouldContinueIdSequenceFromExistingStorage() {
    Trainer existing = trainer("Existing", "User", "existing.user");
    existing.assignId(5L);
    storage.put(5L, existing);
    dao.setStorage(storage);
    Trainer newTrainer = trainer("John", "Doe", "john.doe");

    dao.save(newTrainer);

    assertEquals(6L, newTrainer.getId());
    assertSame(newTrainer, storage.get(6L));
  }

  @Test
  void shouldUpdateExistingTrainer() {
    Trainer existing = trainer("John", "Doe", "john.doe");
    existing.assignId(1L);
    storage.put(1L, existing);
    Trainer updated = trainer("John", "Smith", "john.smith");
    updated.assignId(1L);

    Trainer result = dao.update(updated);

    assertSame(updated, result);
    assertSame(updated, storage.get(1L));
  }

  @Test
  void shouldNotUpdateTrainerThatDoesNotExist() {
    Trainer trainer = trainer("John", "Doe", "john.doe");
    trainer.assignId(99L);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> dao.update(trainer));

    assertEquals("Trainer not found.", exception.getMessage());
    assertTrue(storage.isEmpty());
  }

  private Trainer trainer(String firstName, String lastName, String username) {
    Trainer trainer = new Trainer();
    trainer.setFirstName(firstName);
    trainer.setLastName(lastName);
    trainer.setUsername(username);
    return trainer;
  }
}
