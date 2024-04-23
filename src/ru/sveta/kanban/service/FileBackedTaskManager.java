package ru.sveta.kanban.service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import ru.sveta.kanban.exception.StorageException;
import ru.sveta.kanban.task.Epic;
import ru.sveta.kanban.task.SubTask;
import ru.sveta.kanban.task.Task;
import ru.sveta.kanban.task.TaskType;

public class FileBackedTaskManager extends InMemoryTaskManager {

  private final String storageFile;

  public FileBackedTaskManager(String storageFile) {
    super();
    this.storageFile = storageFile;
    loadData();
  }

  @Override
  public int createTask(Task newTask) {
    int id = super.createTask(newTask);
    saveToFile();
    return id;
  }

  @Override
  public int createEpic(Epic newEpic) {
    int id = super.createEpic(newEpic);
    saveToFile();
    return id;
  }

  @Override
  public int createSubTask(SubTask newSubTask) {
    int id = super.createSubTask(newSubTask);
    saveToFile();
    return id;
  }

  @Override
  public void updateTask(Task task) {
    super.updateTask(task);
    saveToFile();
  }

  @Override
  public void updateSubTask(SubTask subTask) {
    super.updateSubTask(subTask);
    saveToFile();
  }

  @Override
  public void updateEpic(Epic epic) {
    super.updateEpic(epic);
    saveToFile();
  }

  @Override
  public void updateEpicStatus(Epic epic) {
    super.updateEpicStatus(epic);
    saveToFile();
  }

  @Override
  public void deleteAllTask() {
    super.deleteAllTask();
    saveToFile();
  }

  @Override
  public void deleteAllEpic() {
    super.deleteAllEpic();
    saveToFile();
  }

  @Override
  public void deleteAllSubTask() {
    super.deleteAllSubTask();
    saveToFile();
  }

  @Override
  public void deleteTaskByTypeAndId(TaskType taskType, int taskId) {
    super.deleteTaskByTypeAndId(taskType, taskId);
    saveToFile();
  }

  /**
   * Сохраняем данные в файл.
   *
   * @throws StorageException ошибка сохранения
   */
  private void saveToFile() throws StorageException {
    try (FileWriter csvWriter = new FileWriter(storageFile)) {
      csvWriter.append("id,type,name, status,description,epic\n");
      for (Task task : tasksById.values()) {
        csvWriter.append(task.toCsvFormat());
      }
    } catch (IOException error) {
      throw new StorageException("Error while save data to storage file:" + error.getMessage(), error);
    }
  }

  /**
   * Метод загрузки данных из файла
   */
  private void loadData() {
    Path path = Paths.get(storageFile);

    if (path.toFile().exists()) {
      try (Stream<String> lines = Files.lines(path)) {
        tasksById.putAll(lines
            .skip(1)
            .map(taskDataAsString -> {
              String[] data = taskDataAsString.split(",");
              return switch (TaskType.valueOf(data[1])) {
                case EPIC -> Epic.fromCsvFormat(data);
                case TASK -> Task.fromCsvFormat(data);
                case SUB_TASK -> SubTask.fromCsvFormat(data);
              };
            }).collect(Collectors.toMap(Task::getId, Function.identity())));

        // Обновляем следующий идентификатор для новых задач
        tasksById.keySet().stream().max(Integer::compareTo).ifPresent(integer -> nextTaskId = integer + 1);
        ;
      } catch (IOException error) {
        throw new StorageException("Error while read data from storage file:" + error.getMessage(), error);
      }
    }
  }

}
