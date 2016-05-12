package Tests.UnitTests;

import Logic.ApplicationObject;
import Logic.ObjectAction;
import Logic.ObjectAttribute;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Gila-Ber on 04/05/2016.
 */
public class ApplicationObjectTest {

    @Test
    public void testFromDocument() throws Exception {

        // attributes
        ArrayList<ObjectAttribute> attributes = new ArrayList<ObjectAttribute>();
        ObjectAttribute attr1 = new ObjectAttribute("attr1", "Number");
        ObjectAttribute attr2 = new ObjectAttribute("attr2", "Number");
        attributes.add(attr1); attributes.add(attr2);

        // actions
        ArrayList<ObjectAction> actions = new ArrayList<ObjectAction>();
        ObjectAction action1 = new ObjectAction("action1", attr1, "Increase By", "1");
        ObjectAction action2 = new ObjectAction("action2", attr2, "Reduce By", "5");
        actions.add(action1); actions.add(action2);

        // create document
        Document doc = new Document();
        doc.append("id", "objectId");
        doc.append("name", "objectName");
        doc.append("attributes", attributes);
        doc.append("actions", actions);

        ApplicationObject applicationObject = ApplicationObject.fromDocument(doc);
        assertEquals("objectId", applicationObject.getId());
        assertEquals("objectName", applicationObject.getName());
        assertEquals(attributes, applicationObject.getAttributes());
        assertEquals(actions, applicationObject.getActions());
    }
}