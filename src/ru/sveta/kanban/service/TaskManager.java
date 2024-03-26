package ru.sveta.kanban.service;

import java.util.List;
import java.util.Set;
import ru.sveta.kanban.task.Epic;
import ru.sveta.kanban.task.SubTask;
import ru.sveta.kanban.task.Task;
import ru.sveta.kanban.task.TaskType;

public interface TaskManager {

  Set<Task> getTaskListByType(TaskType taskType);

  Task getTaskById(int taskId);

  SubTask getSubTaskById(int subTaskId);

  Epic getEpicById(int epicId);

  int createTask(Task newTask);

  int createEpic(Epic newEpic);

  int createSubTask(SubTask newSubTask);

  void updateEpicStatus(Epic epic);

  void deleteAllTask();

  void deleteAllEpic();

  void deleteAllSubTask();

  void deleteTaskByTypeAndId(TaskType taskType, int taskId);

  void updateTask(Task task);

  void updateSubTask(SubTask subTask);

  void updateEpic(Epic epic);

  Set<SubTask> getEpicSubTasksByEpicId(int epicId);

  List<Task> getViewHistory();

}
