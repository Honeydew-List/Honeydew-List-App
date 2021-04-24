package com.honeydew.honeydewlist.ui.home_screen.ui.tasks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TasksViewModel extends ViewModel {
    private MutableLiveData<Integer> checkedItem;

    public TasksViewModel() {
        checkedItem = new MutableLiveData<>();
    }

    public LiveData<Integer> getCheckedItem() {
        return checkedItem;
    }

    public void setCheckedItem(MutableLiveData<Integer> checkedItem) {
        this.checkedItem = checkedItem;
    }
}
