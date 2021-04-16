package com.honeydew.honeydewlist.data;

import java.util.HashMap;

public class Task extends Item {
    private HashMap<String, Double> steps;
    public Task(String label, String description, HashMap<String, Double> steps, boolean completed) {
        this.label = label;
        this.description = description;
        this.steps = steps;
        this.completed = completed;
    }

    /**
     * setLabel
     * @param label the name of task
     * @return success Return a boolean value indicating if it was successful
     */
    public boolean setLabel(String label) {
        // Could check name against blacklisted words
        // Then return false if there is
        this.label = label;
        return true;
    }


}
