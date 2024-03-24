package ru.sveta.kanban.service;

public class ManagerFactory {

  public TaskManager getCreateDefaultTaskManager(){
    return new InMemoryTaskManager();
  }

}
