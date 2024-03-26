package ru.sveta.kanban.task;

public class SubTask extends Task{

    private final Integer epicId;

    public SubTask(String title, String description, TaskStatus status, Integer epicId) {
        super(title, description, status);
        this.epicId = epicId;
    }


    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getTaskType(){
        return TaskType.SUB_TASK;
    }

}
