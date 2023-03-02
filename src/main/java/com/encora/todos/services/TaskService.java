package com.encora.todos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.encora.todos.entities.Task;
import com.encora.todos.repository.InMemoryTaskRepositoryImp;

@Service
public class TaskService {
    private final InMemoryTaskRepositoryImp repository;

    @Autowired
    public TaskService(InMemoryTaskRepositoryImp repository) {
        this.repository = repository;
    }

    public Task saveTask(Task task) {
        return repository.save(task);
    }

    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    public Optional<Task> findTaskById(Integer id) {
        return repository.findById(id);
    }
}
