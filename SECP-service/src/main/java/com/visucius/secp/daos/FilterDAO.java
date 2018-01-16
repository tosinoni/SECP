package com.visucius.secp.daos;

import com.google.common.base.Optional;
import com.visucius.secp.models.Filter;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import java.util.List;

public class FilterDAO extends AbstractDAO<Filter> {


    public FilterDAO(SessionFactory provider) {
        super(provider);
    }


    public Optional<Filter> find(long id) {
        return Optional.fromNullable(get(id));
    }

    public List<Filter> findAll() {
        return (List<Filter>) criteria().list();
    }

    public Filter save(Filter entity) throws HibernateException {
        return persist(entity);
    }

    public void delete(Filter entity) throws HibernateException {
        currentSession().delete(entity);
    }

    public Filter findByName(String name) {
        return (Filter)
            namedQuery("com.visucius.secp.models.Filter.findByName").
                setParameter("name",name).uniqueResult();
    }
}
