package com.encora.todos.json;

import java.util.List;

import org.springframework.data.domain.Page;

import com.encora.todos.entities.Statistic;
import com.encora.todos.entities.Task;

public record TaskPageCustomResponse(Page<Task> page, List<Statistic> statistics) {}
