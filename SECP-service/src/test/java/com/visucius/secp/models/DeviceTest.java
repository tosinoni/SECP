package com.visucius.secp.models;

import org.junit.Test;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class DeviceTest {

    @Test
    public void testDeviceForEntityAndTableAttribute() {
        AssertAnnotations.assertType(Device.class, Entity.class, Table.class, NamedQueries.class);
    }

    @Test
    public void testId() {
        //testing all the annotations on the id field
        AssertAnnotations.assertField( Device.class, "id", Id.class, GeneratedValue.class, Column.class);

        //testing the @column annotation
        Column c = ReflectTool.getFieldAnnotation(Device.class, "id", Column.class);

        assertEquals("column id:  name is not equal", "id", c.name());
        assertEquals("column id: unique is false", true, c.unique());
        assertEquals("column id: nullable is true", false, c.nullable());

        //testing setters and getters
        Device device = getDevice();
        assertEquals("id is not equal", 1, device.getId());
    }

    @Test
    public void testDeviceName() {
        AssertAnnotations.assertField( Device.class, "name", Column.class);

        Column c = ReflectTool.getFieldAnnotation(Device.class, "name", Column.class);

        assertEquals("column name:  name is not equal", "name", c.name());
        assertEquals("column name: unique is false", true, c.unique());
        assertEquals("column name: nullable is true", false, c.nullable());

        //testing setters and getters
        Device device = getDevice();
        assertEquals("name is not equal", "device1", device.getName());
    }

    @Test
    public void testDeviceForPublicKey() {
        AssertAnnotations.assertField( Device.class, "publicKey", Column.class);

        Column c = ReflectTool.getFieldAnnotation(Device.class, "publicKey", Column.class);

        assertEquals("column publicKey:  name is not equal", "public_key", c.name());
        assertEquals("column publicKey: nullable is true", false, c.nullable());

        //testing setters and getters
        Device device = getDevice();
        assertEquals("public key is not equal", "publickey1", device.getPublicKey());
    }

    @Test
    public void testUsers() {
        //testing all the annotations on the id field
        AssertAnnotations.assertField( Device.class, "users", ManyToMany.class);

        //testing the @column annotation
        ManyToMany m = ReflectTool.getFieldAnnotation(Device.class, "users", ManyToMany.class);

        assertEquals("ManyToMany:  mappedBy is not equal", "devices", m.mappedBy());
        assertEquals("ManyToMany:  fetch is not equal", FetchType.LAZY, m.fetch());

        //testing setters and getters
        Device device = getDevice();
        assertEquals("users size is not equal", 1, device.getUsers().size());
    }

    @Test
    public void testNamedQueries() {

        //testing the @JoinTable annotation
        NamedQueries namedQueries = ReflectTool.getClassAnnotation(Device.class, NamedQueries.class);
        assertEquals("NamedQueries:  size is not equal to 2", 2, namedQueries.value().length);

        NamedQuery[] namedQueriesArray = namedQueries.value();

        assertEquals("NamedQueries[0]: name is not equal",
            "com.visucius.secp.models.Device.findByDeviceName", namedQueriesArray[0].name());
        assertEquals("NamedQueries[1]: name is not equal",
            "com.visucius.secp.models.Device.getDevicesForUser", namedQueriesArray[1].name());
    }

    @Test
    public void testEquals() {
        Device device = getDevice();
        Device newDevice = new Device();
        newDevice.setId(1);
        newDevice.setName("device1");
        newDevice.setPublicKey("publickey1");

        assertFalse("device is equal", device.equals(newDevice));
    }
    private Device getDevice() {
        Device device = new Device();
        device.setId(1);
        device.setName("device1");
        device.setPublicKey("publickey1");

        Set<User> users = new HashSet<>();
        User user = new User("user1", "e@g");
        users.add(user);

        device.setUsers(users);
        return device;
    }
}
