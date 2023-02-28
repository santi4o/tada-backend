package com.encora.todos.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.encora.todos.entities.Task;

@Component
public class InMemoryTaskRepositoryImp implements TaskRepository {

    private List<Task> tasks = new ArrayList<Task>();

    @Override
    public Task save(Task task) {
        if (tasks.isEmpty()) {
            task.setId(1);
            System.out.println("is empty");
        } else {
            System.out.println("not empty?");
            task.setId(tasks.get(tasks.size() - 1).getId() + 1);
        }
        tasks.add(task);
        return task;
    }

    @Override
    public List<Task> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Page<Task> findAll(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Optional<Task> findById() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public long count() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public void delete(Task task) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
    
}
