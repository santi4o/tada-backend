package com.encora.todos.entities;

import java.util.Date;

public class Task {
    // private static int count = 0;
    private Integer id;
    private String text;
    private Integer priority;
    private Date creationDate;
    private Date dueDate;
    private Boolean done;
    private Date doneDate;

    public Task() {}

    public Task(String text, Integer priority, Date creationDate, Date dueDate, Boolean done) {
        this.text = text;
        setPriority(priority);
        this.creationDate = creationDate;
        this.dueDate = dueDate;
        this.done = done;
        // setId(++count);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        if (priority >= 0 && priority <= 2) {
            this.priority = priority;
        } else {
            throw new IllegalArgumentException("priority out of range (0-2)");
        }
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public Date getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(Date doneDate) {
        this.doneDate = doneDate;
    }

}
