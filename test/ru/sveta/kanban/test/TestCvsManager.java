package ru.sveta.kanban.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import ru.sveta.kanban.service.Managers;
import ru.sveta.kanban.service.TaskManager;

class TestCvsManager {

  @Test()
  @Order(1)
  @DisplayName("Проверка загрузки данных при старте приложения - пустой файл")
  void testLoadDataAtStartUp_Empty() throws IOException {
    File dataFile = File.createTempFile("kanban-board-empty", "csv");

    TaskManager taskManager = Managers.getCsvTaskManager(dataFile.getAbsolutePath());
    assertNotNull(taskManager);
  }

  @Test()
  @Order(2)
  @DisplayName("Проверка загрузки данных при старте приложения - файл с данными")
  void testLoadDataAtStartUp_WithData() throws IOException {
    File dataFile = File.createTempFile("kanban-board-data", "csv");
    loadDataToFile(dataFile);

    TaskManager taskManager = Managers.getCsvTaskManagerAndLoadData(dataFile.getAbsolutePath());
    assertNotNull(taskManager);

    assertNotNull(taskManager.getTaskById(1));
    assertNotNull(taskManager.getEpicById(3));
    assertNotNull(taskManager.getSubTaskById(4));
    assertNotNull(taskManager.getSubTaskById(5));
  }

  private void loadDataToFile(File file) throws IOException{
    try (FileWriter csvWriter = new FileWriter(file)) {
      // Write header row
      csvWriter.append("id,type,name, status,description,epic\n");
      csvWriter.append("1,TASK,Задача 1,DONE,Описание задачи 1\n");
      csvWriter.append("3,EPIC,Эпик 1,DONE,Описание Эпика 1\n");
      csvWriter.append("4,SUB_TASK,Подзадача 1.1,DONE,Описание подзадачи 1.1,3\n");
      csvWriter.append("5,SUB_TASK,Подзадача 1.2,DONE,Описание подзадачи 1.2,3\n");
    }
  }

}
