package ru.kpfu.itis.kropinov.repository;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.kropinov.model.User;

import java.util.List;


public class UserRepositoryHibernate {

    private final SessionFactory sessionFactory;

    public UserRepositoryHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from User", User.class)
                .list();
    }

    @Transactional
    public void save(User user) {
        sessionFactory.getCurrentSession().persist(user);
    }
}
