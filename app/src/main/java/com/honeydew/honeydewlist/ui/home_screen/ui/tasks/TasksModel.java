package com.honeydew.honeydewlist.ui.home_screen.ui.tasks;

import com.honeydew.honeydewlist.data.Task;

import java.util.List;

public class TasksModel {
    private Task task;

    private TasksModel() {};

    private TasksModel(String name, String description, String owner, String uuid, long points, List<String> steps, boolean completionStatus) {
        this.task = new Task(name, description, owner, uuid, points, steps, completionStatus);
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
