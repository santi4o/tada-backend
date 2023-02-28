package com.encora.todos.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;

import org.junit.jupiter.api.Test;

public class TaskTest {
    @Test
    public void createTasks() {
        Date now = new Date();
        Task task = new Task("learn react", 2, now, null, false);
        assertEquals(task.getId(), 1);
        assertEquals(task.getText(), "learn react");
        Task task2 = new Task("keep learning", 0, now, null, false);
        assertEquals(task2.getId(), 2);
        assertEquals(task2.getText(), "keep learning");
    }

    @Test
    public void invalidPriority() {
        Date now = new Date();
        assertThrows(IllegalArgumentException.class, () -> {
            new Task("learn react", 3, now, null, false);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Task("learn react", -1, now, null, false);
        });
    }
}
