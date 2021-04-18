package com.honeydew.honeydewlist.data;

abstract public class Item extends Owner {
    private String name = "";
    private String description = "";
    private String itemID = "";
    protected Item(String name, String description, String itemID, String owner, String uuid) {
        super(owner, uuid);
        this.name = name;
        this.description = description;
        this.itemID = itemID;
    }

    // Required for FireStore
    protected Item() {
        super();
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

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }
}
