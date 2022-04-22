package com.panacoding.gridtimeline;

public class JModelEvent {
    String id = "";
    String name;
    String y_label_id;
    String start_time = "";
    String end_time = "";
    String start_date = "";
    String end_date = "";
    String background_color = "#436fe8";
    String text_color = "#ffffff";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText_color() {
        return text_color;
    }

    public void setText_color(String text_color) {
        this.text_color = text_color;
    }

    public String getBackground_color() {
        return background_color;
    }

    public void setBackground_color(String background_color) {
        this.background_color = background_color;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getY_label_id() {
        return y_label_id;
    }

    public void setY_label_id(String y_label_id) {
        this.y_label_id = y_label_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}
