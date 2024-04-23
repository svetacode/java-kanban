package ru.sveta.kanban.task;

public class SubTask extends Task {

    private final Integer epicId;

    public SubTask(String title, String description, TaskStatus status, Integer epicId) {
        super(title, description, status);
        this.epicId = epicId;
    }

    private SubTask(int id, String title, String description, TaskStatus status, Integer epicId) {
        super(title, description, status);
        setId(id);
        this.epicId = epicId;
    }


    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.SUB_TASK;
    }

    @Override
    public String toCsvFormat(){
        return String.format("%s,%s,%s,%s,%s,%s\n", id, getTaskType(), title, status, description, epicId);
    }

    public static SubTask fromCsvFormat(String[] csv){
        return new SubTask(
            Integer.parseInt(csv[0]),
            csv[2],
            csv[4],
            TaskStatus.valueOf(csv[3]),
            Integer.parseInt(csv[5])
        );
    }

}
