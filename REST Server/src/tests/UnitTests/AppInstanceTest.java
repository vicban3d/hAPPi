//package tests.unitTests;
//
//import logic.AppInstance;
//import org.bson.Document;
//import org.junit.Before;
//import org.junit.Test;
//import tests.TestUtils;
//
//import java.util.*;
//
//import static org.junit.Assert.*;
//
///**
// * Created by Gila-Ber on 04/05/2016.
// */
//public class AppInstanceTest {
//
//    AppInstance appInstance;
//
//    @Before
//    public void setup() {
//        appInstance = TestUtils.createAppInstance();
//    }
//
//    @Test
//    public void testFromDocument() throws Exception {
//        Document doc = new Document();
//        doc.append("id", "instanceId");
//        doc.append("app_id", "appId");
//        doc.append("object_map", appInstance.getObjectInstances());
//
//        AppInstance instance = AppInstance.fromDocument(doc);
//        assertEquals("instanceId", instance.getId());
//        assertEquals("appId", instance.getApp_id());
//        assertEquals(appInstance.getObjectInstances(), instance.getObjectInstances());
//    }
//
//    @Test
//    public void testAddObjectInstance_existObject() throws Exception {
//        Map<String, List<String>> attrInstances = appInstance.getObjectInstances().get("Attr1");
//        assertTrue(attrInstances.size() == 2);
//
//        List<String> attributes = new ArrayList<String>();
//        attributes.add("5");
//        attributes.add("6");
//
//        appInstance.addObjectInstance("Attr1", attributes);
//        attrInstances = appInstance.getObjectInstances().get("Attr1");
//        assertTrue(attrInstances.size() == 3);
//        assertTrue(attrInstances.contains(attributes));
//    }
//
//    @Test
//    public void testAddObjectInstance_notExistObject() throws Exception {
//        List<String> attributes = new ArrayList<String>();
//        attributes.add("5");
//        attributes.add("6");
//        appInstance.addObjectInstance("newAttr", attributes);
//        Map<String, List<List<String>>> objectInstances = appInstance.getObjectInstances();
//        assertTrue(objectInstances.size() == 2); //Attr and newAttr
//        List<List<String>> newAttrInstances = objectInstances.get("newAttr");
//        assertTrue(newAttrInstances.size() == 1);
//        assertEquals(attributes, newAttrInstances.get(0));
//    }
//
//    @Test
//    public void testRemoveObjectInstance() throws Exception {
//        appInstance.removeObjectInstance("Attr1", 0);
//        List<List<String>> attr = appInstance.getObjectInstances().get("Attr1");
//        assertTrue(attr.size() == 1);
//        assertEquals(Arrays.asList("3","4"), attr.get(0));
//    }
//
//}