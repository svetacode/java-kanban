package ru.sveta.kanban.test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

}
