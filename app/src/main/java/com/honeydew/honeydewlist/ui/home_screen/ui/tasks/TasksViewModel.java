package com.honeydew.honeydewlist.ui.home_screen.ui.tasks;
import com.honeydew.honeydewlist.data.Task;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;


public class TasksViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<Task> mTask;

    public TasksViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is tasks fragment");

        HashMap<String, Double> steps = new HashMap<String, Double>();
        steps.put("Step 1: do this first", 0.0);
        Task task = new Task("Task", "Do this", steps, false);
        mTask = new MutableLiveData<>();
        mTask.setValue(task);
    }

    public LiveData<String> getText() {
        return mText;
    }
}