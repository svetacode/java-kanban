package ru.sveta.kanban.test;

import java.io.File;
import java.io.IOException;
import ru.sveta.kanban.exception.ManagerSaveException;
import ru.sveta.kanban.service.Managers;
import ru.sveta.kanban.service.TaskManager;

class TestCsvTaskManagerEpic extends TestTaskManagerEpic {

  @Override
  protected TaskManager getTaskManager() {
    try {
      File dataFile = File.createTempFile("kanban-board-empty", "csv");
      return Managers.getCsvTaskManagerAndLoadData(dataFile.getAbsolutePath());
    } catch (IOException exception) {
      throw new ManagerSaveException("Can't create temp storage file", exception);
    }

  }

}
