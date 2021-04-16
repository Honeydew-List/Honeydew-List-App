package com.honeydew.honeydewlist.data;

import java.util.HashMap;

public class Task extends Item {
    private HashMap<String, Double> steps;
    public Task(String label, String description, HashMap<String, Double> steps, boolean completed) {
        super(label, description, completed);
        this.steps = steps;
    }


}
