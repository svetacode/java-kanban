package ru.sveta.kanban;

import java.util.Collection;
import java.util.List;

import ru.sveta.kanban.service.Managers;
import ru.sveta.kanban.service.TaskManager;
import ru.sveta.kanban.task.Epic;
import ru.sveta.kanban.task.SubTask;
import ru.sveta.kanban.task.Task;
import ru.sveta.kanban.task.TaskStatus;
import ru.sveta.kanban.task.TaskType;

public class Main {

    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = Managers.getDefaultTaskManager();
        System.out.println("Поехали!");
        testTaskManager(inMemoryTaskManager);

        TaskManager csvTaskManager = Managers.getCsvTaskManager("kanban-board.csv");
        testTaskManagerFileStorage(csvTaskManager);

        TaskManager csvTaskManager2 = Managers.getCsvTaskManager("kanban-board.csv");
        testTaskManagerFileStorage2(csvTaskManager2);
    }

    private static void testTaskManagerFileStorage2(TaskManager taskManager) {
        System.out.println("\nДанные загруженные из файла\n");
        printAllTasks(taskManager);
    }

    private static void testTaskManagerFileStorage(TaskManager taskManager) {
        Task task1 = new Task("Задача 1", "Описание задачи 1", TaskStatus.IN_PROGRESS);
        taskManager.createTask(task1);
        taskManager.getTaskById(task1.getId());

        Task task2 = new Task("Задача 2", "Описание задачи 2", TaskStatus.NEW);
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Эпик 1", "Описание Эпика 1");
        taskManager.createEpic(epic1);
        SubTask epic1SubTask1 = new SubTask("Подзадача 1.1", "Описание подзадачи 1.1", TaskStatus.NEW, epic1.getId());
        taskManager.createSubTask(epic1SubTask1);
        SubTask epic1SubTask2 = new SubTask("Подзадача 1.2", "Описание подзадачи 1.2", TaskStatus.DONE, epic1.getId());
        taskManager.createSubTask(epic1SubTask2);

        Epic epic2 = new Epic("Эпик 2", "Описание Эпика 2");
        taskManager.createEpic(epic2);
        SubTask epic2SubTask1 = new SubTask("Подзадача 2.1", "Описание подзадачи 2.1", TaskStatus.NEW, epic2.getId());
        taskManager.createSubTask(epic2SubTask1);

        printAllTasks(taskManager);

        epic1SubTask1.setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(epic1SubTask1);
        epic1SubTask2.setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(epic1SubTask2);

        epic2SubTask1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubTask(epic2SubTask1);

        task1.setStatus(TaskStatus.DONE);
        taskManager.updateTask(task1);

        System.out.println("\nПроизошло обновление данных\n");

        printAllTasks(taskManager);

        System.out.println("\nПроизошло удаление данных\n");

        taskManager.deleteTaskByTypeAndId(TaskType.TASK, task2.getId());
        taskManager.deleteTaskByTypeAndId(TaskType.EPIC, epic2.getId());

        printAllTasks(taskManager);

        printTaskHistory(taskManager.getViewHistory());
    }

    private static void testTaskManager(TaskManager taskManager) {
        Task task1 = new Task("Задача 1", "Описание задачи 1", TaskStatus.IN_PROGRESS);
        taskManager.createTask(task1);
        taskManager.getTaskById(task1.getId());

        Task task2 = new Task("Задача 2", "Описание задачи 2", TaskStatus.NEW);
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Эпик 1", "Описание Эпика 1");
        taskManager.createEpic(epic1);
        SubTask epic1SubTask1 = new SubTask("Подзадача 1.1", "Описание подзадачи 1.1", TaskStatus.NEW, epic1.getId());
        taskManager.createSubTask(epic1SubTask1);
        SubTask epic1SubTask2 = new SubTask("Подзадача 1.2", "Описание подзадачи 1.2", TaskStatus.DONE, epic1.getId());
        taskManager.createSubTask(epic1SubTask2);

        Epic epic2 = new Epic("Эпик 2", "Описание Эпика 2");
        taskManager.createEpic(epic2);
        SubTask epic2SubTask1 = new SubTask("Подзадача 2.1", "Описание подзадачи 2.1", TaskStatus.NEW, epic2.getId());
        taskManager.createSubTask(epic2SubTask1);

        printAllTasks(taskManager);

        epic1SubTask1.setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(epic1SubTask1);
        epic1SubTask2.setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(epic1SubTask2);

        epic2SubTask1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubTask(epic2SubTask1);

        task1.setStatus(TaskStatus.DONE);
        taskManager.updateTask(task1);

        System.out.println("\nПроизошло обновление данных\n");

        printAllTasks(taskManager);

        System.out.println("\nПроизошло удаление данных\n");

        taskManager.deleteTaskByTypeAndId(TaskType.TASK, task2.getId());
        taskManager.deleteTaskByTypeAndId(TaskType.EPIC, epic2.getId());

        printAllTasks(taskManager);

        printTaskHistory(taskManager.getViewHistory());
    }

    private static void printAllTasks(TaskManager taskManager) {
        System.out.println("Список задач:");
        printTaskList(taskManager.getTaskListByType(TaskType.TASK));
        System.out.println("Список эпиков:");
        printTaskList(taskManager.getTaskListByType(TaskType.EPIC));
        System.out.println("Список подзадач:");
        printTaskList(taskManager.getTaskListByType(TaskType.SUB_TASK));
    }

    private static void printTaskList(Collection<Task> taskListByType) {
        for (Task task: taskListByType) {
            System.out.printf("\t %s - %s - %s [%s] %n",
                    task.getId(),
                    task.getTitle(),
                    task.getDescription(),
                    task.getStatus()
            );
        }
    }

    private static void printTaskHistory(List<Task> taskList) {
        System.out.println("Список просмотренных задач:");
        printTaskList(taskList);
    }
}




