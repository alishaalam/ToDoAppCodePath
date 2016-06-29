package com.happytimes.alisha.model;

import java.util.Date;

/**
 * Created by alishaalam on 6/26/16.
 */
public class Task {

    int id;
    String title;
    String description;
    Date deadline;
    String priority;


    public Task() {
    }

    public Task(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public Task(String title, String priority) {
        this.title = title;
        this.priority = priority;
    }

    public Task(String title, String description, String priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Task{" +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
