package com.encora.todos.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TaskTest {
    @Test
    public void createTask() {
        Task task = new Task("Todo test");
        assertEquals(task.id(), null);
        assertEquals(task.name(), "Todo test");
    }
}
