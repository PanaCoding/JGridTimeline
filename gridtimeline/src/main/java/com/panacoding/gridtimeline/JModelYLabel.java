package com.panacoding.gridtimeline;

public class JModelYLabel {
    String name;
    String id;

    public JModelYLabel(String id,String name){
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
