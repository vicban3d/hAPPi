package Tests.unitTests;

import Logic.AppInstance;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.jvnet.ws.wadl.Doc;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by Gila-Ber on 04/05/2016.
 */
public class AppInstanceTest {

    AppInstance appInstance;

    @Before
    public void setup() {
        appInstance = createAppInstance();
    }

    @Test
    public void testFromDocument() throws Exception {
        Document doc = new Document();
        doc.append("id", "instanceId");
        doc.append("app_id", "appId");
        doc.append("object_map", appInstance.getObjectInstances());

        AppInstance instance = AppInstance.fromDocument(doc);
        assertEquals("instanceId", instance.getId());
        assertEquals("appId", instance.getApp_id());
        assertEquals(appInstance.getObjectInstances(), instance.getObjectInstances());
    }

    @Test
    public void testAddObjectInstance_existObject() throws Exception {
        List<List<String>> attrInstances = appInstance.getObjectInstances().get("Attr");
        assertTrue(attrInstances.size() == 2);

        List<String> attributes = new ArrayList<String>();
        attributes.add("5");
        attributes.add("6");

        appInstance.addObjectInstance("Attr", attributes);
        attrInstances = appInstance.getObjectInstances().get("Attr");
        assertTrue(attrInstances.size() == 3);
        assertTrue(attrInstances.contains(attributes));
    }

    @Test
    public void testAddObjectInstance_notExistObject() throws Exception {
        List<String> attributes = new ArrayList<String>();
        attributes.add("5");
        attributes.add("6");
        appInstance.addObjectInstance("newAttr", attributes);
        Map<String, List<List<String>>> objectInstances = appInstance.getObjectInstances();
        assertTrue(objectInstances.size() == 2); //Attr and newAttr
        List<List<String>> newAttrInstances = objectInstances.get("newAttr");
        assertTrue(newAttrInstances.size() == 1);
        assertEquals(attributes, newAttrInstances.get(0));
    }

    @Test
    public void testRemoveObjectInstance() throws Exception {
        appInstance.removeObjectInstance("Attr", 0);
        List<List<String>> attr = appInstance.getObjectInstances().get("Attr");
        assertTrue(attr.size() == 1);
        assertEquals(Arrays.asList("3","4"), attr.get(0));
    }


    private AppInstance createAppInstance() {
        Map<String, List<List<String>>> instances = new HashMap<String, List<List<String>>>();
        List<List<String>> instanceValues = new ArrayList<List<String>>();
        instanceValues.add(Arrays.asList("1","2"));
        instanceValues.add(Arrays.asList("3","4"));
        instances.put("Attr", instanceValues);
        return new AppInstance("appInstanceId","appId", instances);
    }
}