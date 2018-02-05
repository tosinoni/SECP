package com.visucius.secp.models;

import org.junit.Test;

import javax.persistence.*;

import static org.junit.Assert.assertEquals;

public class SecretTest {
    @Test
    public void testSecretForEntityAndTable() {
        AssertAnnotations.assertType(Secret.class, Entity.class, Table.class, NamedQueries.class);
    }

    @Test
    public void testId() {
        //testing all the annotations on the id field
        AssertAnnotations.assertField(Secret.class, "id", Id.class, GeneratedValue.class, Column.class);

        //testing the @column annotation
        Column c = ReflectTool.getFieldAnnotation(Secret.class, "id", Column.class);

        assertEquals("column id:  name is not equal", "id", c.name());
        assertEquals("column id: unique is false", true, c.unique());
        assertEquals("column id: nullable is true", false, c.nullable());
    }

    @Test
    public void testGroupId() {
        //testing all the annotations on the groupID field
        AssertAnnotations.assertField( Secret.class, "groupID", Column.class);

        //testing the @column annotation
        Column c = ReflectTool.getFieldAnnotation(Secret.class, "groupID", Column.class);

        assertEquals("column groupID:  name is not equal", "group_id", c.name());
        assertEquals("column groupID: nullable is true", false, c.nullable());
    }

    @Test
    public void testUserId() {
        //testing all the annotations on the groupID field
        AssertAnnotations.assertField( Secret.class, "userID", Column.class);

        //testing the @column annotation
        Column c = ReflectTool.getFieldAnnotation(Secret.class, "userID", Column.class);

        assertEquals("column userID:  name is not equal", "user_id", c.name());
        assertEquals("column userID: nullable is true", false, c.nullable());
    }

    @Test
    public void testEncryptedSecret() {
        //testing all the annotations on the id field
        AssertAnnotations.assertField( Secret.class, "encryptedSecret", Column.class, Lob.class);

        //testing the @column annotation
        Column c = ReflectTool.getFieldAnnotation(Secret.class, "encryptedSecret", Column.class);

        assertEquals("column encryptedSecret:  name is not equal", "encrypted_secret", c.name());
        assertEquals("column encryptedSecret: nullable is true", false, c.nullable());
    }

    @Test
    public void testDevice(){
        AssertAnnotations.assertField(Secret.class, "device", ManyToOne.class, JoinColumn.class);

        ManyToOne m = ReflectTool.getFieldAnnotation(Secret.class, "device", ManyToOne.class);
        assertEquals("ManyToOne: Fetch is not equal", FetchType.LAZY, m.fetch());

        JoinColumn joinColumn = ReflectTool.getFieldAnnotation(Secret.class, "device", JoinColumn.class);
        assertEquals("JoinColumn device: name is not equal", "device_id", joinColumn.name());
        assertEquals("JoinColumn device: nullable is true", false, joinColumn.nullable());
    }

    @Test
    public void testNamedQueries() {

        //testing the @JoinTable annotation
        NamedQueries namedQueries = ReflectTool.getClassAnnotation(Secret.class, NamedQueries.class);
        assertEquals("NamedQueries:  size is not equal", 2, namedQueries.value().length);

        NamedQuery[] namedQueriesArray = namedQueries.value();

        assertEquals("NamedQueries[0]: name is not equal",
            "com.visucius.secp.models.Secret.findSecretForDevice", namedQueriesArray[0].name());
        assertEquals("NamedQueries[1]: name is not equal",
            "com.visucius.secp.models.Secret.findSecretForUserDevice", namedQueriesArray[1].name());
    }
}
