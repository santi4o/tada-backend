package com.encora.todos.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public Page<Task> getTasks(TaskPageRequest taskPageRequest) {
        List<Sort.Order> orders = new ArrayList<Sort.Order>();

        taskPageRequest.sort().forEach((order) -> {
            orders.add(new Sort.Order(directions.get(order.direction()), order.property()));
        });

        PageRequest pr = PageRequest.of(taskPageRequest.number(), taskPageRequest.size(), Sort.by(orders));
        // System.out.println("page request: " + pr);
        return repository.findAll(pr);
    }

    public Page<Task> getTasks(String pageNumber, String pageSize, ArrayList<String> sorting, String name,
            String priority, String done) {
        List<Sort.Order> orders = new ArrayList<Sort.Order>();

        if (sorting != null) {
            sorting.forEach((order) -> {
                try {
                    String[] sortingBy = order.split("-");
                    orders.add(new Sort.Order(directions.get(sortingBy[1]), sortingBy[0]));
                } catch (Exception e) {
                    System.out.println(e);
                }
            });
        }

        PageRequest pr = PageRequest.of(Integer.parseInt(pageNumber), Integer.parseInt(pageSize), Sort.by(orders));

        return repository.findByNameAndPriorityAndStatus(name, priority, done, pr);
    }

    public Optional<Task> findTaskById(Integer id) {
        return repository.findById(id);
    }

    public Optional<Task> updateTask(Integer id, Task task) {
        Optional<Task> toFind = repository.findById(id);

        if (!toFind.isPresent()) {
            return toFind;
        }

        Task toUpdate = toFind.get();
        toUpdate.setText(task.getText());
        toUpdate.setPriority(task.getPriority());
        toUpdate.setDueDate(task.getDueDate());

        return Optional.of(repository.save(toUpdate));
    }

    public Optional<Task> markTaskAsDone(Integer id) {
        Optional<Task> toFind = repository.findById(id);

        if (!toFind.isPresent()) {
            return toFind;
        }

        Task toUpdate = toFind.get();
        if (!toUpdate.getDone()) {
            toUpdate.setDoneDate(new Date());
            toUpdate.setDone(true);
        }

        return Optional.of(repository.save(toUpdate));
    }

    public Optional<Task> markTaskAsPending(Integer id) {
        Optional<Task> toFind = repository.findById(id);

        if (!toFind.isPresent()) {
            return toFind;
        }

        Task toUpdate = toFind.get();
        toUpdate.setDone(false);
        toUpdate.setDoneDate(null);

        return Optional.of(repository.save(toUpdate));
    }
}
