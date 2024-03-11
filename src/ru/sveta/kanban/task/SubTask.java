package ru.sveta.kanban.task;

public class SubTask extends Task{

    private Epic epic;

    public SubTask(String title, String description, TaskStatus status, Epic epic) {
        super(title, description, status);
        this.epic = epic;
    }


    public Epic getEpic() {
        return epic;
    }
}
