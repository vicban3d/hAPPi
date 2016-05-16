package Tests.unitTests;

import Logic.ObjectAttribute;
import org.bson.Document;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Gila-Ber on 04/05/2016.
 */
public class ObjectAttributeTest {

    @Test
    public void testFromDocument() throws Exception {

        Document doc = new Document();
        doc.append("name", "attr");
        doc.append("type", "Number");

        ObjectAttribute objectAttribute = ObjectAttribute.fromDocument(doc);
        assertEquals("attr", objectAttribute.getName());
        assertEquals("Number", objectAttribute.getType());
    }
}