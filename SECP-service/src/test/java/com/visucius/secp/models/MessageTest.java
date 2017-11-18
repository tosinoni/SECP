package com.visucius.secp.models;

import static org.junit.Assert.*;

import org.hibernate.validator.constraints.Email;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.*;

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
}
