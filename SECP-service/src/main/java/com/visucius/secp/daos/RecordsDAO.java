package com.visucius.secp.daos;

import com.visucius.secp.models.Record;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import java.util.List;

public class RecordsDAO extends AbstractDAO<Record> {

    public RecordsDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<Record> findAll() {
        return (List<Record>) criteria().list();
    }

    public Record save(Record entity) throws HibernateException {
        return persist(entity);
    }
}
