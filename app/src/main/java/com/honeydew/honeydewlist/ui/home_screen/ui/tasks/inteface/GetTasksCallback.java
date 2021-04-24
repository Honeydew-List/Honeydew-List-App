package com.honeydew.honeydewlist.ui.home_screen.ui.tasks.inteface;

import com.honeydew.honeydewlist.data.Task;

import java.util.List;

public interface GetTasksCallback {
    void onCallback(List<Task> taskList);
}
