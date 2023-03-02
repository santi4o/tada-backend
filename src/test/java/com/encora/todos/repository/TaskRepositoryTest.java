package com.encora.todos.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

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
        Task task = new Task("breakable toy", 2, now, null, false);
        Task task2 = new Task("keep learning", 1, now, now, false);
        dao.save(task);
        dao.save(task2);
        tasks = dao.findAll();
        assertTrue(tasks.size() >= 2); // because the order of execution of tests is "deterministic but intentionally non obvious"
    }

    @Test
    public void update() {
        Date now = new Date();
        Task task = new Task("talk about Angular", 1, now, null, false);
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
        Task task = new Task("write about Angular", 1, now, null, false);
        dao.save(task);
        System.out.println("collection size: " + dao.count());
        assertTrue(dao.count() >= 1); // because the order of execution of tests is "deterministic but intentionally non obvious"
    }

    @Test
    public void delete() {
        Date now = new Date();
        Task task = new Task("ask for feedback", 1, now, null, false);
        dao.save(task);
        long size = dao.count();
        Task retrieved = dao.findById(1).get();
        dao.delete(retrieved);
        assertEquals(size-1, dao.count());
        assertTrue(dao.findById(1).isEmpty());
    }

    @Test
    public void pagination() {
        Date now = new Date();
        Calendar c = Calendar.getInstance(); 
        c.setTime(now); 
        c.add(Calendar.DATE, 1);
  
        Task task1 = new Task("a", 2, c.getTime(), null, false);
        Task task2 = new Task("c", 1, c.getTime(), null, true);
        c.add(Calendar.DATE, 1);
        Task task3 = new Task("e", 1, c.getTime(), null, true);
        c.add(Calendar.DATE, 1);
        Task task4 = new Task("j", 0, c.getTime(), null, true);
        Task task5 = new Task("h", 2, now, null, true);
        Task task6 = new Task("k", 1, now, null, true);
        Task task7 = new Task("d", 0, now, null, true);
        Task task8 = new Task("o", 1, now, null, true);
        Task task9 = new Task("g", 2, now, null, true);
        Task task10 = new Task("m", 2, now, null, false);
        Task task11 = new Task("b", 0, now, null, false);
        Task task12 = new Task("i", 0, now, null, false);
        Task task13 = new Task("n", 1, now, null, false);
        Task task14 = new Task("l", 2, now, null, false);
        Task task15 = new Task("f", 1, now, null, false);


        dao.save(task1);
        dao.save(task2);
        dao.save(task3);
        dao.save(task4);
        dao.save(task5);
        dao.save(task6);
        dao.save(task7);
        dao.save(task8);
        dao.save(task9);
        dao.save(task10);
        dao.save(task11);
        dao.save(task12);
        dao.save(task13);
        dao.save(task14);
        dao.save(task15);

        Task r = dao.findById(1).get();
        r.setDone(true);
        dao.save(r);

        List<Order> orders = new ArrayList<Order>();
        orders.add(new Order(Sort.Direction.ASC, "done"));
        orders.add(new Order(Sort.Direction.ASC, "creationDate"));
        orders.add(new Order(Sort.Direction.DESC, "text"));

        PageRequest pr = PageRequest.of(0, 20, Sort.by(orders));
        // System.out.println("sort: " + pr.getSort().get().toList());
        Page<Task> tasks = dao.findAll(pr);
        // System.out.println(tasks.getSort());
        tasks.forEach(task -> {
            System.out.println(task);
        });
        System.out.println("current page: " + tasks.getNumber());
        System.out.println("number of elements in current page: " + tasks.getNumberOfElements());
        System.out.println("number of elements in a full page: " + tasks.getSize());
        System.out.println("total elements in all pages: " + tasks.getTotalElements());
        System.out.println("total pages: " + tasks.getTotalPages());
        System.out.println("are there pages in current page: " + tasks.hasContent());
        System.out.println("is there next page: " + tasks.hasNext());
        System.out.println("is there previous page: " + tasks.hasPrevious());
        System.out.println("is current page empty: " + tasks.isEmpty());
        System.out.println("is current page the first one: " + tasks.isFirst());
        System.out.println("is current page the last one: " + tasks.isLast());
    }
}
