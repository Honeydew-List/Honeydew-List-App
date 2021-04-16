package com.honeydew.honeydewlist.data;

import java.util.List;

public class Task extends Item {
    private List<String> steps;
    private long points;
    private Boolean completed = false;
    public Task(String name, String description, long points, List<String> steps, Boolean completed) {
        super(name, description);
        this.steps = steps;
        this.points = points;
        this.completed = completed;
    }

    protected List<String> getSteps() {
        return steps;
    }

    protected void setSteps(List<String> steps) {
        this.steps = steps;
    }

    protected long getPoints() {
        return points;
    }

    protected void setPoints(long points) {
        this.points = points;
    }

    protected Boolean getCompleted() {
        return completed;
    }

    protected void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
