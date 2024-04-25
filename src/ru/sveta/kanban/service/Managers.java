package ru.sveta.kanban.service;

public final class Managers {

  private Managers() {
  }

  public static HistoryManager getDefaultHistoryManager() {
    return new InMemoryHistoryManager();
  }

  public static TaskManager getDefaultTaskManager() {
    return new InMemoryTaskManager();
  }

  public static TaskManager getCsvTaskManager(String storageFile) {
    return new FileBackedTaskManager(storageFile);
  }

  public static TaskManager getCsvTaskManagerAndLoadData(String storageFile) {
    return FileBackedTaskManager.loadData(storageFile);
  }

}
