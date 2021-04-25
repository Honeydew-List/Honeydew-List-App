package com.honeydew.honeydewlist.ui.home_screen.ui.tasks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.honeydew.honeydewlist.data.Task;

import java.util.ArrayList;

public class TasksViewModel extends ViewModel {
    private MutableLiveData<Integer> checkedItem;
    private MutableLiveData<ArrayList<Task>> dataModalArrayList;

    public TasksViewModel() {
        checkedItem = new MutableLiveData<>();
        dataModalArrayList = new MutableLiveData<>();
    }

    public LiveData<Integer> getCheckedItem() {
        return checkedItem;
    }

    public void setCheckedItem(MutableLiveData<Integer> checkedItem) {
        this.checkedItem = checkedItem;
    }

    public LiveData<ArrayList<Task>> getDataModalArrayList() {
        return dataModalArrayList;
    }

    public void setDataModalArrayList(MutableLiveData<ArrayList<Task>> dataModalArrayList) {
        this.dataModalArrayList = dataModalArrayList;
    }
}
