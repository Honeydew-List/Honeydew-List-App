package com.honeydew.honeydewlist.data;

public class Owner {
    private String owner;
    private String uuid;

    protected Owner(String owner, String uuid) {
        this.owner = owner;
        this.uuid = uuid;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getUUID() {
        return uuid;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }
}
