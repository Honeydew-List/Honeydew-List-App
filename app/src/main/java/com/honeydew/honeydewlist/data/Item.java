package com.honeydew.honeydewlist.data;

abstract public class Item {
    protected String label = "";
    protected String description = "";
    protected boolean completed = false;
    protected Item(String label, String description, boolean completed) {
        this.label = label;
        this.description = description;
        this.completed = completed;
    }


    /**
     * setLabel
     * @param label the name of task
     * @return success Return a boolean value indicating if it was successful
     */
    protected boolean setLabel(String label) {
        // Could check name against blacklisted words
        // Then return false if there is
        this.label = label;
        return true;
    }

    /**
     * @return label
     */
    protected String getLabel() {
        return this.label;
    }
}
