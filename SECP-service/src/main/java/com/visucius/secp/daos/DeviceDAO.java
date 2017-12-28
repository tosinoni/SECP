package com.visucius.secp.daos;

import com.google.common.base.Optional;
import com.visucius.secp.models.Device;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import java.util.List;

public class DeviceDAO extends AbstractDAO<Device> {

    public DeviceDAO(SessionFactory provider) {
        super(provider);
    }

    public Optional<Device> find(long id) {
        return Optional.fromNullable(get(id));
    }

    public List<Device> findAll() {
        return (List<Device>) criteria().list();
    }

    public Device save(Device entity) throws HibernateException {
        return persist(entity);
    }

    public Device merge(Device entity) throws HibernateException {
        return (Device) currentSession().merge(entity);
    }

    public void delete(Device entity) throws HibernateException {
        currentSession().delete(entity);
    }

    public Device findByDeviceName(String name) {
        return (Device) namedQuery("com.visucius.secp.models.Device.findByDeviceName")
            .setParameter("name",name).uniqueResult();
    }

    public List<Device> getDevicesForUser(long userID)
    {
        return (List<Device>) namedQuery("com.visucius.secp.models.Device.getDevicesForUser").
            setParameter("id",userID).list();
    }

}
