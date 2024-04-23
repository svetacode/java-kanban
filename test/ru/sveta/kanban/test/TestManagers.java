package ru.sveta.kanban.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import ru.sveta.kanban.service.HistoryManager;
import ru.sveta.kanban.service.Managers;
import ru.sveta.kanban.service.TaskManager;

public class TestManagers {

  @Test()
  @Order(1)
  @DisplayName("Проверка Managers - TaskManager")
  public void testDefaultTaskManager(){
    TaskManager taskManager = Managers.getDefaultTaskManager();
    assertNotNull(taskManager);
  }

  @Test()
  @Order(2)
  @DisplayName("Проверка Managers - HistoryManager")
  public void testDefaultHistoryManager(){
    HistoryManager historyManager = Managers.getDefaultHistoryManager();
    assertNotNull(historyManager);
  }

}
