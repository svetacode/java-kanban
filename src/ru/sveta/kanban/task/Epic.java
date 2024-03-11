package ru.sveta.kanban.task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Epic extends Task {

    private final Set<SubTask> subTasks = new HashSet<>();

    public Epic(String title, String description) {
        super(title, description, TaskStatus.NEW);
    }

    public Set<SubTask> getSubTasks() {
        return subTasks;
    }

    /**
     * Удаление под задач и обновление статуса
     */
    public void removeAllSubTasks() {
        subTasks.clear();
        updateEpicStatus();
    }

    public void addSubTask(SubTask newSubTask) {
        subTasks.add(newSubTask);
        updateEpicStatus();
    }

    public void updateSubTask(SubTask subTask) {
        subTasks.remove(subTask);
        subTasks.add(subTask);
        updateEpicStatus();
    }

    private void updateEpicStatus() {
        if (!subTasks.isEmpty()) {
            List<Integer> newList = new ArrayList<>();
            List<Integer> doneList = new ArrayList<>();
            for (SubTask subTask : subTasks) {
                if (subTask.getStatus().equals(TaskStatus.NEW)) {
                    newList.add(subTask.getId());
                } else if (subTask.getStatus().equals(TaskStatus.DONE)) {
                    doneList.add(subTask.getId());
                }
            }

            if (newList.size() == subTasks.size()) {
                status = TaskStatus.NEW;
            } else if (doneList.size() == subTasks.size()) {
                status = TaskStatus.DONE;
            } else {
                status = TaskStatus.IN_PROGRESS;
            }
        } else {
            status = TaskStatus.NEW;
        }

    }

    public void removeSubTask(SubTask subTask) {
        subTasks.remove(subTask);
        updateEpicStatus();
    }
}
