package com.honeydew.honeydewlist.data;

abstract public class Item extends Owner {
    private String name = "";
    private String description = "";
    protected Item(String name, String description, String owner, String uuid) {
        super(owner, uuid);
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
