package com.visucius.secp.daos;

import com.google.common.base.Optional;
import com.visucius.secp.models.PermissionLevel;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import java.util.List;

public class PermissionLevelDAO extends AbstractDAO<PermissionLevel> {

    public PermissionLevelDAO(SessionFactory provider) {
        super(provider);
    }


    public Optional<PermissionLevel> find(long id) {
        return Optional.fromNullable(get(id));
    }

    public List<PermissionLevel> findAll() {
        return (List<PermissionLevel>) criteria().list();
    }

    public PermissionLevel save(PermissionLevel entity) throws HibernateException {
        return persist(entity);
    }

    public PermissionLevel merge(PermissionLevel entity) throws HibernateException {
        return (PermissionLevel) currentSession().merge(entity);
    }

    public void delete(PermissionLevel entity) throws HibernateException {
        currentSession().delete(entity);
    }
}
