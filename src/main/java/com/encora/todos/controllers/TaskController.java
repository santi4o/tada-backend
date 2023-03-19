package com.encora.todos.controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.encora.todos.entities.Task;
import com.encora.todos.json.TaskPageCustomResponse;
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
    public TaskPageCustomResponse findPage(@RequestParam String pageNumber, @RequestParam String pageSize,
            @RequestParam(required = false) ArrayList<String> sorting, @RequestParam(required = false) String name,
            @RequestParam(required = false) String priority, @RequestParam(required = false) String done) {
        return service.getTasks(pageNumber, pageSize, sorting, name, priority, done);
    }

    @GetMapping("{id}")
    public ResponseEntity<Task> findById(@PathVariable Integer id) {
        return ResponseEntity.of(service.findTaskById(id));
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity<Task> save(@RequestBody Task task) {
        Task savedTask = service.saveTask(task);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(savedTask.getId())
                .toUri();
        return ResponseEntity.created(uri).body(task);
    }

    @CrossOrigin
    @PutMapping("{id}")
    public ResponseEntity<Task> update(@PathVariable Integer id, @RequestBody Task task) {
        Optional<Task> updatedTask = service.updateTask(id, task);
        if (!updatedTask.isPresent()) {
            return ResponseEntity.ofNullable(null);
        }
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(updatedTask.get().getId())
                .toUri();
        return ResponseEntity.created(uri).body(updatedTask.get());
    }

    @CrossOrigin
    @PatchMapping("{id}/done")
    public ResponseEntity<Task> markAsDone(@PathVariable Integer id) {
        Optional<Task> updatedTask = service.markTaskAsDone(id);
        if (!updatedTask.isPresent()) {
            return ResponseEntity.ofNullable(null);
        }
        return ResponseEntity.ok().body(updatedTask.get());
    }
    
    @CrossOrigin
    @PatchMapping("{id}/undone")
    public ResponseEntity<Task> markAsPending(@PathVariable Integer id) {
        Optional<Task> updatedTask = service.markTaskAsPending(id);
        if (!updatedTask.isPresent()) {
            return ResponseEntity.ofNullable(null);
        }
        return ResponseEntity.ok().body(updatedTask.get());
    }

    @CrossOrigin
    @DeleteMapping("{id}")
    public ResponseEntity<Integer> delete(@PathVariable Integer id) {
        Boolean deleted = service.delete(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(id);
    }
}
