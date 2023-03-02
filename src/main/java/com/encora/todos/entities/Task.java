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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((text == null) ? 0 : text.hashCode());
        result = prime * result + ((priority == null) ? 0 : priority.hashCode());
        result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
        result = prime * result + ((dueDate == null) ? 0 : dueDate.hashCode());
        result = prime * result + ((done == null) ? 0 : done.hashCode());
        result = prime * result + ((doneDate == null) ? 0 : doneDate.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Task other = (Task) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (text == null) {
            if (other.text != null)
                return false;
        } else if (!text.equals(other.text))
            return false;
        if (priority == null) {
            if (other.priority != null)
                return false;
        } else if (!priority.equals(other.priority))
            return false;
        if (creationDate == null) {
            if (other.creationDate != null)
                return false;
        } else if (!creationDate.equals(other.creationDate))
            return false;
        if (dueDate == null) {
            if (other.dueDate != null)
                return false;
        } else if (!dueDate.equals(other.dueDate))
            return false;
        if (done == null) {
            if (other.done != null)
                return false;
        } else if (!done.equals(other.done))
            return false;
        if (doneDate == null) {
            if (other.doneDate != null)
                return false;
        } else if (!doneDate.equals(other.doneDate))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Task [id=" + id + ", text=" + text + ", priority=" + priority + ", creationDate=" + creationDate
                + ", dueDate=" + dueDate + ", done=" + done + ", doneDate=" + doneDate + "]";
    }

    

}
