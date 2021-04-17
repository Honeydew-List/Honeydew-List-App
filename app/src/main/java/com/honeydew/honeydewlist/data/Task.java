package com.honeydew.honeydewlist.data;

import java.util.List;

public class Task extends Item {
    private List<String> steps;
    private Long points;
    private Boolean completed = false;

    // Required for FireStore
    public Task() {
        super(); // Required for firestore
    }

    public Task(String name, String description, String owner, String uuid, Long points, List<String> steps, Boolean completed) {
        super(name, description, owner, uuid);
        this.steps = steps;
        this.points = points;
        this.completed = completed;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}