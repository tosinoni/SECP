package com.visucius.secp.models;

import org.junit.Test;

import javax.persistence.*;

import static org.junit.Assert.assertEquals;

public class RoleTest {

    @Test
    public void testRoleForEntityAndTable() {
        AssertAnnotations.assertType(Role.class, Entity.class, Table.class);
    }

    @Test
    public void testId() {
        //testing all the annotations on the id field
        AssertAnnotations.assertField( Role.class, "id", Id.class, GeneratedValue.class, Column.class);

        //testing the @column annotation
        Column c = ReflectTool.getFieldAnnotation(Role.class, "id", Column.class);

        assertEquals("column id:  name is not equal", "id", c.name());
        assertEquals("column id: unique is false", true, c.unique());
        assertEquals("column id: nullable is true", false, c.nullable());
    }

    @Test
    public void testRole() {
        //testing all the annotations on the id field
        AssertAnnotations.assertField( Role.class, "role", Column.class);

        //testing the @column annotation
        Column c = ReflectTool.getFieldAnnotation(Role.class, "role", Column.class);

        assertEquals("column id:  name is not equal", "role", c.name());
        assertEquals("column id: unique is false", true, c.unique());
        assertEquals("column id: nullable is true", false, c.nullable());
    }

    @Test
    public void testUsers() {
        //testing all the annotations on the id field
        AssertAnnotations.assertField( Role.class, "users", ManyToMany.class);

        //testing the @column annotation
        ManyToMany m = ReflectTool.getFieldAnnotation(Role.class, "users", ManyToMany.class);

        assertEquals("ManyToMany:  mappedBy is not equal", "roles", m.mappedBy());
    }


    @Test
    public void testGroups() {
        //testing all the annotations on the id field
        AssertAnnotations.assertField( Role.class, "groups", ManyToMany.class);

        //testing the @column annotation
        ManyToMany m = ReflectTool.getFieldAnnotation(Role.class, "groups", ManyToMany.class);

        assertEquals("ManyToMany:  mappedBy is not equal", "roles", m.mappedBy());
    }
}
