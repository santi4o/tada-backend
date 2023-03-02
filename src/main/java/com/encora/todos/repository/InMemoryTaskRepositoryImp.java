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
        keyExtractors.put("dueDate", task -> task.getDueDate().toString());
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

    @Override
    public Page<Task> findAll(Pageable pageable) {
        System.out.println("elements per page: " + pageable.getPageSize());
        System.out.println(Comparator.naturalOrder().getClass());
        List<Order> sortOrders = new ArrayList<Order>(pageable.getSort().toList());
        System.out.println("sort types:");
        sortOrders.forEach(order -> {
            System.out.println(order.getProperty() + " " + order.getDirection() + " " + order.getDirection().toString().getClass());
        });

        Comparator<Task> comparator;
        if (sortOrders.size() > 0) {
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
            List<Task> sortedTasks = taskStream.collect(Collectors.toList());
            List<List<Task>> pages = new ArrayList<List<Task>>();
            for (int i = 0; i < sortedTasks.size(); i += pageable.getPageSize()) {
                pages.add(sortedTasks.subList(i, Math.min(i + pageable.getPageSize(), sortedTasks.size())));
            }
            Page<Task> page;
            try {
                page = new PageImpl<>(pages.get(pageable.getPageNumber()), pageable, tasks.size());
            } catch (IndexOutOfBoundsException e) {
                return null;
            }

            System.out.println("pageable: " + pageable);
            
            return page;
        } else {
            Page<Task> page = new PageImpl<>(tasks, pageable, tasks.size());
            return page;
        }    
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
