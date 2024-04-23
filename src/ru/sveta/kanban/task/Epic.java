package ru.sveta.kanban.task;

import java.util.HashSet;
import java.util.Set;

public class Epic extends Task {

  private final Set<Integer> subTaskIds = new HashSet<>();

  public Epic(String title, String description) {
    super(title, description, TaskStatus.NEW);
  }

  private Epic(int id, String title, String description, TaskStatus status) {
    super(title, description, status);
    setId(id);
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

  public void removeSubTask(SubTask subTask) {
    subTaskIds.remove(subTask.getId());
  }

  @Override
  public TaskType getTaskType() {
    return TaskType.EPIC;
  }

  public static Epic fromCsvFormat(String[] csv){
    return new Epic(
        Integer.parseInt(csv[0]),
        csv[2],
        csv[4],
        TaskStatus.valueOf(csv[3])
    );
  }
}
