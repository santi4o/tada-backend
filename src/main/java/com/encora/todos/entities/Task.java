package com.encora.todos.entities;

public record Task(Integer id, String name) {
    public Task(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Task(String name) {
        this(null, name);
    }
}
