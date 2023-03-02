package com.encora.todos.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.encora.todos.entities.Task;
import com.encora.todos.json.TaskPageRequest;
import com.encora.todos.services.TaskService;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public List<Task> findAll() {
        return service.getAllTasks();
    }

    @GetMapping("pages")
    public Page<Task> findPage(@RequestBody TaskPageRequest request) {
        return service.getAllTasks(request);
    }

    @GetMapping("{id}")
    public ResponseEntity<Task> findById(@PathVariable Integer id) {
        return ResponseEntity.of(service.findTaskById(id));
    }

    @PostMapping
    public ResponseEntity<Task> save(@RequestBody Task task) {
        Task savedTask = service.saveTask(task);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(savedTask.getId())
                .toUri();
        return ResponseEntity.created(uri).body(task);
    }
}
