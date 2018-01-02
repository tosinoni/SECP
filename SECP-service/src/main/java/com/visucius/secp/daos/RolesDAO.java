package com.visucius.secp.daos;

import com.google.common.base.Optional;
import com.visucius.secp.models.Role;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import java.util.List;

public class RolesDAO extends AbstractDAO<Role> {


    public RolesDAO(SessionFactory provider) {
        super(provider);
    }


    public Optional<Role> find(long id) {
        return Optional.fromNullable(get(id));
    }

    public List<Role> findAll() {
        return (List<Role>) criteria().list();
    }

    public Role save(Role entity) throws HibernateException {
        return persist(entity);
    }

    public Role merge(Role entity) throws HibernateException {
        return (Role) currentSession().merge(entity);
    }

    public void delete(Role entity) throws HibernateException {
        currentSession().delete(entity);
    }

    public Role findByName(String name) {
        return (Role)
            namedQuery("com.visucius.secp.models.Role.findByName").
                setParameter("name",name).uniqueResult();
    }
}
