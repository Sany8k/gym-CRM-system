package com.project.gymcrmsystem.dao;

import com.project.gymcrmsystem.model.Trainer;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerDaoImplTest {

  @Mock
  private EntityManager entityManager;

  private TrainerDaoImpl dao;

  @BeforeEach
  void setUp() {
    dao = new TrainerDaoImpl();
    ReflectionTestUtils.setField(dao, "em", entityManager);
  }

  @Test
  void shouldSaveTrainerAssignIdAndStoreIt() {
    Trainer trainer = trainer("John", "Doe", "john.doe");
    doAnswer(invocation -> {
      trainer.setId(1L);
      return null;
    }).when(entityManager).persist(trainer);

    Trainer saved = dao.save(trainer);

    assertSame(trainer, saved);
    assertEquals(1L, trainer.getId());
    verify(entityManager).persist(trainer);
  }

  @Test
  void shouldContinueIdSequenceFromExistingStorage() {
    Trainer newTrainer = trainer("John", "Doe", "john.doe");
    doAnswer(invocation -> {
      newTrainer.setId(6L);
      return null;
    }).when(entityManager).persist(newTrainer);

    dao.save(newTrainer);

    assertEquals(6L, newTrainer.getId());
    verify(entityManager).persist(newTrainer);
  }

  @Test
  void shouldUpdateExistingTrainer() {
    Trainer existing = trainer("John", "Doe", "john.doe");
    existing.setId(1L);
    Trainer updated = trainer("John", "Smith", "john.smith");
    updated.setId(1L);
    when(entityManager.find(Trainer.class, 1L)).thenReturn(existing);
    when(entityManager.merge(updated)).thenReturn(updated);

    Trainer result = dao.update(updated);

    assertSame(updated, result);
    verify(entityManager).merge(updated);
  }

  @Test
  void shouldNotUpdateTrainerThatDoesNotExist() {
    Trainer trainer = trainer("John", "Doe", "john.doe");
    trainer.setId(99L);
    when(entityManager.find(Trainer.class, 99L)).thenReturn(null);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> dao.update(trainer));

    assertEquals("Trainer not found.", exception.getMessage());
    verify(entityManager, never()).merge(trainer);
  }

  private Trainer trainer(String firstName, String lastName, String username) {
    Trainer trainer = new Trainer();
    trainer.setFirstName(firstName);
    trainer.setLastName(lastName);
    trainer.setUsername(username);
    return trainer;
  }
}
