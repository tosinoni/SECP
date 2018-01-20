package com.visucius.secp.daos;

import com.google.common.base.Optional;
import com.visucius.secp.models.LoginRole;
import com.visucius.secp.models.User;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * A DAO for managing {@link User} objects.
 */
public class UserDAO extends AbstractDAO<User> {

    /**
     * Creates a new DAO with the given session provider.
     *
     * @param provider a session provider
     */
    public UserDAO(SessionFactory provider) {
        super(provider);
    }

    /**
     * Returns the {@link User} with the given ID.
     *
     * @param id the entity ID
     * @return the entity with the given ID
     */
    public Optional<User> find(long id) {
        return Optional.fromNullable(get(id));
    }

    public Optional<User> getUserWithGroups(long id)
    {
        Optional<User> optionalUser = find(id);
        if(optionalUser.isPresent())
            Hibernate.initialize(optionalUser.get().getGroups());

        return optionalUser;
    }

    public Optional<User> getUserWithDevices(long id)
    {
        Optional<User> optionalUser = find(id);
        if(optionalUser.isPresent())
            Hibernate.initialize(optionalUser.get().getDevices());

        return optionalUser;
    }

    /**
     * Returns the {@link User} with the given userName.
     *
     * @param userName the entity userName
     * @return the entity with the given userName
     */
    public User findByUserName(String userName) {
        return (User) namedQuery("com.visucius.secp.models.User.findByUsername").setParameter("username",userName).uniqueResult();
    }

    /**
     * Returns the {@link User} with the given email.
     *
     * @param email the entity email
     * @return the entity with the given email
     */
    public User findByEmail(String email) {
        return (User) namedQuery("com.visucius.secp.models.User.findByEmail").setParameter("email",email).uniqueResult();
    }

    /**
     * Returns all {@link User} entities.
     *
     * @return the list of entities
     */
    public List<User> findAll() {
        return (List<User>) criteria().list();
    }

    /**
     * Saves the given {@link User}.
     *
     * @param entity the entity to save
     * @return the persistent entity
     */
    public User save(User entity) throws HibernateException {
        return persist(entity);
    }

    /**
     * Merges the given {@link User}.
     *
     * @param entity the entity to merge
     * @return the persistent entity
     */
    public User merge(User entity) throws HibernateException {
        return (User) currentSession().merge(entity);
    }

    /**
     * Deletes the given {@link User}.
     *
     * @param entity the entity to delete
     */
    public void delete(User entity) throws HibernateException {
        currentSession().delete(entity);
    }

    public List<User> findUsersWithRole(long roleID)
    {
        return (List<User>) namedQuery("com.visucius.secp.models.User.findUsersWithRole").
            setParameter("roleID",roleID).list();
    }

    public List<User> findUsersWithPermissionLevel(long permissionID)
    {
        return (List<User>) namedQuery("com.visucius.secp.models.User.findUsersWithPermissionLevel").
            setParameter("permissionID",permissionID).list();
    }

    public List<User> findAdmins()
    {
        return (List<User>) namedQuery("com.visucius.secp.models.User.findAdmins").
            setParameter("loginRole", LoginRole.ADMIN).list();
    }


    public List<User> findActiveUsers()
    {
        return (List<User>)
            namedQuery("com.visucius.secp.models.User.findAllActiveUsers").list();
    }

    public List<User> searchForUser(String value)
    {
        return (List<User>) namedQuery("com.visucius.secp.models.User.search").
            setParameter("value","%" + value + "%").list();
    }
}
