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

}
