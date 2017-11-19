package com.visucius.secp.models;

import static org.junit.Assert.*;
import org.junit.Test;
import javax.persistence.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageTest {

    @Test
    public void testMessageForEntityAndTableAttribute() {
        AssertAnnotations.assertType(Message.class, Entity.class, Table.class);
    }

    @Test
    public void testId(){
        //Testing annotations on the id field
        AssertAnnotations.assertField(Message.class, "id", Id.class, GeneratedValue.class, Column.class);

        //Testing the @column annotation
        Column c = ReflectTool.getFieldAnnotation(Message.class, "id", Column.class);

        assertEquals("column id: name is not equal","id", c.name());
        assertEquals("column id: unique is false",true, c.unique());
        assertEquals("column id: nullable is true",false, c.nullable());
    }

    @Test
    public void testDateTime(){
        AssertAnnotations.assertField(Message.class, "timestamp", Column.class, Temporal.class);

        Column c = ReflectTool.getFieldAnnotation(Message.class, "timestamp", Column.class);

        assertEquals("column timestamp: name is not equal", "timestamp", c.name());
    }

    @Test
    public void testBody(){
        AssertAnnotations.assertField(Message.class, "body", Column.class, Lob.class);

        Column c = ReflectTool.getFieldAnnotation(Message.class, "body", Column.class);

        assertEquals("column body: name is not equal", "body", c.name());
    }

    @Test
    public void testUser(){
        AssertAnnotations.assertField(Message.class, "user", ManyToOne.class, JoinColumn.class);

        ManyToOne m = ReflectTool.getFieldAnnotation(Message.class, "user", ManyToOne.class);
        assertEquals("ManyToOne: Fetch is not equal", FetchType.LAZY, m.fetch());

        JoinColumn joinColumn = ReflectTool.getFieldAnnotation(Message.class, "user", JoinColumn.class);
        assertEquals("JoinColumn user: name is not equal", "user_id", joinColumn.name());
        assertEquals("JoinColumn user: nullable is true", false, joinColumn.nullable());
    }

    @Test
    public void testGroup() {
        AssertAnnotations.assertField(Message.class, "group",ManyToOne.class, JoinColumn.class);

        ManyToOne m = ReflectTool.getFieldAnnotation(Message.class, "group", ManyToOne.class);
        assertEquals("ManyToOne: Fetch is not equal", FetchType.LAZY, m.fetch());

        JoinColumn joinColumn = ReflectTool.getFieldAnnotation(Message.class, "group", JoinColumn.class);
        assertEquals("JoinColumn group: name is not equal", "group_id", joinColumn.name());
        assertEquals("JoinColumn group: nullable is true", false, joinColumn.nullable());
    }

    @Test
    public void test_setAndGetId(){
        User user = new User();
        Group group = new Group("Group 1");
        Message message = new Message("Body", user, group);

        message.setId(1);
        assertEquals("ID does not match",message.getId(),1);
    }

    @Test
    public void test_setAndGetTimestamp() throws ParseException {
        User user = new User();
        Group group = new Group("Group 1");
        Message message = new Message("Body", user, group);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("01/01/2017");
        long time = date.getTime();
        Timestamp timestamp = new Timestamp(time);

        message.setTimestamp(timestamp);
        assertEquals("timestamp not equal", message.getTimestamp(),date);
    }

    @Test
    public void test_setAndGetBody(){
        User user = new User();
        Group group = new Group("Group 1");
        Message message = new Message("Body", user, group);

        message.setBody("TestBody");
        assertEquals("Body not equal", message.getBody(), "TestBody");
    }

    @Test
    public void test_getGroup(){
        User user = new User();
        Group group = new Group("Group 1");
        Message message = new Message("Body", user, group);

        assertEquals("Group not equal", message.getGroup(), group);
    }

}
