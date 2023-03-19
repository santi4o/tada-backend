package com.encora.todos.repository;

import java.util.List;
import java.util.Optional;

import com.encora.todos.entities.Statistic;

// can extend JpaRepository when implementing DB
public interface StatisticRepository {
    Statistic save(Statistic statistic); // delete when extending JpaRepository
    List<Statistic> findAll(); // delete when extending JpaRepository
    Optional<Statistic> findByName(String name); // delete when extending JpaRepository
    void delete(Statistic statistic); // delete when extending JpaRepository
}
