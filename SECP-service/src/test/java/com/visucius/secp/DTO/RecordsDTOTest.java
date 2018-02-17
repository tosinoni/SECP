package com.visucius.secp.DTO;

import com.visucius.secp.models.ActionType;
import org.junit.Test;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class RecordsDTOTest {

    @Test
    public void testID() {
        RecordsDTO recordsDTO = new RecordsDTO();
        recordsDTO.setId(1);
        assertEquals("record id is not equal", 1, recordsDTO.getId());
    }

    @Test
    public void testEditorName() {
        RecordsDTO recordsDTO = new RecordsDTO();
        recordsDTO.setEditorName("John Doe");
        assertEquals("editor name is not equal", "John Doe", recordsDTO.getEditorName());
    }

    @Test
    public void testActionType() {
        RecordsDTO recordsDTO = new RecordsDTO();
        recordsDTO.setActionType(ActionType.USER);
        assertEquals("action type is not equal", ActionType.USER, recordsDTO.getActionType());
    }

    @Test
    public void testEditorAction() {
        RecordsDTO recordsDTO = new RecordsDTO();
        recordsDTO.setEditorAction("Modified user John");
        assertEquals("editor action is not equal", "Modified user John", recordsDTO.getEditorAction());
    }

    @Test
    public void testTimestamp() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("01/01/2017");
        long time = date.getTime();
        Timestamp timestamp = new Timestamp(time);

        RecordsDTO recordsDTO = new RecordsDTO();
        recordsDTO.setTimestamp(timestamp);
        assertEquals("editor action is not equal", date, recordsDTO.getTimestamp());
    }
}
