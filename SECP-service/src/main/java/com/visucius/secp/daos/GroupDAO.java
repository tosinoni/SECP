package com.visucius.secp.daos;

import com.google.common.base.Optional;
import com.visucius.secp.models.Group;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import java.util.List;

public class GroupDAO extends AbstractDAO<Group> {


    public GroupDAO(SessionFactory provider) {
        super(provider);
    }


    public Optional<Group> find(long id) {
        return Optional.fromNullable(get(id));
    }

    public List<Group> findAll() {
        return (List<Group>) criteria().list();
    }

    public Group save(Group entity) throws HibernateException {
        return persist(entity);
    }

    public Group merge(Group entity) throws HibernateException {
        return (Group) currentSession().merge(entity);
    }

    public void delete(Group entity) throws HibernateException {
        currentSession().delete(entity);
    }

    public Group findByName(String name) {
        return (Group)
            namedQuery("com.visucius.secp.models.Group.findByName").
                setParameter("name",name).uniqueResult();
    }
}
