package ru.sveta.kanban.test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import ru.sveta.kanban.service.HistoryManager;
import ru.sveta.kanban.service.Managers;
import ru.sveta.kanban.service.TaskManager;
import ru.sveta.kanban.task.Epic;
import ru.sveta.kanban.task.SubTask;
import ru.sveta.kanban.task.Task;
import ru.sveta.kanban.task.TaskStatus;
import ru.sveta.kanban.task.TaskType;

public class TestHistoryManager {

  @Test()
  @Order(1)
  @DisplayName("Проверка HistoryManager")
  public void testHistoryManager() {
    HistoryManager historyManager = Managers.getDefaultHistoryManager();
    TaskManager taskManager = Managers.getDefaultTaskManager(historyManager);

    Task task = new Task("Задача 1", "Описание задачи 1", TaskStatus.IN_PROGRESS);
    int createdTaskId = taskManager.createTask(task);
    Task createdTask = taskManager.getTaskById(createdTaskId);

    Epic epic = new Epic("Эпик 1", "Описание Эпика 1");
    int createdEpicId = taskManager.createEpic(epic);
    Epic createdEpic = taskManager.getEpicById(createdEpicId);

    SubTask epicSubTask = new SubTask("Подзадача 1.1", "Описание подзадачи 1.1", TaskStatus.NEW, createdEpic.getId());
    int subTask1Id = taskManager.createSubTask(epicSubTask);
    SubTask subTask = taskManager.getSubTaskById(subTask1Id);

    List<Task> taskList = historyManager.getViewHistory();
    assertEquals(3, taskList.size());
  }

  @Test()
  @Order(2)
  @DisplayName("Проверка HistoryManager - сохранение просмотренных версий")
  public void testHistoryManagerSaveHistoryObjects() {
    HistoryManager historyManager = Managers.getDefaultHistoryManager();
    TaskManager taskManager = Managers.getDefaultTaskManager(historyManager);

    Task task = new Task("Задача 1", "Описание задачи 1", TaskStatus.IN_PROGRESS);
    int createdTaskId = taskManager.createTask(task);
    Task createdTask = taskManager.getTaskById(createdTaskId);

    Epic epic = new Epic("Эпик 1", "Описание Эпика 1");
    int createdEpicId = taskManager.createEpic(epic);
    Epic createdEpic = taskManager.getEpicById(createdEpicId);

    SubTask epicSubTask = new SubTask("Подзадача 1.1", "Описание подзадачи 1.1", TaskStatus.NEW, createdEpic.getId());
    int subTask1Id = taskManager.createSubTask(epicSubTask);
    SubTask subTask = taskManager.getSubTaskById(subTask1Id);

    List<Task> taskList = historyManager.getViewHistory();
    assertEquals(3, taskList.size());

    createdTask.setTitle("обновление название");
    createdTask.setDescription("обновление описания");
    taskManager.updateTask(task);

    taskManager.deleteTaskByTypeAndId(TaskType.SUB_TASK, subTask.getId());

    taskList = historyManager.getViewHistory();
    assertEquals(3, taskList.size());

    Task taskFromHistory = taskList.get(0);
    SubTask subTaskFromHistory = (SubTask) taskList.get(2);

    assertAll("Task Data From History",
        () -> assertEquals(TaskType.TASK, taskFromHistory.getTaskType()),
        () -> assertEquals(createdTask.getId(), taskFromHistory.getId()),
        () -> assertEquals(TaskStatus.IN_PROGRESS, taskFromHistory.getStatus()),
        () -> assertEquals(createdTask.getTitle(), taskFromHistory.getTitle()),
        () -> assertEquals(createdTask.getDescription(), taskFromHistory.getDescription())
    );

    assertAll("SubTask Data From History",
        () -> assertEquals(TaskType.SUB_TASK, subTaskFromHistory.getTaskType()),
        () -> assertEquals(subTask.getId(), subTaskFromHistory.getId()),
        () -> assertEquals(TaskStatus.NEW, subTaskFromHistory.getStatus()),
        () -> assertEquals(subTask.getTitle(), subTaskFromHistory.getTitle()),
        () -> assertEquals(subTask.getDescription(), subTaskFromHistory.getDescription())
    );


  }

}
