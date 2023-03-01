package com.encora.todos.repository;

import java.util.ArrayList;
import java.util.Date;
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
        if (task.getId() == null) {
            return saveNew(task);
        } else {
            try {
                return update(task);
            } catch (Exception e) {
                return null;
            }
        }
    }

    private Task saveNew(Task task) {
        if (tasks.isEmpty()) {
            task.setId(1);
        } else {
            task.setId(tasks.get(tasks.size() - 1).getId() + 1);
        }
        tasks.add(task);
        return task;
    }

    private Task update(Task task) throws Exception {
        Task toUpdate = tasks.stream().filter(t -> task.getId().equals(t.getId())).findFirst().orElse(null);
        if (toUpdate == null) {
            throw new Exception("task with id: " + task.getId() + " was not found");
        }
        toUpdate.setText(task.getText());
        toUpdate.setPriority(task.getPriority());
        toUpdate.setDueDate(task.getDueDate());
        toUpdate.setDone(task.getDone());
        if (task.getDone()) {
            Date now = new Date();
            toUpdate.setDoneDate(now);
        }
        return toUpdate;
    }

    @Override
    public List<Task> findAll() {
        return tasks;
    }

    @Override
    public Page<Task> findAll(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Optional<Task> findById(Integer id) {
        return Optional.ofNullable(tasks.stream().filter(task -> id.equals(task.getId())).findFirst().orElse(null));
    }

    @Override
    public long count() {
        return tasks.size();
    }

    @Override
    public void delete(Task task) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
    
}
