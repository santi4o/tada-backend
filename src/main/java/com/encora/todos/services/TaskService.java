package com.encora.todos.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.encora.todos.entities.Statistic;
import com.encora.todos.entities.Task;
import com.encora.todos.json.TaskPageCustomResponse;
import com.encora.todos.json.TaskPageRequest;
import com.encora.todos.repository.InMemoryStatisticRepositoryImp;
import com.encora.todos.repository.InMemoryTaskRepositoryImp;

@Service
public class TaskService {
    private final InMemoryTaskRepositoryImp tasksRepository;
    private final InMemoryStatisticRepositoryImp statisticRepository;

    Map<String, Sort.Direction> directions = new HashMap<String, Sort.Direction>();

    public TaskService(
            InMemoryTaskRepositoryImp tasksrepository,
            InMemoryStatisticRepositoryImp statisticsRepository) {
        this.tasksRepository = tasksrepository;
        this.statisticRepository = statisticsRepository;
        directions.put("ASC", Sort.Direction.ASC);
        directions.put("DESC", Sort.Direction.DESC);
    }

    public Task saveTask(Task task) {
        return tasksRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return tasksRepository.findAll();
    }

    public Page<Task> getTasks(TaskPageRequest taskPageRequest) {
        List<Sort.Order> orders = new ArrayList<Sort.Order>();

        taskPageRequest.sort().forEach((order) -> {
            orders.add(new Sort.Order(directions.get(order.direction()), order.property()));
        });

        PageRequest pr = PageRequest.of(taskPageRequest.number(), taskPageRequest.size(), Sort.by(orders));
        // System.out.println("page request: " + pr);
        return tasksRepository.findAll(pr);
    }

    public TaskPageCustomResponse getTasks(String pageNumber, String pageSize, ArrayList<String> sorting, String name,
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
        Page<Task> page = tasksRepository.findByNameAndPriorityAndStatus(name, priority, done, pr);
        List<Statistic> stats = statisticRepository.findAll();

        return new TaskPageCustomResponse(page, stats);
    }

    public Optional<Task> findTaskById(Integer id) {
        return tasksRepository.findById(id);
    }

    public Optional<Task> updateTask(Integer id, Task task) {
        Optional<Task> toFind = tasksRepository.findById(id);

        if (!toFind.isPresent()) {
            return toFind;
        }

        Task toUpdate = toFind.get();
        toUpdate.setText(task.getText());
        toUpdate.setPriority(task.getPriority());
        toUpdate.setDueDate(task.getDueDate());

        Optional<Task> updated =  Optional.of(tasksRepository.save(toUpdate));
        calculateStats();

        return updated;
    }

    private void calculateStat(Integer priority, String name, String desc) {
        List<Task> filtered;
        long acum = 0;

        if (priority == null) {
            filtered = tasksRepository.findByDone(true);
        } else {
            filtered = tasksRepository.findByDoneAndPriority(true, priority);
        }

        for (Task task : filtered) {
            long difMS = task.getDoneDate().getTime() - task.getCreationDate().getTime();
            acum += TimeUnit.SECONDS.convert(difMS, TimeUnit.MILLISECONDS);
        }

        Optional<Statistic> toFind = statisticRepository.findByName(name);

        if (toFind.isPresent()) {
            Statistic stat = toFind.get();
            stat.setValue(filtered.isEmpty() ? -1 : acum / filtered.size());
            statisticRepository.save(stat);
        } else {
            statisticRepository.save(new Statistic(name, desc, filtered.isEmpty() ? -1 : acum / filtered.size()));
        }

    }

    private void calculateStats() {
        calculateStat(null, "avg general", "Average time to finish a to-do in seconds");
        calculateStat(0, "avg low", "Average time to finish a low priority to-do in seconds");
        calculateStat(1, "avg med", "Average time to finish a medium priority to-do in seconds");      
        calculateStat(2, "avg high", "Average time to finish a high priority to-do in seconds");  
    }

    public Optional<Task> markTaskAsDone(Integer id) {
        Optional<Task> toFind = tasksRepository.findById(id);

        if (!toFind.isPresent()) {
            return toFind;
        }

        Task toUpdate = toFind.get();
        if (!toUpdate.getDone()) {
            toUpdate.setDoneDate(new Date());
            toUpdate.setDone(true);
        }

        calculateStats();

        return Optional.of(tasksRepository.save(toUpdate));
    }

    public Optional<Task> markTaskAsPending(Integer id) {
        Optional<Task> toFind = tasksRepository.findById(id);

        if (!toFind.isPresent()) {
            return toFind;
        }

        Task toUpdate = toFind.get();
        toUpdate.setDone(false);
        toUpdate.setDoneDate(null);

        calculateStats();

        return Optional.of(tasksRepository.save(toUpdate));
    }

    public boolean delete(Integer id) {
        Optional<Task> toDelete = tasksRepository.findById(id);

        if (!toDelete.isPresent()) {
            return false;
        }

        tasksRepository.delete(toDelete.get());
        calculateStats();

        return true;
    }
}
