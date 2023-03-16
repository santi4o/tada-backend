package com.encora.todos.repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;

import com.encora.todos.entities.Task;

@Component
public class InMemoryTaskRepositoryImp implements TaskRepository {

    private List<Task> tasks = new ArrayList<Task>();
    Map<String, Function<? super Task,? extends String>> keyExtractors = new HashMap<String, Function<? super Task,? extends String>>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");

    public InMemoryTaskRepositoryImp() {
        keyExtractors.put("text", task -> task.getText() );
        keyExtractors.put("priority", task -> task.getPriority().toString());
        keyExtractors.put("creationDate", task -> dateFormat.format(task.getCreationDate()));
        keyExtractors.put("dueDate", task -> task.getDueDate() != null ? dateFormat.format(task.getDueDate()) : "");
        keyExtractors.put("done", task -> task.getDone().toString());
    }

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

    private List<Task> getSortedList(List<Order> sortOrders) {
        Comparator<Task> comparator;
            
        // I tried to use a hashmap (and avoid if else) for inserting Comparator.naturalOrder() or Comparator.reverseOrder() depending on the
        // value of sortOrders.get(index).getDirection, but I had issues with the types for the hashmap, tried without success
        if (sortOrders.get(0).getDirection() == Sort.Direction.ASC) {
            comparator = Comparator.comparing(keyExtractors.get(sortOrders.get(0).getProperty()), Comparator.naturalOrder());
        } else {
            comparator = Comparator.comparing(keyExtractors.get(sortOrders.get(0).getProperty()), Comparator.reverseOrder());
        }
        sortOrders.remove(0);

        while (!sortOrders.isEmpty()) {
            if (sortOrders.get(0).getDirection() == Sort.Direction.ASC) {
                comparator = comparator.thenComparing(keyExtractors.get(sortOrders.get(0).getProperty()), Comparator.naturalOrder());
            } else {
                comparator = comparator.thenComparing(keyExtractors.get(sortOrders.get(0).getProperty()), Comparator.reverseOrder());
            }
            sortOrders.remove(0);
        }

        Stream<Task> taskStream = tasks.stream().sorted(comparator);

        return taskStream.collect(Collectors.toList());
    }

    private List<List<Task>> genPages(List<Task> tasks, Pageable pageable) {
        List<List<Task>> pages = new ArrayList<List<Task>>();
        for (int i = 0; i < tasks.size(); i += pageable.getPageSize()) {
            pages.add(tasks.subList(i, Math.min(i + pageable.getPageSize(), tasks.size())));
        }
        
        return pages;
    }

    @Override
    public Page<Task> findAll(Pageable pageable) {
        List<Order> sortOrders = new ArrayList<Order>(pageable.getSort().toList());
        List<Task> tasksList;

        if (sortOrders.size() > 0) {
            tasksList = getSortedList(sortOrders);       
        } else {
            tasksList = tasks;
        }    
        
        List<List<Task>> pages = genPages(tasksList, pageable);
        Page<Task> page;

        try {
            page = new PageImpl<>(pages.get(pageable.getPageNumber()), pageable, tasksList.size());
        } catch (IndexOutOfBoundsException e) {
            page = new PageImpl<>(new ArrayList<>(), pageable, tasksList.size());
        }

        return page;
    }

    @Override
    public Page<Task> findByNameAndPriorityAndStatus(String name, String priority, String done, Pageable pageable) {
        List<Order> sortOrders = new ArrayList<Order>(pageable.getSort().toList());
        List<Task> tasksList;

        if (sortOrders.size() > 0) {
            tasksList = getSortedList(sortOrders);       
        } else {
            tasksList = tasks;
        }
        
        if (name != null) {
            tasksList = tasksList.stream().filter(t -> t.getText().toLowerCase().contains(name.toLowerCase())).toList();
        }
        if (priority != null) {
            tasksList = tasksList.stream().filter(t -> t.getPriority() == Integer.parseInt(priority)).toList();
        }
        if (done != null) {
            tasksList = tasksList.stream().filter(t -> t.getDone() == Boolean.parseBoolean(done)).toList();
        }
        
        List<List<Task>> pages = genPages(tasksList, pageable);
        Page<Task> page;

        try {
            page = new PageImpl<>(pages.get(pageable.getPageNumber()), pageable, tasksList.size());
        } catch (IndexOutOfBoundsException e) {
            page = new PageImpl<>(new ArrayList<>(), pageable, tasksList.size());
        }

        return page;
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
        tasks.remove(task);
    }

}
