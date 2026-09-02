package com.project.gymcrmsystem.dao;

import com.project.gymcrmsystem.model.Trainee;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeDaoImplTest {

  @Mock
  private EntityManager entityManager;

  private TraineeDaoImpl dao;

  @BeforeEach
  void setUp() {
    dao = new TraineeDaoImpl();
    ReflectionTestUtils.setField(dao, "em", entityManager);
  }

  @Test
  void shouldSaveTraineeAssignIdAndStoreIt() {
    Trainee trainee = trainee("Jane", "Doe", "jane.doe");
    doAnswer(invocation -> {
      trainee.setId(1L);
      return null;
    }).when(entityManager).persist(trainee);

    Trainee saved = dao.save(trainee);

    assertSame(trainee, saved);
    assertEquals(1L, trainee.getId());
    verify(entityManager).persist(trainee);
  }

  @Test
  void shouldContinueIdSequenceFromExistingStorage() {
    Trainee newTrainee = trainee("Jane", "Doe", "jane.doe");
    doAnswer(invocation -> {
      newTrainee.setId(6L);
      return null;
    }).when(entityManager).persist(newTrainee);

    dao.save(newTrainee);

    assertEquals(6L, newTrainee.getId());
    verify(entityManager).persist(newTrainee);
  }

  @Test
  void shouldUpdateExistingTrainee() {
    Trainee existing = trainee("Jane", "Doe", "jane.doe");
    existing.setId(1L);
    Trainee updated = trainee("Jane", "Smith", "jane.smith");
    updated.setId(1L);
    when(entityManager.find(Trainee.class, 1L)).thenReturn(existing);
    when(entityManager.merge(updated)).thenReturn(updated);

    Trainee result = dao.update(updated);

    assertSame(updated, result);
    verify(entityManager).merge(updated);
  }

  @Test
  void shouldNotUpdateTraineeThatDoesNotExist() {
    Trainee trainee = trainee("Jane", "Doe", "jane.doe");
    trainee.setId(99L);
    when(entityManager.find(Trainee.class, 99L)).thenReturn(null);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> dao.update(trainee));

    assertEquals("Trainee not found.", exception.getMessage());
    verify(entityManager, never()).merge(trainee);
  }

  @Test
  void shouldDeleteExistingTrainee() {
    Trainee trainee = trainee("Jane", "Doe", "jane.doe");
    trainee.setId(1L);
    when(entityManager.find(Trainee.class, 1L)).thenReturn(trainee);

    dao.deleteById(1L);

    verify(entityManager).remove(trainee);
  }

  @Test
  void shouldLeaveOtherTraineesWhenDeletingUnknownId() {
    when(entityManager.find(Trainee.class, 99L)).thenReturn(null);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> dao.deleteById(99L));

    assertEquals("Trainee not found.", exception.getMessage());
    verify(entityManager, never()).remove(any());
  }

  private Trainee trainee(String firstName, String lastName, String username) {
    Trainee trainee = new Trainee();
    trainee.setFirstName(firstName);
    trainee.setLastName(lastName);
    trainee.setUsername(username);
    return trainee;
  }
}
