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
import ru.sveta.kanban.task.Task;
import ru.sveta.kanban.task.TaskStatus;
import ru.sveta.kanban.task.TaskType;

public class TestTaskManagerTask {

  @Test()
  @Order(1)
  @DisplayName("Проверка TaskManager - создание Task")
  public void testTaskCreate() {
    TaskManager taskManager = Managers.getDefaultTaskManager();

    Task task = new Task("Задача 1", "Описание задачи 1", TaskStatus.IN_PROGRESS);
    int createdTaskId = taskManager.createTask(task);
    Task createdTask = taskManager.getTaskById(createdTaskId);
    assertNotNull(createdTask);

    assertAll("Task Data",
        () -> assertEquals(TaskType.TASK, createdTask.getTaskType()),
        () -> assertEquals(createdTaskId, createdTask.getId()),
        () -> assertEquals(TaskStatus.IN_PROGRESS, createdTask.getStatus()),
        () -> assertEquals(task.getTitle(), createdTask.getTitle()),
        () -> assertEquals(task.getDescription(), createdTask.getDescription())
    );
  }

  @Test()
  @Order(2)
  @DisplayName("Проверка TaskManager - обновление Task")
  public void testTaskUpdate() {
    TaskManager taskManager = Managers.getDefaultTaskManager();

    Task task = new Task("Задача 1", "Описание задачи 1", TaskStatus.IN_PROGRESS);
    int createdTaskId = taskManager.createTask(task);
    Task createdTask = taskManager.getTaskById(createdTaskId);
    createdTask.setTitle("обновление название");
    createdTask.setDescription("обновление описания");

    taskManager.updateTask(task);

    Task updatedTask = taskManager.getTaskById(createdTask.getId());
    assertNotNull(updatedTask);

    assertAll("Task Data After Update",
        () -> assertEquals(TaskType.TASK, updatedTask.getTaskType()),
        () -> assertEquals(createdTask.getId(), updatedTask.getId()),
        () -> assertEquals(TaskStatus.IN_PROGRESS, updatedTask.getStatus()),
        () -> assertEquals(createdTask.getTitle(), updatedTask.getTitle()),
        () -> assertEquals(createdTask.getDescription(), updatedTask.getDescription())
    );
  }

  @Test()
  @Order(3)
  @DisplayName("Проверка TaskManager - удаление Task")
  public void testTaskDelete() {
    TaskManager taskManager = Managers.getDefaultTaskManager();

    Task task = new Task("Задача 1", "Описание задачи 1", TaskStatus.IN_PROGRESS);
    int createdTaskId = taskManager.createTask(task);
    Task createdTask = taskManager.getTaskById(createdTaskId);
    assertNotNull(createdTask);

    taskManager.deleteTaskByTypeAndId(TaskType.TASK, createdTask.getId());

    Task deletedTask = taskManager.getTaskById(createdTask.getId());
    assertNull(deletedTask);
  }

}
