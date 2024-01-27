package dev.lesechko.proselyte.repository.hibernate;

import dev.lesechko.proselyte.model.Event;
import dev.lesechko.proselyte.repository.EventRepository;

import java.util.List;

public class HibernateEventRepositoryImpl implements EventRepository {
    @Override
    public List<Event> getAll() {
        return null;
    }

    @Override
    public Event getById(Integer integer) {
        return null;
    }

    @Override
    public Event save(Event event) {
        return null;
    }

    @Override
    public Event update(Event event) {
        return null;
    }

    @Override
    public boolean deleteById(Integer integer) {
        return false;
    }
}
