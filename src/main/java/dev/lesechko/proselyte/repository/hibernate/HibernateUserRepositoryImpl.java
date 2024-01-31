package dev.lesechko.proselyte.repository.hibernate;

import dev.lesechko.proselyte.model.Status;
import dev.lesechko.proselyte.model.User;
import dev.lesechko.proselyte.repository.UserRepository;
import dev.lesechko.proselyte.utils.HibernateConnectionUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class HibernateUserRepositoryImpl implements UserRepository {
    private void rollbackTransaction(Transaction t) {
        if (t != null) {
            t.rollback();
            System.err.println("Hibernate: Rolling back transaction");
        }
    }

    private boolean hasValidValues(User user) {
        if (user.getName() == null ||  user.getName().isBlank() || user.getStatus() == null) {
            return false;
        }
        return true;
    }

    @Override
    public List<User> getAll() {
        try (Session session = HibernateConnectionUtils.getNewSession()) {
            return session.createQuery("FROM User", User.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User getById(Integer id) {
        try (Session session = HibernateConnectionUtils.getNewSession()) {
            return session.createQuery("FROM User u LEFT JOIN FETCH u.events WHERE u.id = :id", User.class)
                    .setParameter("id", id)
                    .getSingleResult();
            //return session.get(User.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User save(User user) {
        if (user == null) {
            return null;
        }
        Transaction transaction = null;
        try (Session session = HibernateConnectionUtils.getNewSession()) {
            if (!hasValidValues(user)) {
                throw new Exception("Incorrect field values");
            }
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            return user;
        } catch (Exception e) {
            rollbackTransaction(transaction);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User update(User user) {
        if (user == null) {
            return null;
        }
        Transaction transaction = null;
        try (Session session = HibernateConnectionUtils.getNewSession()) {
            if (session.get(User.class, user.getId()) == null) {
                throw new Exception("ID is not found. Nothing to update.");
            }
            if (!hasValidValues(user)) {
                throw new Exception("Incorrect field values");
            }
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
            return user;
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
            User user = session.get(User.class, id);
            if (user == null) {
                throw new Exception("ID is not found.Nothing to delete.");
            }
            user.setStatus(Status.DELETED);
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
            return true;
        } catch (Exception e) {
            rollbackTransaction(transaction);
            e.printStackTrace();
            return false;
        }
    }
}
