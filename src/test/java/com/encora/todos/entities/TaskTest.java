package com.encora.todos.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TaskTest {
    @Test
    public void createTasks() {
        Date now = new Date();

        Task task = new Task("learn react", 2, now, null, false);
        assertNull(task.getId());
        assertEquals(task.getText(), "learn react");
        assertEquals(2, task.getPriority());
        assertEquals(now, task.getCreationDate());
        assertEquals(false, task.getDone());
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
