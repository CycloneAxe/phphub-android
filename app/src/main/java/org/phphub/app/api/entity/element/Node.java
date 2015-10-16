package org.phphub.app.api.entity.element;

import com.google.gson.annotations.SerializedName;

public class Node {

    int id;

    String name;

    @SerializedName("parent_node")
    int parentNode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentNode() {
        return parentNode;
    }

    public void setParentNode(int parentNode) {
        this.parentNode = parentNode;
    }
}
