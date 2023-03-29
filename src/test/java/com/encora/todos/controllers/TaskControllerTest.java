package com.encora.todos.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.encora.todos.entities.Task;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskControllerTest {

    @Autowired
    private TestRestTemplate template;

    @Test
    public void getPage() {
        ResponseEntity<Object> entity = template.getForEntity(
                "/todos?pageNumber=0&pageSize=10",
                Object.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, entity.getHeaders().getContentType());
    }

    @Test
    public void saveTask() {
        Date date = new Date();
        Task newTask = new Task("test task 1", 1, null, date, false);

        ResponseEntity<Task> entity = template.postForEntity(
                "/todos", newTask,
                Task.class);
        assertEquals(HttpStatus.CREATED, entity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, entity.getHeaders().getContentType());

        Task response = entity.getBody();
        if (response != null) {
            assertEquals("test task 1", response.getText());
            assertEquals(1, response.getPriority());
            assertNotNull(response.getCreationDate());
            assertEquals(date, response.getDueDate());
            assertFalse(response.getDone());
        }
    }

    @Test
    public void updateTask() {
        Date date = new Date();
        Task newTask = new Task("test task 2", 1, null, date, false);

        ResponseEntity<Task> entity = template.postForEntity(
                "/todos", newTask,
                Task.class);
        assertEquals(HttpStatus.CREATED, entity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, entity.getHeaders().getContentType());

        Task response = entity.getBody();
        if (response != null) {
            response.setText("changed name");
            response.setPriority(2);
            response.setDueDate(null);

            template.put("/todos/" + response.getId(), response);

            ResponseEntity<Task> entity2 = template.getForEntity(
                    "/todos/" + response.getId(),
                    Task.class);

            assertEquals(HttpStatus.OK, entity2.getStatusCode());
            assertEquals(MediaType.APPLICATION_JSON, entity2.getHeaders().getContentType());
            Task response2 = entity.getBody();
            if (response2 != null) {
                assertEquals("changed name", response2.getText());
                assertEquals(2, response2.getPriority());
                assertNotNull(response2.getCreationDate());
                assertEquals(null, response2.getDueDate());
                assertFalse(response2.getDone());
            }
        }
    }

    @Test
    public void deleteTask() {
        Date date = new Date();
        Task newTask = new Task("test task 3", 1, null, date, false);

        ResponseEntity<Task> entity = template.postForEntity(
                "/todos", newTask,
                Task.class);
        assertEquals(HttpStatus.CREATED, entity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, entity.getHeaders().getContentType());

        Task response = entity.getBody();
        if (response != null) {

            template.delete("/todos/" + response.getId());

            ResponseEntity<Task> entity2 = template.getForEntity(
                    "/todos/" + response.getId(),
                    Task.class);

            assertEquals(HttpStatus.NOT_FOUND, entity2.getStatusCode());

        }
    }
}
