package dev.lesechko.proselyte.utils;

import org.hibernate.cfg.Configuration;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateConnectionUtils {
    private static SessionFactory sessionFactory;

    private HibernateConnectionUtils() {}

    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (HibernateException e) {
            System.err.println("Hibernate problem: Failed to create SessionFactory");
            e.printStackTrace();
        }
    }

    public static Session getNewSession() {
        return sessionFactory.openSession();
    }
}
