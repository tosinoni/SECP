package com.visucius.secp.daos;

import com.google.common.base.Optional;
import com.visucius.secp.models.Group;
import com.visucius.secp.models.GroupType;
import com.visucius.secp.models.User;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Set;

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

    public List<Group> findAllPublicGroups()
    {
        return (List<Group>)
            namedQuery("com.visucius.secp.models.Group.findAllPublicGroups")
                .setParameter("type", GroupType.PUBLIC).list();
    }

    public Group findByName(String name) {
        return (Group)
            namedQuery("com.visucius.secp.models.Group.findByName").
                setParameter("name",name).uniqueResult();
    }

    public List<Group> findGroupsForUser(long permissionID, List<Long> roleIDS)
    {
        return (List<Group>) namedQuery("com.visucius.secp.models.Group.findGroupsForUser").
            setParameter("permissionID",permissionID)
            .setParameterList("roleIDS",roleIDS).list();
    }

    public List<Group> findPrivateGroupForUsers(Set<User> users)
    {
        return (List<Group>) namedQuery("com.visucius.secp.models.Group.findPrivateGroupForUsers").
            setParameter("users",users).list();
    }

    public List<Group> searchForGroup(String value)
    {
        return (List<Group>) namedQuery("com.visucius.secp.models.Group.search").
            setParameter("value","%" + value + "%").list();
    }
}
