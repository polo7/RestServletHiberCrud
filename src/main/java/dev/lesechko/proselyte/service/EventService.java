package dev.lesechko.proselyte.service;

import java.util.List;

import dev.lesechko.proselyte.model.Event;
import dev.lesechko.proselyte.repository.EventRepository;
import dev.lesechko.proselyte.repository.hibernate.HibernateEventRepositoryImpl;


public class EventService {
    private EventRepository eventRepository = new HibernateEventRepositoryImpl();

    public List<Event> getAll() {
        return eventRepository.getAll();
    }

    public Event getById(Integer id) {
        return eventRepository.getById(id);
    }

    public Event save(Event event) {
        return eventRepository.save(event);
    }

    public boolean deleteById(Integer id) {
        return eventRepository.deleteById(id);
    }
}
