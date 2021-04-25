package com.honeydew.honeydewlist.data;

import java.util.List;

/**
 * Task
 * Note that the variable names must be the same as what is in Firestore
 */
public class Task extends Item {
    private Long points;
    private Boolean completionStatus = false;
    private String completionDoer;
    private String completionDoerUUID;
    private Boolean verifiedStatus = false;

    // Required for FireStore
    public Task() {
        super(); // Required for firestore
    }

    public Task(String name, String description, String owner, String uuid,
                Long points, Boolean completionStatus, String completionDoer, String completionDoerUUID, Boolean verifiedStatus, String itemID) {
        super(name, description, owner, uuid, itemID);
        this.points = points;
        this.completionStatus = completionStatus;
        this.completionDoer = completionDoer;
        this.completionDoerUUID = completionDoerUUID;
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

    public Boolean getVerifiedStatus() {
        return verifiedStatus;
    }

    public void setVerifiedStatus(Boolean verifiedStatus) {
        this.verifiedStatus = verifiedStatus;
    }

    public String getCompletionDoer() {
        return completionDoer;
    }

    public void setCompletionDoer(String completionDoer) {
        this.completionDoer = completionDoer;
    }

    public String getCompletionDoerUUID() {
        return completionDoerUUID;
    }

    public void setCompletionDoerUUID(String completionDoerUUID) {
        this.completionDoerUUID = completionDoerUUID;
    }
}
