package com.visucius.secp.models;

import static org.junit.Assert.*;

import org.hibernate.validator.constraints.Email;
import org.junit.Test;

import javax.persistence.*;

public class UserTest {

    @Test
    public void testUserForEntityAndTableAttribute() {
        AssertAnnotations.assertType(User.class, Entity.class, Table.class, NamedQueries.class);
    }

    @Test
    public void testId() {
        //testing all the annotations on the id field
        AssertAnnotations.assertField( User.class, "id", Id.class, GeneratedValue.class, Column.class);

        //testing the @column annotation
        Column c = ReflectTool.getFieldAnnotation(User.class, "id", Column.class);

        assertEquals("column id:  name is not equal", "id", c.name());
        assertEquals("column id: unique is false", true, c.unique());
        assertEquals("column id: nullable is true", false, c.nullable());
    }

    @Test
    public void testUserName() {
        AssertAnnotations.assertField( User.class, "username", Column.class);

        Column c = ReflectTool.getFieldAnnotation(User.class, "username", Column.class);

        assertEquals("column username:  name is not equal", "username", c.name());
        assertEquals("column username: unique is false", true, c.unique());
        assertEquals("column username: nullable is true", false, c.nullable());
    }

    @Test
    public void testFirstName() {
        AssertAnnotations.assertField( User.class, "firstname", Column.class);

        Column c = ReflectTool.getFieldAnnotation(User.class, "firstname", Column.class);

        assertEquals("column firstname:  name is not equal", "firstname", c.name());
        assertEquals("column firstname: nullable is true", false, c.nullable());
    }

    @Test
    public void testLastName() {
        AssertAnnotations.assertField( User.class, "lastname", Column.class);

        Column c = ReflectTool.getFieldAnnotation(User.class, "lastname", Column.class);

        assertEquals("column lastname:  name is not equal", "lastname", c.name());
        assertEquals("column lastname: nullable is true", false, c.nullable());
    }

    @Test
    public void testEmail() {
        AssertAnnotations.assertField( User.class, "email", Column.class, Email.class);

        Column c = ReflectTool.getFieldAnnotation(User.class, "email", Column.class);

        assertEquals("column email:  name is not equal", "email", c.name());
        assertEquals("column email: unique is false", true, c.unique());
        assertEquals("column email: nullable is true", false, c.nullable());
    }

    @Test
    public void testPassword() {
        AssertAnnotations.assertField( User.class, "password", Column.class);

        Column c = ReflectTool.getFieldAnnotation(User.class, "password", Column.class);

        assertEquals("column password:  name is not equal", "password", c.name());
        assertEquals("column password: nullable is true", false, c.nullable());
    }

    @Test
    public void testRoles() {
        AssertAnnotations.assertField( User.class, "roles", ManyToMany.class, JoinTable.class);

        //testing the @JoinTable annotation
        JoinTable j = ReflectTool.getFieldAnnotation(User.class, "roles", JoinTable.class);
        assertEquals("JoinTable roles:  name is not equal", "user_roles", j.name());
        assertEquals("JoinTable roles:  joinColumns size is not 1", 1, j.joinColumns().length);
        assertEquals("JoinTable roles:  inverseJoinColumns size is not 1", 1, j.inverseJoinColumns().length);

        //testing the joincolumn inside JoinColumns
        JoinColumn joinColumn = j.joinColumns()[0];
        assertEquals("JoinColumn roles: name is not equal", "user_id", joinColumn.name());

        //testing the joincolumn inside inverseJoinColumns
        joinColumn = j.inverseJoinColumns()[0];
        assertEquals("JoinColumn roles: name is not equal", "role_id", joinColumn.name());
    }


    @Test
    public void testTableName() {

        //testing the @JoinTable annotation
        Table table = ReflectTool.getClassAnnotation(User.class, Table.class);
        assertEquals("Table:  name is not equal", "Users", table.name());
    }

    @Test
    public void testNamedQueries() {

        //testing the @JoinTable annotation
        NamedQueries namedQueries = ReflectTool.getClassAnnotation(User.class, NamedQueries.class);
        assertEquals("NamedQueries:  size is not equal to 4", 4, namedQueries.value().length);

        NamedQuery[] namedQueriesArray = namedQueries.value();

        assertEquals("NamedQueries[0]: name is not equal",
            "com.visucius.secp.models.User.findByUsername", namedQueriesArray[0].name());
        assertEquals("NamedQueries[1]: name is not equal",
            "com.visucius.secp.models.User.findByEmail", namedQueriesArray[1].name());
        assertEquals("NamedQueries[2]: name is not equal",
            "com.visucius.secp.models.User.findUsersWithRole", namedQueriesArray[2].name());
        assertEquals("NamedQueries[3]: name is not equal",
            "com.visucius.secp.models.User.findUsersWithPermissionLevel", namedQueriesArray[3].name());
    }

    @Test
    public void testGroups() {
        //testing all the annotations on the id field
        AssertAnnotations.assertField(User.class, "groups", ManyToMany.class);

        //testing the @column annotation
        ManyToMany m = ReflectTool.getFieldAnnotation(User.class, "groups", ManyToMany.class);

        assertEquals("ManyToMany:  mappedBy is not equal", "users", m.mappedBy());
    }

    @Test
    public void testLoginRole() {
        AssertAnnotations.assertField( User.class, "loginRole", Column.class, Enumerated.class);

        Column c = ReflectTool.getFieldAnnotation(User.class, "loginRole", Column.class);

        assertEquals("column loginRole:  name is not equal", "login_role", c.name());
        assertEquals("column loginRole: nullable is true", false, c.nullable());

        Enumerated e = ReflectTool.getFieldAnnotation(User.class, "loginRole", Enumerated.class);

        assertEquals("Enumerated loginRole: enum value is not equal", EnumType.STRING, e.value());
    }
}
