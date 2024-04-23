package ru.sveta.kanban.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import ru.sveta.kanban.service.Managers;
import ru.sveta.kanban.service.TaskManager;
import ru.sveta.kanban.task.Epic;
import ru.sveta.kanban.task.SubTask;
import ru.sveta.kanban.task.Task;
import ru.sveta.kanban.task.TaskStatus;
import ru.sveta.kanban.task.TaskType;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestHistoryManager {

  @Test()
  @Order(1)
  @DisplayName("Проверка HistoryManager")
  public void testHistoryManager() {
    TaskManager taskManager = Managers.getDefaultTaskManager();

    Task task = new Task("Задача 1", "Описание задачи 1", TaskStatus.IN_PROGRESS);
    int createdTaskId = taskManager.createTask(task);
    taskManager.getTaskById(createdTaskId);

    Epic epic = new Epic("Эпик 1", "Описание Эпика 1");
    int createdEpicId = taskManager.createEpic(epic);
    Epic createdEpic = taskManager.getEpicById(createdEpicId);

    SubTask epicSubTask = new SubTask("Подзадача 1.1", "Описание подзадачи 1.1", TaskStatus.NEW, createdEpic.getId());
    int subTask1Id = taskManager.createSubTask(epicSubTask);
    taskManager.getSubTaskById(subTask1Id);

    List<Task> taskList = taskManager.getViewHistory();
    assertEquals(3, taskList.size());

    assertEquals(createdTaskId, taskList.get(0).getId());
    assertEquals(createdEpicId, taskList.get(1).getId());
    assertEquals(subTask1Id, taskList.get(2).getId());
  }

  @Test()
  @Order(2)
  @DisplayName("Проверка HistoryManager - обновление просмотра")
  public void testHistoryManager_UpdateView() {
    TaskManager taskManager = Managers.getDefaultTaskManager();

    Task task = new Task("Задача 1", "Описание задачи 1", TaskStatus.IN_PROGRESS);
    int createdTaskId = taskManager.createTask(task);
    taskManager.getTaskById(createdTaskId);

    Epic epic = new Epic("Эпик 1", "Описание Эпика 1");
    int createdEpicId = taskManager.createEpic(epic);
    Epic createdEpic = taskManager.getEpicById(createdEpicId);

    SubTask epicSubTask = new SubTask("Подзадача 1.1", "Описание подзадачи 1.1", TaskStatus.NEW, createdEpic.getId());
    int subTask1Id = taskManager.createSubTask(epicSubTask);
    taskManager.getSubTaskById(subTask1Id);

    List<Task> taskList = taskManager.getViewHistory();
    assertEquals(3, taskList.size());

    taskManager.getTaskById(createdTaskId);

    taskList = taskManager.getViewHistory();

    assertEquals(createdEpicId, taskList.get(0).getId());
    assertEquals(subTask1Id, taskList.get(1).getId());
    assertEquals(createdTaskId, taskList.get(2).getId());
  }

  @Test()
  @Order(3)
  @DisplayName("Проверка HistoryManager - удаление епика")
  public void testHistoryManager_DeleteEpic() {
    TaskManager taskManager = Managers.getDefaultTaskManager();

    Task task = new Task("Задача 1", "Описание задачи 1", TaskStatus.IN_PROGRESS);
    int createdTaskId = taskManager.createTask(task);
    taskManager.getTaskById(createdTaskId);

    Epic epic = new Epic("Эпик 1", "Описание Эпика 1");
    int createdEpicId = taskManager.createEpic(epic);
    Epic createdEpic = taskManager.getEpicById(createdEpicId);

    SubTask epicSubTask = new SubTask("Подзадача 1.1", "Описание подзадачи 1.1", TaskStatus.NEW, createdEpic.getId());
    int subTask1Id = taskManager.createSubTask(epicSubTask);
    taskManager.getSubTaskById(subTask1Id);

    List<Task> taskList = taskManager.getViewHistory();
    assertEquals(3, taskList.size());

    taskManager.deleteTaskByTypeAndId(TaskType.EPIC, createdEpicId);

    taskList = taskManager.getViewHistory();

    assertEquals(1, taskList.size());
    assertEquals(createdTaskId, taskList.get(0).getId());
  }

  @Test()
  @Order(4)
  @DisplayName("Проверка HistoryManager - проверка повторных просмотров епика")
  public void testHistoryManager_CheckView() {
    TaskManager taskManager = Managers.getDefaultTaskManager();

    Task task = new Task("Задача 1", "Описание задачи 1", TaskStatus.IN_PROGRESS);
    int createdTaskId = taskManager.createTask(task);
    taskManager.getTaskById(createdTaskId);

    Epic epic = new Epic("Эпик 1", "Описание Эпика 1");
    int createdEpicId = taskManager.createEpic(epic);
    Epic createdEpic = taskManager.getEpicById(createdEpicId);

    SubTask epicSubTask = new SubTask("Подзадача 1.1", "Описание подзадачи 1.1", TaskStatus.NEW, createdEpic.getId());
    int subTask1Id = taskManager.createSubTask(epicSubTask);
    taskManager.getSubTaskById(subTask1Id);

    List<Task> taskList = taskManager.getViewHistory();
    assertEquals(3, taskList.size());

    assertEquals(createdTaskId, taskList.get(0).getId());
    assertEquals(createdEpicId, taskList.get(1).getId());
    assertEquals(subTask1Id, taskList.get(2).getId());

    taskManager.getSubTaskById(subTask1Id);
    taskList = taskManager.getViewHistory();

    assertEquals(createdTaskId, taskList.get(0).getId());
    assertEquals(createdEpicId, taskList.get(1).getId());
    assertEquals(subTask1Id, taskList.get(2).getId());

    taskManager.getTaskById(createdTaskId);
    taskList = taskManager.getViewHistory();

    assertEquals(createdEpicId, taskList.get(0).getId());
    assertEquals(subTask1Id, taskList.get(1).getId());
    assertEquals(createdTaskId, taskList.get(2).getId());

  }

}
