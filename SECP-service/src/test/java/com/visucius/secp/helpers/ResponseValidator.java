package com.visucius.secp.helpers;


import org.json.JSONObject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import javax.ws.rs.core.Response;

public class ResponseValidator {
    public static void validate(Response response, int status, String msg) {
        assertNotNull("No response was produced", response);

        response.bufferEntity(); //this is needed in case validate is called more than once
        JSONObject jsonObject = new JSONObject(response.readEntity(String.class));

        assertEquals(status, response.getStatus());
        assertFalse("Response does not have the code property", jsonObject.isNull("code"));
        assertFalse("Response does not have the message property", jsonObject.isNull("message"));
        assertEquals("Response code is not equal", status, jsonObject.getInt("code"));
        assertEquals("Response message is not equal",msg, jsonObject.getString("message"));
    }

    public static void validate(Response response, int status) {
        assertEquals(status, response.getStatus());
    }

    public static void validate(Response response, String key, String msg) {
        assertNotNull("No response was produced", response);

        response.bufferEntity();

        JSONObject jsonObject = new JSONObject(response.readEntity(String.class));
        assertFalse("Response does not have the " + key + " property", jsonObject.isNull(key));
        assertEquals("Response " + key + " is not equal", msg, jsonObject.getString(key));
    }
}
