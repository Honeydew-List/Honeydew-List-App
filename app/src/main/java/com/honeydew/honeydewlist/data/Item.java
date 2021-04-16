package com.honeydew.honeydewlist.data;

abstract public class Item {
    private String name = "";
    private String owner = "";
    private String description = "";
    protected Item(String name, String description) {
        this.name = name;
        this.description = description;
    }


    /**
     * setLabel
     * @param name the name of task
     * @return success Return a boolean value indicating if it was successful
     */
    protected Boolean setLabel(String name) {
        // Could check name against blacklisted words
        // Then return false if there is
        this.name = name;
        return true;
    }

    /**
     * getLabel
     * @return name
     */
    protected String getLabel() {
        return this.name;
    }

    /**
     * getDescription
     * @return description
     */
    protected String getDescription() { return this.description; }

    protected String getOwner() {
        return owner;
    }

    protected void setOwner(String owner) {
        this.owner = owner;
    }
}
