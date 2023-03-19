package com.encora.todos.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.encora.todos.entities.Statistic;

@Component
public class InMemoryStatisticRepositoryImp implements StatisticRepository {

    private List<Statistic> statistics = new ArrayList<Statistic>();

    @Override
    public Statistic save(Statistic statistic) {
        if (statistic.getId() == null) {
            return saveNew(statistic);
        } else {
            try {
                return update(statistic);
            } catch (Exception e) {
                return null;
            }
        }
    }

    private Statistic saveNew(Statistic statistic) {
        if (statistics.isEmpty()) {
            statistic.setId(1);
        } else {
            statistic.setId(statistics.get(0).getId() + 1);
        }
        statistics.add(0, statistic);
        return statistic;
    }

    private Statistic update(Statistic statistic) throws Exception {
        Statistic toUpdate = statistics.stream().filter(s -> statistic.getId().equals(s.getId())).findFirst()
                .orElse(null);
        if (toUpdate == null) {
            throw new Exception("statistic with id: " + statistic.getId() + " was not found");
        }
        toUpdate.setName(statistic.getName());
        toUpdate.setDescription(statistic.getDescription());
        toUpdate.setValue(statistic.getValue());
        return toUpdate;
    }

    @Override
    public List<Statistic> findAll() {
        return statistics;
    }

    @Override
    public Optional<Statistic> findByName(String name) {
        return Optional.ofNullable(statistics.stream().filter(s -> name.equals(s.getName())).findFirst().orElse(null));
    }

    @Override
    public void delete(Statistic statistic) {
        statistics.remove(statistic);
    }

}
