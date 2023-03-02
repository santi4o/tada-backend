package com.encora.todos.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.encora.todos.entities.Task;
import com.encora.todos.json.TaskPageRequest;
import com.encora.todos.repository.InMemoryTaskRepositoryImp;

@Service
public class TaskService {
    private final InMemoryTaskRepositoryImp repository;
    Map<String, Sort.Direction> directions = new HashMap<String, Sort.Direction>();


    @Autowired
    public TaskService(InMemoryTaskRepositoryImp repository) {
        this.repository = repository;
        directions.put("ASC", Sort.Direction.ASC);
        directions.put("DESC", Sort.Direction.DESC);
    }

    public Task saveTask(Task task) {
        return repository.save(task);
    }

    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    public Page<Task> getAllTasks(TaskPageRequest taskPageRequest) {
        List<Sort.Order> orders = new ArrayList<Sort.Order>();

        taskPageRequest.sort().forEach((order) -> {
            orders.add(new Sort.Order(directions.get(order.direction()), order.property()));
        });

        PageRequest pr = PageRequest.of(taskPageRequest.number(), taskPageRequest.size(), Sort.by(orders));
        // System.out.println("page request: " + pr);
        return repository.findAll(pr);
    }

    public Optional<Task> findTaskById(Integer id) {
        return repository.findById(id);
    }
}
