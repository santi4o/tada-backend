package com.encora.todos.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.encora.todos.entities.Task;

@SpringBootTest
public class TaskRepositoryTest {
    @Autowired
    private InMemoryTaskRepositoryImp dao;

    @Test
    public void save() {
        Date now = new Date();

        Task task = new Task("learn react", 2, now, null, false);
        assertNull(task.getId());
        task = dao.save(task);
        assertNotNull(task.getId());
        assertEquals("learn react", task.getText());
        assertEquals(2, task.getPriority());
        assertEquals(now, task.getCreationDate());
        assertEquals(false, task.getDone());
    }

    @Test
    public void findByIdThatExists() {
        Date now = new Date();
        Task task = new Task("read more about spring", 2, now, null, false);
        dao.save(task);
        dao.save(task);
        Optional<Task> retreived = dao.findById(1);
        assertTrue(retreived.isPresent());
        assertEquals(1, retreived.get().getId().intValue());
    }

    @Test
    public void findByIdThatDoesNotExist() {
        Optional<Task> retreived = dao.findById(100);
        assertTrue(retreived.isEmpty());
    }

    @Test
    public void findAll() {
        Date now = new Date();
        List<Task> tasks = dao.findAll();
        assertEquals(0, tasks.size());
        Task task = new Task("learn react", 2, now, null, false);
        Task task2 = new Task("keep learning", 1, now, now, false);
        dao.save(task);
        dao.save(task2);
        tasks = dao.findAll();
        assertTrue(tasks.size() >= 2); // because the order of execution of tests is "deterministic but intentionally non obvious"
    }

    @Test
    public void update() {
        Date now = new Date();
        Task task = new Task("keep practicing Angular", 1, now, null, false);
        dao.save(task);
        Task retrieved = dao.findById(1).get();
        retrieved.setText("feed my cat");
        assertNull(retrieved.getDoneDate());
        retrieved.setDone(true);
        dao.save(retrieved);
        assertNotNull(retrieved.getDoneDate());
        Task retrieved2 = dao.findById(1).get();
        assertEquals("feed my cat", retrieved2.getText());
    }

    @Test
    public void count() {
        Date now = new Date();
        Task task = new Task("keep practicing Angular", 1, now, null, false);
        dao.save(task);
        System.out.println("collection size: " + dao.count());
        assertTrue(dao.count() >= 1); // because the order of execution of tests is "deterministic but intentionally non obvious"
    }

    
}
