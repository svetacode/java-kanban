package ru.sveta.kanban.service;

import java.util.LinkedList;
import java.util.List;
import ru.sveta.kanban.task.Task;

public class InMemoryHistoryManager implements HistoryManager {

  /**
   * Исходя из ТЗ в будущем тут можно использовать TreeSet
   */
  private final LinkedList<Task> taskViewHistory;

  public InMemoryHistoryManager() {
    taskViewHistory = new LinkedList<>();
  }


  @Override
  public void add(Task task) {
    taskViewHistory.add(task);
  }

  @Override
  public List<Task> getViewHistory() {
    return taskViewHistory.stream()
        .limit(10)
        .toList();
  }
}
