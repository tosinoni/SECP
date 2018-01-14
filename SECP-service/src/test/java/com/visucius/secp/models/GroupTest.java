package com.visucius.secp.models;

import org.junit.Test;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.assertEquals;

public class GroupTest {


    @Test
    public void testGroupForEntityAndTableAttribute() {
        AssertAnnotations.assertType(Group.class, Entity.class, Table.class, NamedQueries.class);
    }

    @Test
    public void testId(){
        //Testing annotations on the id field
        AssertAnnotations.assertField(Group.class, "id", Id.class, GeneratedValue.class, Column.class);

        //Testing the @column annotation
        Column c = ReflectTool.getFieldAnnotation(Group.class, "id", Column.class);

        assertEquals("column id: name is not equal","id", c.name());
        assertEquals("column id: unique is false",true, c.unique());
        assertEquals("column id: nullable is true",false, c.nullable());
    }

    @Test
    public void testName(){
        AssertAnnotations.assertField(Group.class, "name", Column.class);

        Column c = ReflectTool.getFieldAnnotation(Group.class, "name", Column.class);

        assertEquals("column name: name is not equal", "name", c.name());
        assertEquals("column name: unique is false", true, c.unique());
        assertEquals("column name: nullable is true", false, c.nullable());
    }

    @Test
    public void testUsers() {
        AssertAnnotations.assertField( Group.class, "users", ManyToMany.class, JoinTable.class);

        //testing the @JoinTable annotation
        JoinTable j = ReflectTool.getFieldAnnotation(Group.class, "users", JoinTable.class);
        assertEquals("JoinTable users:  name is not equal", "group_user", j.name());
        assertEquals("JoinTable users:  joinColumns size is not 1", 1, j.joinColumns().length);
        assertEquals("JoinTable users:  inverseJoinColumns size is not 1", 1, j.inverseJoinColumns().length);

        //testing the joincolumn inside JoinColumns
        JoinColumn joinColumn = j.joinColumns()[0];
        assertEquals("JoinColumn users: name is not equal", "group_id", joinColumn.name());

        //testing the joincolumn inside inverseJoinColumns
        joinColumn = j.inverseJoinColumns()[0];
        assertEquals("JoinColumn users: name is not equal", "user_id", joinColumn.name());
    }

    @Test
    public void testRoles() {
        AssertAnnotations.assertField( Group.class, "roles", ManyToMany.class, JoinTable.class);

        //testing the @JoinTable annotation
        JoinTable j = ReflectTool.getFieldAnnotation(Group.class, "roles", JoinTable.class);
        assertEquals("JoinTable roles:  name is not equal", "group_roles", j.name());
        assertEquals("JoinTable roles:  joinColumns size is not 1", 1, j.joinColumns().length);
        assertEquals("JoinTable roles:  inverseJoinColumns size is not 1", 1, j.inverseJoinColumns().length);

        //testing the joincolumn inside JoinColumns
        JoinColumn joinColumn = j.joinColumns()[0];
        assertEquals("JoinColumn roles: name is not equal", "group_id", joinColumn.name());

        //testing the joincolumn inside inverseJoinColumns
        joinColumn = j.inverseJoinColumns()[0];
        assertEquals("JoinColumn users: name is not equal", "role_id", joinColumn.name());
    }

    @Test
    public void testMessages() {
        AssertAnnotations.assertField(Group.class, "messages",OneToMany.class);

        //testing @OneToMany annotation
        OneToMany o = ReflectTool.getFieldAnnotation(Group.class, "messages", OneToMany.class);

        assertEquals("OneToMany: mappedBy is not equal", "group", o.mappedBy());
        assertEquals("OneToMany: Fetch is not equal", FetchType.LAZY, o.fetch());
    }

    @Test
    public void test_SetAndGetId(){
        Group group = new Group("Group 1");
        group.setId(1);
        assertEquals("Id not equal", group.getId(), 1);
    }

    @Test
    public void testGetUsers(){
        Group group = new Group("Group 1");
        Set<User> users = new HashSet<>();
        group.setUsers(users);
        assertEquals("Users not equal", group.getUsers(),users);
    }

    @Test
    public void testGetRoles(){
        Group group = new Group("Group 1");
        Set<Role> roles = new HashSet<>();
        group.setRoles(roles);
        assertEquals("Roles not equal", group.getRoles(),roles);
    }

    @Test
    public void testGetMessages(){
        Group group = new Group("Group 1");
        Set<Message> messages = new HashSet<>();
        group.setMessages(messages);
        assertEquals("Messages not equal", group.getMessages(),messages);
    }

    @Test
    public void testNamedQueries() {

        //testing the @JoinTable annotation
        NamedQueries namedQueries = ReflectTool.getClassAnnotation(Group.class, NamedQueries.class);
        assertEquals("NamedQueries:  size is not equal to 4", 4, namedQueries.value().length);

        NamedQuery[] namedQueriesArray = namedQueries.value();

        assertEquals("NamedQueries[0]: name is not equal",
            "com.visucius.secp.models.Group.findByName", namedQueriesArray[0].name());
        assertEquals("NamedQueries[1]: name is not equal",
            "com.visucius.secp.models.Group.findAllActiveGroups", namedQueriesArray[1].name());
        assertEquals("NamedQueries[2]: name is not equal",
            "com.visucius.secp.models.Group.findGroupsForUser", namedQueriesArray[2].name());
        assertEquals("NamedQueries[3]: name is not equal",
            "com.visucius.secp.models.Group.findPrivateGroupForUsers", namedQueriesArray[3].name());
        assertEquals("NamedQueries[4]: name is not equal",
            "com.visucius.secp.models.Group.search", namedQueriesArray[4].name());
    }
}
