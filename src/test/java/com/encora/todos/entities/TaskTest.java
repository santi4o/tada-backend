package com.encora.todos.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.encora.todos.repository.InMemoryTaskRepositoryImp;

@SpringBootTest
public class TaskTest {
    @Autowired
    private InMemoryTaskRepositoryImp dao;

    @Test
    public void createTasks() {
        Date now = new Date();

        Task task = new Task("learn react", 2, now, null, false);
        assertNull(task.getId());
        task = dao.save(task);
        assertEquals(1, task.getId());
        assertEquals(task.getText(), "learn react");

        Task task2 = new Task("keep learning", 0, now, null, false);
        assertNull(task2.getId());
        task2 = dao.save(task2);
        assertEquals(2, task2.getId());
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
