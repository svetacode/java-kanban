package ru.sveta.kanban.service;

public class Managers {

  public static HistoryManager getDefaultHistoryManager() {
    return new InMemoryHistoryManager();
  }

  public static TaskManager getDefaultTaskManager() {
    return new InMemoryTaskManager(getDefaultHistoryManager());
  }

  public static TaskManager getDefaultTaskManager(HistoryManager historyManager) {
    return new InMemoryTaskManager(historyManager);
  }

}
