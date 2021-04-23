package com.honeydew.honeydewlist.data;

import java.util.List;

/**
 * Task
 * Note that the variable names must be the same as what is in Firestore
 */
public class Task extends Item {
    private Long points;
    private Boolean completionStatus = false;

    // Required for FireStore
    public Task() {
        super(); // Required for firestore
    }

    public Task(String name, String description, String owner, String uuid,
                Long points, Boolean completionStatus) {
        super(name, description, owner, uuid);
        this.points = points;
        this.completionStatus = completionStatus;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

    public Boolean getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(Boolean completionStatus) {
        this.completionStatus = completionStatus;
    }
}
