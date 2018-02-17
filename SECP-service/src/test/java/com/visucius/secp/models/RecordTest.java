package com.visucius.secp.models;

import org.junit.Test;

import javax.persistence.*;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class RecordTest {

    @Test
    public void testRecordForEntityAndTable() {
        AssertAnnotations.assertType(Record.class, Entity.class, Table.class);
    }

    @Test
    public void testId() {
        //testing all the annotations on the id field
        AssertAnnotations.assertField( Record.class, "id", Id.class, GeneratedValue.class, Column.class);

        //testing the @column annotation
        Column c = ReflectTool.getFieldAnnotation(Record.class, "id", Column.class);

        assertEquals("column id:  name is not equal", "id", c.name());
        assertEquals("column id: unique is false", true, c.unique());
        assertEquals("column id: nullable is true", false, c.nullable());

        Record record = new Record();
        record.setId(1);
        assertEquals("record id is not equal", 1, record.getId());
    }

    @Test
    public void testEditorName() {
        AssertAnnotations.assertField( Record.class, "editorName", Column.class);

        Column c = ReflectTool.getFieldAnnotation(Record.class, "editorName", Column.class);

        assertEquals("column editorName:  name is not equal", "editor_name", c.name());
        assertEquals("column editorName: nullable is true", false, c.nullable());

        Record record = new Record();
        record.setEditorName("John Doe");
        assertEquals("editor name is not equal", "John Doe", record.getEditorName());
    }

    @Test
    public void testEditorAction() {
        AssertAnnotations.assertField( Record.class, "editorAction", Column.class, Lob.class);

        Column c = ReflectTool.getFieldAnnotation(Record.class, "editorAction", Column.class);

        assertEquals("column editorAction:  name is not equal", "editor_action", c.name());
        assertEquals("column editorAction: nullable is true", false, c.nullable());

        Record record = new Record();
        record.setEditorAction("Created new Role PERMISSION");
        assertEquals("editor action is not equal", "Created new Role PERMISSION", record.getEditorAction());
    }


    @Test
    public void testActionType() {
        AssertAnnotations.assertField( Record.class, "actionType", Column.class, Enumerated.class);

        Column c = ReflectTool.getFieldAnnotation(Record.class, "actionType", Column.class);

        assertEquals("column actionType:  name is not equal", "action_type", c.name());
        assertEquals("column actionType: nullable is true", false, c.nullable());

        Enumerated e = ReflectTool.getFieldAnnotation(Record.class, "actionType", Enumerated.class);

        assertEquals("Enumerated actionType: enum value is not equal", EnumType.STRING, e.value());

        Record record = new Record();
        record.setActionType(ActionType.USER);
        assertEquals("action type is not equal", ActionType.USER, record.getActionType());
    }

    @Test
    public void testTimestamp() throws ParseException {
        AssertAnnotations.assertField(Record.class, "timestamp", Column.class, Temporal.class);

        Column c = ReflectTool.getFieldAnnotation(Record.class, "timestamp", Column.class);

        assertEquals("column timestamp: name is not equal", "timestamp", c.name());

        Temporal t = ReflectTool.getFieldAnnotation(Record.class, "timestamp", Temporal.class);

        assertEquals("temporal timestamp: name is not equal", TemporalType.TIMESTAMP, t.value());


        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("01/01/2017");
        long time = date.getTime();
        Timestamp timestamp = new Timestamp(time);

        Record record = new Record();
        record.setTimestamp(timestamp);

        assertEquals("timestamp not equal", date, record.getTimestamp());
    }
}
