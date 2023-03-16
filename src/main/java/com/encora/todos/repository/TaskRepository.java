package com.encora.todos.repository;

import java.util.List;
import java.util.Optional;

import com.encora.todos.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


// can extend JpaRepository when implementing DB
public interface TaskRepository {
    Task save(Task task); // Delete when extending JpaRepository
    List<Task> findAll(); // Delete when extending JpaRepository
    Page<Task> findAll(Pageable pageable); // Delete when extending JpaRepository
    Optional<Task> findById(Integer id); // Delete when extending JpaRepository
    long count(); // Delete when extending JpaRepository
    void delete(Task task); // Delete when extending JpaRepository

    // I think this one would anyway need a custom implementation (even when extending JpaRepository)
    // because it seems there is no findBy... method that returns a Page<T>, and takes a list of
    // the mentioned parammeters in addition to the Pageable
    Page<Task> findByNameAndPriorityAndStatus(String name, String priority, String status, Pageable pageable);
}
