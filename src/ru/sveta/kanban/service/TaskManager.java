package ru.sveta.kanban.service;

import java.util.Set;
import ru.sveta.kanban.task.Epic;
import ru.sveta.kanban.task.SubTask;
import ru.sveta.kanban.task.Task;
import ru.sveta.kanban.task.TaskType;

/**
 * @author m.gromov
 * @version 1.0
 * @since 1.0.0
 */
public interface TaskManager {

  Set<Task> getTaskListByType(TaskType taskType);

  Task getTaskById(int taskId);

  SubTask getSubTaskById(int subTaskId);

  Epic getEpicById(int epicId);

  int createTask(Task newTask);

  int createEpic(Epic newEpic);

  int createSubTask(SubTask newSubTask);

  void deleteAllTaskByType(TaskType taskType);

  void updateEpicStatus(Epic epic);

  void deleteTaskByTypeAndId(TaskType taskType, int taskId);

  void updateTask(Task task);

  void updateSubTask(SubTask subTask);

  void updateEpic(Epic epic);

  Set<SubTask> getEpicSubTasksByEpicId(int epicId);
}
