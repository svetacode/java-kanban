import java.util.*;

public class TaskManager {
    private int nextTaskId;

    private final Map<TaskType, Set<Task>> tasksByType;
    private final Map<Integer, Task> tasksById;

    public TaskManager() {
        tasksByType = new HashMap<>();
        for (TaskType taskType : TaskType.values()) {
            tasksByType.put(taskType, new HashSet<>());
        }
        tasksById = new HashMap<>();

        nextTaskId = 1;
    }

    /**
     * Получение списка всех задач
     *
     * @param taskType тип задач список которых хотим получить
     * @return список задач или пустой список если их нет
     */
    public Set<Task> getTaskListByType(TaskType taskType) {
        return tasksByType.get(taskType);
    }

    public Task getTaskById(int taskId) {
        return tasksById.get(taskId);
    }

    public SubTask getSubTaskById(int subTaskId) {
        if (tasksById.containsKey(subTaskId)) {
            return (SubTask) tasksById.get(subTaskId);
        } else {
            return null;
        }
    }

    public Epic getEpicById(int epicId) {
        if (tasksById.containsKey(epicId)) {
            return (Epic) tasksById.get(epicId);
        } else {
            return null;
        }
    }

    public int createTask(Task newTask) {
        newTask.setId(nextTaskId);
        nextTaskId++;

        tasksById.put(newTask.getId(), newTask);
        tasksByType.get(TaskType.TASK).add(newTask);

        return newTask.getId();
    }

    public int createEpic(Epic newEpic) {
        newEpic.setId(nextTaskId);
        nextTaskId++;

        tasksById.put(newEpic.getId(), newEpic);
        tasksByType.get(TaskType.EPIC).add(newEpic);

        return newEpic.getId();
    }

    public int createSubTask(SubTask newSubTask) {
        newSubTask.setId(nextTaskId);
        nextTaskId++;

        tasksById.put(newSubTask.getId(), newSubTask);
        tasksByType.get(TaskType.SUB_TASK).add(newSubTask);

        newSubTask.getEpic().addSubTask(newSubTask);

        return newSubTask.getId();
    }

    /**
     * Удаление всех задач по типу
     *
     * @param taskType тип задач для удаления
     */
    public void deleteAllTaskByType(TaskType taskType) {
        switch (taskType) {
            case TASK -> {
                tasksByType.get(taskType).clear();
            }
            case EPIC -> {
                //Для эпиков надо удалить все связанные подзадачи
                for (Task task : tasksByType.get(taskType)) {
                    Epic epic = (Epic) task;
                    Set<SubTask> epicSubTasks = epic.getSubTasks();
                    tasksByType.get(TaskType.SUB_TASK).removeAll(epicSubTasks);
                }
                tasksByType.get(TaskType.EPIC).clear();
            }
            case SUB_TASK -> {
                //Если удаляем под задачи, надо удалить их из эпиков
                for (Task task : tasksByType.get(TaskType.EPIC)) {
                    Epic epic = (Epic) task;
                    epic.removeAllSubTasks();
                }
                tasksByType.get(TaskType.SUB_TASK).clear();
            }
        }
    }

    /**
     * Удаление задач по типу задачи и ее идентификатору
     *
     * @param taskType тип задач для удаления
     * @param taskId   идентификатор задачи для удаления
     */
    public void deleteTaskByTypeAndId(TaskType taskType, int taskId) {
        Task task = tasksById.get(taskId);
        if (task != null) {
            switch (taskType) {
                case TASK -> {
                    tasksByType.get(taskType).remove(task);
                }
                case EPIC -> {
                    //Для эпиков надо удалить все связанные подзадачи
                    Epic epic = (Epic) task;
                    Set<SubTask> epicSubTasks = epic.getSubTasks();
                    tasksByType.get(TaskType.SUB_TASK).removeAll(epicSubTasks);
                    tasksByType.get(taskType).remove(task);
                }
                case SUB_TASK -> {
                    SubTask subTask = (SubTask) task;
                    Epic epic = subTask.getEpic();
                    epic.removeSubTask(subTask);
                    tasksByType.get(TaskType.SUB_TASK).remove(task);
                }
            }
        }
        tasksById.remove(taskId);


    }

    public void updateTask(Task task) {
        tasksById.put(task.getId(), task);
        tasksByType.get(TaskType.TASK).remove(task);
        tasksByType.get(TaskType.TASK).add(task);
    }

    /**
     * Не учтена логика, когда подзадача переносится в другой эпик.
     *
     * @param subTask
     */
    public void updateSubTask(SubTask subTask) {
        tasksById.put(subTask.getId(), subTask);
        tasksByType.get(TaskType.SUB_TASK).remove(subTask);
        tasksByType.get(TaskType.SUB_TASK).add(subTask);
        subTask.getEpic().updateSubTask(subTask);
    }

    /**
     * Считаем, что мы не обновляем подзадачи (не добавляем и не удаляем), а только атрибуты самого эпика
     *
     * @param epic
     */
    public void updateEpic(Epic epic) {
        tasksById.put(epic.getId(), epic);
        tasksByType.get(TaskType.EPIC).remove(epic);
        tasksByType.get(TaskType.EPIC).add(epic);
    }

    public Set<SubTask> getEpicSubTasksByEpicId(int epicId) {
        if (tasksById.containsKey(epicId)) {
            return ((Epic) tasksById.get(epicId)).getSubTasks();
        } else {
            return Collections.emptySet();
        }
    }

}
