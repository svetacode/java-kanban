package ru.sveta.kanban.test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import ru.sveta.kanban.service.Managers;
import ru.sveta.kanban.service.TaskManager;
import ru.sveta.kanban.task.Epic;
import ru.sveta.kanban.task.SubTask;
import ru.sveta.kanban.task.Task;
import ru.sveta.kanban.task.TaskStatus;
import ru.sveta.kanban.task.TaskType;

public class TestTaskManagerEpic {

  @Test()
  @Order(1)
  @DisplayName("Проверка TaskManager - создание Epic")
  public void testEpicCreate() {
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
  public void testEpicUpdate() {
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
  public void testEpicDelete() {
    TaskManager taskManager = Managers.getDefaultTaskManager();

    Epic task = new Epic("Эпик 1", "Описание Эпика 1");
    int createdTaskId = taskManager.createEpic(task);
    Epic createdTask = taskManager.getEpicById(createdTaskId);
    assertNotNull(createdTask);

    taskManager.deleteTaskByTypeAndId(TaskType.EPIC, createdTask.getId());

    Epic deletedTask = taskManager.getEpicById(createdTask.getId());
    assertNull(deletedTask);
  }

  @Test()
  @Order(4)
  @DisplayName("Проверка TaskManager - создание Epic с подзадачами")
  public void testEpicCreateWithSubtasks() {
    TaskManager taskManager = Managers.getDefaultTaskManager();

    Epic epic1 = new Epic("Эпик 1", "Описание Эпика 1");
    int epicId = taskManager.createEpic(epic1);
    Epic createdEpic = taskManager.getEpicById(epicId);
    assertEquals(TaskStatus.NEW, createdEpic.getStatus());

    SubTask epic1SubTask1 = new SubTask("Подзадача 1.1", "Описание подзадачи 1.1", TaskStatus.NEW, epic1.getId());
    int subTask1Id = taskManager.createSubTask(epic1SubTask1);
    SubTask epic1SubTask2 = new SubTask("Подзадача 1.2", "Описание подзадачи 1.2", TaskStatus.DONE, epic1.getId());
    int subTask2Id = taskManager.createSubTask(epic1SubTask2);

    createdEpic = taskManager.getEpicById(epicId);
    assertNotNull(createdEpic);
    assertNotNull(taskManager.getSubTaskById(subTask1Id));
    assertNotNull(taskManager.getSubTaskById(subTask2Id));

    Set<SubTask> subTaskSet = taskManager.getEpicSubTasksByEpicId(epicId);
    assertEquals(2, subTaskSet.size());

    assertEquals(TaskStatus.IN_PROGRESS, createdEpic.getStatus());

  }

  @Test()
  @Order(5)
  @DisplayName("Проверка TaskManager - создание Epic с кривыми подзадачами")
  public void testEpicCreateWithWrongSubtasks() {
    TaskManager taskManager = Managers.getDefaultTaskManager();

    Task task = new Task("Задача 1", "Описание задачи 1", TaskStatus.IN_PROGRESS);
    int createdTaskId = taskManager.createTask(task);


    Epic epic1 = new Epic("Эпик 1", "Описание Эпика 1");
    int epicId = taskManager.createEpic(epic1);
    Epic createdEpic = taskManager.getEpicById(epicId);
    assertEquals(TaskStatus.NEW, createdEpic.getStatus());

    SubTask epic1SubTask1 = new SubTask("Подзадача 1.1", "Описание подзадачи 1.1", TaskStatus.NEW, createdTaskId);
    int subTask1Id = taskManager.createSubTask(epic1SubTask1);
    assertEquals(-1, subTask1Id);

    SubTask epic1SubTask2 = new SubTask("Подзадача 1.2", "Описание подзадачи 1.2", TaskStatus.DONE, 100);
    int subTask2Id = taskManager.createSubTask(epic1SubTask2);
    assertEquals(-1, subTask2Id);

  }

}
