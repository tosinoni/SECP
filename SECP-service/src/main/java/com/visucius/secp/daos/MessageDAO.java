package com.visucius.secp.daos;

import com.google.common.base.Optional;
import com.visucius.secp.models.Message;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class MessageDAO extends AbstractDAO<Message> {

    public MessageDAO(SessionFactory provider) {
        super(provider);
    }


    public Optional<Message> find(long id) {
        return Optional.fromNullable(get(id));
    }

    public List<Message> findAll() {
        return (List<Message>) criteria().list();
    }

    public Message save(Message entity) throws HibernateException {
        return persist(entity);
    }

    public Message merge(Message entity) throws HibernateException {
        return (Message) currentSession().merge(entity);
    }

    public void delete(Message entity) throws HibernateException {
        currentSession().delete(entity);
    }

    public List<Message> findMessageWithGroupID(long groupID, int offset, int limit)
    {
        Query query = namedQuery("com.visucius.secp.models.Message.findMessageWithGroupID").
            setParameter("groupID",groupID);
        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return (List<Message>) query.list();
    }
}
