package tests.unitTests;

import logic.ObjectAction;
import logic.ObjectAttribute;
import org.bson.Document;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Gila-Ber on 04/05/2016.
 */
public class ObjectActionTest {

    @Test
    public void testFromDocument() throws Exception {

        ObjectAttribute attr = new ObjectAttribute("attr1", "Number");

        Document doc = new Document();
        doc.append("name", "objActionName");
        doc.append("operand1", attr);
        doc.append("operator", "Increase By");
        doc.append("operand2", "2");

        ObjectAction objectAction = ObjectAction.fromDocument(doc);
        assertEquals("objActionName", objectAction.getName());
        assertEquals(attr, objectAction.getOperand1());
        assertEquals("Increase By", objectAction.getOperator());
        assertEquals("2", objectAction.getOperand2());
    }
}