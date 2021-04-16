package com.honeydew.honeydewlist.ui.home_screen.ui.tasks;
import com.honeydew.honeydewlist.data.Task;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedHashMap;
import java.util.List;


public class TasksViewModel extends ViewModel {

    private Task task;

    private TasksViewModel() {};

    private TasksViewModel(String name, String description, long points, List<String> steps, Boolean completionStatus) {
        this.task = new Task(name, description, points, steps, completionStatus);
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}