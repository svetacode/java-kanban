package ru.sveta.kanban.service;

import java.util.List;
import ru.sveta.kanban.task.Task;

/**
 * Отвечает за историю просмотров
 */
public interface HistoryManager {

  /**
   * Добавляем задачу в историю просмотров
   *
   * @param task какая задача была просмотрена
   */
  void add(Task task);

  /**
   * История просмотр задач
   *
   * @return возвращает последние 10 просмотренных задачи
   */
  List<Task> getViewHistory();


  /**
   * Удаление задачи из списка просмотренных.
   * @param taskId идентификатор задачи
   */
  void remove(int taskId);
}
