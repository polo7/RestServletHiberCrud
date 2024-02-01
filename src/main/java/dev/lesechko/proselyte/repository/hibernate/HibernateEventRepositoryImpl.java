package dev.lesechko.proselyte.repository.hibernate;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import dev.lesechko.proselyte.model.Event;
import dev.lesechko.proselyte.model.Status;
import dev.lesechko.proselyte.model.User;
import dev.lesechko.proselyte.repository.EventRepository;
import dev.lesechko.proselyte.utils.HibernateConnectionUtils;


public class HibernateEventRepositoryImpl implements EventRepository {
    private void rollbackTransaction(Transaction t) {
        if (t != null) {
            t.rollback();
            System.err.println("Hibernate: Rolling back transaction");
        }
    }

    private boolean hasValidValues(Event event) {
        if (event.getUser() == null
            || event.getFile() == null
            || event.getStatus() == null
        ) {
            return false;
        }
        return true;
    }

    @Override
    public List<Event> getAll() {
        try (Session session = HibernateConnectionUtils.getNewSession()) {
            return session.createQuery("FROM Event e LEFT JOIN FETCH e.user", Event.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Event getById(Integer id) {
        try (Session session = HibernateConnectionUtils.getNewSession()) {
            return session.createQuery("FROM Event e LEFT JOIN FETCH e.user WHERE e.id = :id", Event.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Event save(Event event) {
        if (event == null) {
            return null;
        }
        Transaction transaction = null;
        try (Session session = HibernateConnectionUtils.getNewSession()) {
            if (!hasValidValues(event)) {
                throw new Exception("Incorrect field values");
            }
            transaction = session.beginTransaction();
            session.persist(event);
            transaction.commit();
            return event;
        } catch (Exception e) {
            rollbackTransaction(transaction);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Event update(Event event) {
        if (event == null) {
            return null;
        }
        Transaction transaction = null;
        try (Session session = HibernateConnectionUtils.getNewSession()) {
            if (session.get(User.class, event.getId()) == null) {
                throw new Exception("ID is not found. Nothing to update.");
            }
            if (!hasValidValues(event)) {
                throw new Exception("Incorrect field values");
            }
            transaction = session.beginTransaction();
            session.merge(event);
            transaction.commit();
            return event;
        } catch (Exception e) {
            rollbackTransaction(transaction);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        Transaction transaction = null;
        try (Session session = HibernateConnectionUtils.getNewSession()) {
            Event event = session.get(Event.class, id);
            if (event == null) {
                throw new Exception("ID is not found. Nothing to delete");
            }
            event.setStatus(Status.DELETED);
            transaction = session.beginTransaction();
            session.merge(event);
            transaction.commit();
            return true;
        } catch (Exception e) {
            rollbackTransaction(transaction);
            e.printStackTrace();
            return false;
        }
    }
}
