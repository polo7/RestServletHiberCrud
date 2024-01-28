package dev.lesechko.proselyte.service;

import dev.lesechko.proselyte.model.Event;
import dev.lesechko.proselyte.repository.EventRepository;
import dev.lesechko.proselyte.repository.hibernate.HibernateEventRepositoryImpl;

import java.util.List;

public class EventService {
    private EventRepository eventRepository = new HibernateEventRepositoryImpl();

    public List<Event> getAll() {
        return eventRepository.getAll();
    }

    public Event getById(Integer id) {
        return eventRepository.getById(id);
    }

    public boolean deleteById(Integer id) {
        return eventRepository.deleteById(id);
    }
}
