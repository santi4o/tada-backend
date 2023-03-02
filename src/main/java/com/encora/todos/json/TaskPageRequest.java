package com.encora.todos.json;

import java.util.ArrayList;

public record TaskPageRequest(Integer number, Integer size, ArrayList<Order> sort) {}
