package ru.sveta.kanban.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import ru.sveta.kanban.task.Epic;
import ru.sveta.kanban.task.SubTask;
import ru.sveta.kanban.task.Task;
import ru.sveta.kanban.task.TaskStatus;
import ru.sveta.kanban.task.TaskType;

public class InMemoryTaskManager implements TaskManager {

  private int nextTaskId;

  private final Map<Integer, Task> tasksById;

  public InMemoryTaskManager() {
    tasksById = new HashMap<>();
    nextTaskId = 1;
  }

  /**
   * Получение списка всех задач
   *
   * @param taskType тип задач список которых хотим получить
   * @return список задач или пустой список если их нет
   */
  @Override
  public Set<Task> getTaskListByType(TaskType taskType) {
    Set<Task> taskList = new HashSet<>();
    for (Task task : tasksById.values()) {
      if (task.getTaskType().equals(taskType)) {
        taskList.add(task);
      }
    }
    return taskList;
  }

  @Override
  public Task getTaskById(int taskId) {
    return tasksById.get(taskId);
  }

  @Override
  public SubTask getSubTaskById(int subTaskId) {
    if (tasksById.containsKey(subTaskId)) {
      return (SubTask) tasksById.get(subTaskId);
    } else {
      return null;
    }
  }

  @Override
  public Epic getEpicById(int epicId) {
    if (tasksById.containsKey(epicId)) {
      return (Epic) tasksById.get(epicId);
    } else {
      return null;
    }
  }

  @Override
  public int createTask(Task newTask) {
    newTask.setId(nextTaskId);
    nextTaskId++;

    tasksById.put(newTask.getId(), newTask);

    return newTask.getId();
  }

  @Override
  public int createEpic(Epic newEpic) {
    newEpic.setId(nextTaskId);
    nextTaskId++;

    tasksById.put(newEpic.getId(), newEpic);

    return newEpic.getId();
  }

  @Override
  public int createSubTask(SubTask newSubTask) {
    if (tasksById.containsKey(newSubTask.getEpicId())) {
      Epic epic = (Epic)tasksById.get(newSubTask.getEpicId());
      newSubTask.setId(nextTaskId);
      nextTaskId++;

      tasksById.put(newSubTask.getId(), newSubTask);

      epic.addSubTask(newSubTask);
      updateEpicStatus(epic);

      return newSubTask.getId();
    } else {
      return -1;
    }
  }

  /**
   * Удаление всех задач по типу
   *
   * @param taskType тип задач для удаления
   */
  @Override
  public void deleteAllTaskByType(TaskType taskType) {
    for (Task task : tasksById.values()) {
      if (task.getTaskType().equals(taskType)) {
        if (task.getTaskType().equals(TaskType.EPIC)){
          // Надо удалить все подзадачи
          Epic epic = (Epic)task;
          Set<Integer> subtaskIds = epic.getSubTaskIds();
          for (Integer subtaskId : subtaskIds) {
            tasksById.remove(subtaskId);
          }
        }
        tasksById.remove(task.getId());
      }
    }
  }

  /**
   * Удаление задач по типу задачи и ее идентификатору
   *
   * @param taskType тип задач для удаления
   * @param taskId   идентификатор задачи для удаления
   */
  @Override
  public void deleteTaskByTypeAndId(TaskType taskType, int taskId) {
    Task task = tasksById.get(taskId);
    if (task != null) {
      switch (taskType) {
        case TASK -> {
          tasksById.remove(taskId);
        }
        case EPIC -> {
          //Для эпиков надо удалить все связанные подзадачи
          Epic epic = (Epic) task;
          Set<Integer> epicSubTasks = epic.getSubTaskIds();
          for (Integer subTaskId : epicSubTasks) {
            tasksById.remove(subTaskId);
          }
          tasksById.remove(taskId);
        }
        case SUB_TASK -> {
          SubTask subTask = (SubTask) task;
          Integer epicId = subTask.getEpicId();
          Epic epic = (Epic)tasksById.get(epicId);
          epic.removeSubTask(subTask);
          updateEpicStatus(epic);
          tasksById.remove(taskId);
        }
      }
    }

  }

  @Override
  public void updateTask(Task task) {
    if (tasksById.containsKey(task.getId())) {
      tasksById.put(task.getId(), task);
    }
  }

  /**
   * Не учтена логика, когда подзадача переносится в другой эпик.
   *
   * @param subTask
   */
  @Override
  public void updateSubTask(SubTask subTask) {
    if (
        (tasksById.containsKey(subTask.getId())) &&
        ((SubTask)tasksById.get(subTask.getId())).getEpicId().equals(subTask.getEpicId())
    ){
      tasksById.put(subTask.getId(), subTask);
      Epic epic = (Epic)tasksById.get(subTask.getEpicId());
      updateEpicStatus(epic);
    }
  }

  /**
   * Считаем, что мы не обновляем подзадачи (не добавляем и не удаляем), а только атрибуты самого эпика
   *
   * @param epic
   */
  @Override
  public void updateEpic(Epic epic) {
    if (tasksById.containsKey(epic.getId())) {
      Epic epicOld = (Epic)tasksById.get(epic.getId());
      epicOld.setDescription(epic.getDescription());
      epicOld.setTitle(epic.getTitle());
    }
  }

  @Override
  public Set<SubTask> getEpicSubTasksByEpicId(int epicId) {
    if (tasksById.containsKey(epicId)) {
      Set<Integer> subTaskIds = ((Epic) tasksById.get(epicId)).getSubTaskIds();
      Set<SubTask> subTasks = new HashSet<>();
      for (Integer subTaskId : subTaskIds) {
        subTasks.add((SubTask)tasksById.get(subTaskId));
      }
      return subTasks;
    } else {
      return Collections.emptySet();
    }
  }

  @Override
  public void updateEpicStatus(Epic epic) {
    Set<Integer> subTaskIds = epic.getSubTaskIds();
    if (!subTaskIds.isEmpty()) {
      int newList = 0;
      int doneList = 0;
      for (Integer subTaskId : subTaskIds) {
        SubTask subTask = (SubTask) tasksById.get(subTaskId);
        if (subTask.getStatus().equals(TaskStatus.NEW)) {
          newList++;
        } else if (subTask.getStatus().equals(TaskStatus.DONE)) {
          doneList++;
        }
      }
      if (newList == subTaskIds.size()) {
        epic.setStatus(TaskStatus.NEW);
      } else if (doneList == subTaskIds.size()) {
        epic.setStatus(TaskStatus.DONE);
      } else {
        epic.setStatus(TaskStatus.IN_PROGRESS);
      }
    } else {
      epic.setStatus(TaskStatus.NEW);
    }
  }
}
