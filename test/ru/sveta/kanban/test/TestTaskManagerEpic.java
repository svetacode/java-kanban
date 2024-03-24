package ru.sveta.kanban.test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import ru.sveta.kanban.service.Managers;
import ru.sveta.kanban.service.TaskManager;
import ru.sveta.kanban.task.Epic;
import ru.sveta.kanban.task.Task;
import ru.sveta.kanban.task.TaskStatus;
import ru.sveta.kanban.task.TaskType;

public class TestTaskManagerEpic {

  @Test()
  @Order(1)
  @DisplayName("Проверка TaskManager - создание Epic")
  public void testTaskCreate() {
    TaskManager taskManager = Managers.getDefaultTaskManager();

    Epic task = new Epic("Эпик 1", "Описание Эпика 1");

    int createdTaskId = taskManager.createEpic(task);
    Epic createdTask = taskManager.getEpicById(createdTaskId);
    assertNotNull(createdTask);

    assertAll("Epic Data",
        () -> assertEquals(TaskType.EPIC, createdTask.getTaskType()),
        () -> assertEquals(createdTaskId, createdTask.getId()),
        () -> assertEquals(TaskStatus.NEW, createdTask.getStatus()),
        () -> assertEquals(task.getTitle(), createdTask.getTitle()),
        () -> assertEquals(task.getDescription(), createdTask.getDescription())
    );
  }

  @Test()
  @Order(2)
  @DisplayName("Проверка TaskManager - обновление Epic")
  public void testTaskUpdate() {
    TaskManager taskManager = Managers.getDefaultTaskManager();

    Epic task = new Epic("Эпик 1", "Описание Эпика 1");
    int createdTaskId = taskManager.createEpic(task);
    Task createdTask = taskManager.getTaskById(createdTaskId);
    createdTask.setTitle("обновление название");
    createdTask.setDescription("обновление описания");

    taskManager.updateEpic(task);

    Epic updatedTask = taskManager.getEpicById(createdTask.getId());
    assertNotNull(updatedTask);

    assertAll("Epic Data After Update",
        () -> assertEquals(TaskType.EPIC, updatedTask.getTaskType()),
        () -> assertEquals(createdTask.getId(), updatedTask.getId()),
        () -> assertEquals(TaskStatus.NEW, updatedTask.getStatus()),
        () -> assertEquals(createdTask.getTitle(), updatedTask.getTitle()),
        () -> assertEquals(createdTask.getDescription(), updatedTask.getDescription())
    );
  }

  @Test()
  @Order(3)
  @DisplayName("Проверка TaskManager - удаление Epic")
  public void testTaskDelete() {
    TaskManager taskManager = Managers.getDefaultTaskManager();

    Epic task = new Epic("Эпик 1", "Описание Эпика 1");
    int createdTaskId = taskManager.createEpic(task);
    Epic createdTask = taskManager.getEpicById(createdTaskId);
    assertNotNull(createdTask);

    taskManager.deleteTaskByTypeAndId(TaskType.EPIC, createdTask.getId());

    Epic deletedTask = taskManager.getEpicById(createdTask.getId());
    assertNull(deletedTask);
  }

}
