package com.honeydew.honeydewlist.data;

import java.util.List;

public class Reward extends Item {
    private Long cost;
    private Long quantity; // TODO: Add quantity restriction, for now set quantity to 1
    private Boolean completed;
    protected Reward(String name, String description, String owner, String uuid) {
        super(name, description, owner, uuid);
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
