package com.visucius.secp.models;

import org.junit.Test;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class FilterTest {


    @Test
    public void testFilterForEntityAndTableAttribute() {
        AssertAnnotations.assertType(Filter.class, Entity.class, Table.class, NamedQueries.class);
    }

    @Test
    public void testId(){
        //Testing annotations on the id field
        AssertAnnotations.assertField(Filter.class, "id", Id.class, GeneratedValue.class, Column.class);

        //Testing the @column annotation
        Column c = ReflectTool.getFieldAnnotation(Filter.class, "id", Column.class);

        assertEquals("column id: name is not equal","id", c.name());
        assertEquals("column id: unique is false",true, c.unique());
        assertEquals("column id: nullable is true",false, c.nullable());
    }

    @Test
    public void testName(){
        AssertAnnotations.assertField(Filter.class, "name", Column.class);

        Column c = ReflectTool.getFieldAnnotation(Filter.class, "name", Column.class);

        assertEquals("column name: name is not equal", "name", c.name());
        assertEquals("column name: unique is false", true, c.unique());
        assertEquals("column name: nullable is true", false, c.nullable());
    }

    @Test
    public void testRoles() {
        AssertAnnotations.assertField( Filter.class, "roles", ManyToMany.class, JoinTable.class);

        //testing the @JoinTable annotation
        JoinTable j = ReflectTool.getFieldAnnotation(Filter.class, "roles", JoinTable.class);
        assertEquals("JoinTable roles:  name is not equal", "filter_roles", j.name());
        assertEquals("JoinTable roles:  joinColumns size is not 1", 1, j.joinColumns().length);
        assertEquals("JoinTable roles:  inverseJoinColumns size is not 1", 1, j.inverseJoinColumns().length);

        //testing the joincolumn inside JoinColumns
        JoinColumn joinColumn = j.joinColumns()[0];
        assertEquals("JoinColumn roles: name is not equal", "filter_id", joinColumn.name());
    }

    @Test
    public void testPermissions() {
        AssertAnnotations.assertField( Filter.class, "permissions", ManyToMany.class, JoinTable.class);

        //testing the @JoinTable annotation
        JoinTable j = ReflectTool.getFieldAnnotation(Filter.class, "permissions", JoinTable.class);
        assertEquals("JoinTable permissions:  name is not equal", "filter_permissions", j.name());
        assertEquals("JoinTable permissions:  joinColumns size is not 1", 1, j.joinColumns().length);
        assertEquals("JoinTable permissions:  inverseJoinColumns size is not 1", 1, j.inverseJoinColumns().length);

        //testing the joincolumn inside JoinColumns
        JoinColumn joinColumn = j.joinColumns()[0];
        assertEquals("JoinColumn permissions: name is not equal", "filter_id", joinColumn.name());
        assertEquals("JoinColumn permissions: name is not equal", "filter_id", joinColumn.name());
    }

    @Test
    public void test_SetAndGetId(){
        Filter filter = new Filter("Filter 1");
        filter.setId(1);
        assertEquals("Id not equal", filter.getId(), 1);
    }

    @Test
    public void testGetRoles(){
        Filter filter = new Filter("Filter 1");
        Set<Role> roles = new HashSet<>();
        filter.setRoles(roles);
        assertEquals("Roles not equal", filter.getRoles(),roles);
    }

    @Test
    public void testNamedQueries() {

        //testing the @JoinTable annotation
        NamedQueries namedQueries = ReflectTool.getClassAnnotation(Filter.class, NamedQueries.class);
        assertEquals("NamedQueries:  size is not equal", 1, namedQueries.value().length);

        NamedQuery[] namedQueriesArray = namedQueries.value();

        assertEquals("NamedQueries[0]: name is not equal",
            "com.visucius.secp.models.Filter.findByName", namedQueriesArray[0].name());
    }
}
