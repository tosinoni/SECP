package com.visucius.secp.daos;

import com.google.common.base.Optional;
import com.visucius.secp.models.Permission;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import java.util.List;

public class PermissionDAO extends AbstractDAO<Permission> {

    public PermissionDAO(SessionFactory provider) {
        super(provider);
    }


    public Optional<Permission> find(long id) {
        return Optional.fromNullable(get(id));
    }

    public List<Permission> findAll() {
        return (List<Permission>) criteria().list();
    }

    public Permission save(Permission entity) throws HibernateException {
        return persist(entity);
    }

    public Permission merge(Permission entity) throws HibernateException {
        return (Permission) currentSession().merge(entity);
    }

    public void delete(Permission entity) throws HibernateException {
        currentSession().delete(entity);
    }
}
