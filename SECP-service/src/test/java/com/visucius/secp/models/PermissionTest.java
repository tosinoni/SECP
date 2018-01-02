package com.visucius.secp.models;

import org.junit.Test;

import javax.persistence.*;

import static org.junit.Assert.assertEquals;

public class PermissionTest {

    @Test
    public void testRoleForEntityAndTable() {
        AssertAnnotations.assertType(Permission.class, Entity.class, Table.class, NamedQueries.class);
    }

    @Test
    public void testId() {
        //testing all the annotations on the id field
        AssertAnnotations.assertField( Permission.class, "id", Id.class, GeneratedValue.class, Column.class);

        //testing the @column annotation
        Column c = ReflectTool.getFieldAnnotation(Permission.class, "id", Column.class);

        assertEquals("column id:  name is not equal", "id", c.name());
        assertEquals("column id: unique is false", true, c.unique());
        assertEquals("column id: nullable is true", false, c.nullable());
    }

    @Test
    public void testLevel() {
        //testing all the annotations on the id field
        AssertAnnotations.assertField( Permission.class, "level", Column.class);

        //testing the @column annotation
        Column c = ReflectTool.getFieldAnnotation(Permission.class, "level", Column.class);

        assertEquals("column id:  name is not equal", "level", c.name());
        assertEquals("column id: unique is false", true, c.unique());
        assertEquals("column id: nullable is true", false, c.nullable());
    }

    @Test
    public void testUsers() {
        //testing all the annotations on the id field
        AssertAnnotations.assertField( Permission.class, "users", ManyToMany.class);

        //testing the @column annotation
        ManyToMany m = ReflectTool.getFieldAnnotation(Permission.class, "users", ManyToMany.class);

        assertEquals("ManyToMany:  mappedBy is not equal", "permissions", m.mappedBy());
    }


    @Test
    public void testGroups() {
        //testing all the annotations on the id field
        AssertAnnotations.assertField( Permission.class, "groups", ManyToMany.class);

        //testing the @column annotation
        ManyToMany m = ReflectTool.getFieldAnnotation(Permission.class, "groups", ManyToMany.class);

        assertEquals("ManyToMany:  mappedBy is not equal", "permissions", m.mappedBy());
    }

    @Test
    public void testNamedQueries() {

        //testing the @JoinTable annotation
        NamedQueries namedQueries = ReflectTool.getClassAnnotation(Permission.class, NamedQueries.class);
        assertEquals("NamedQueries:  size is not equal to 1", 1, namedQueries.value().length);

        NamedQuery[] namedQueriesArray = namedQueries.value();

        assertEquals("NamedQueries[0]: name is not equal",
            "com.visucius.secp.models.Permission.findByName", namedQueriesArray[0].name());
    }
}
