package ru.sveta.kanban.task;

import java.util.HashSet;
import java.util.Set;

public class Epic extends Task {

  private final Set<Integer> subTaskIds = new HashSet<>();

  public Epic(String title, String description) {
    super(title, description, TaskStatus.NEW);
  }

  public Set<Integer> getSubTaskIds() {
    return subTaskIds;
  }

  /**
   * Удаление под задач и обновление статуса
   */
  public void removeAllSubTasks() {
    subTaskIds.clear();
  }

  public void addSubTask(SubTask newSubTask) {
    subTaskIds.add(newSubTask.getId());
  }

  public void updateSubTask(SubTask subTask) {
    subTaskIds.remove(subTask.getId());
    subTaskIds.add(subTask.getId());
  }

  public void removeSubTask(SubTask subTask) {
    subTaskIds.remove(subTask.getId());
  }

  @Override
  public TaskType getTaskType() {
    return TaskType.EPIC;
  }
}
