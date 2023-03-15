package com.encora.todos.controllers;

import java.net.URI;
import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.encora.todos.entities.Task;
import com.encora.todos.json.TaskPageRequest;
import com.encora.todos.services.TaskService;

@RestController
@RequestMapping("/todos")
public class TaskController {
    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    // I could not make GET requests with a body from react :c
    // I had even declared my TaskPageRequest record for that D:
    // it would have been nice to send a body instead of many queryParams
    @CrossOrigin
    @GetMapping("v1")
    public Page<Task> findPage0(@RequestBody TaskPageRequest request) {
        return service.getTasks(request);
    }

    @CrossOrigin
    @GetMapping()
    public Page<Task> findPage(@RequestParam String pageNumber, @RequestParam String pageSize, @RequestParam(required = false) ArrayList<String> sorting) {
        return service.getTasks(pageNumber, pageSize, sorting);
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
