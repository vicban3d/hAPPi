package Tests.UnitTests;

import Logic.*;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by Gila-Ber on 04/05/2016.
 */
public class ApplicationTest {

    Application app;

    @Before
    public void setup () {
        app = new Application("appId", "appName", "username", new ArrayList<String>(), new ArrayList<ApplicationObject>(),
                new ArrayList<ApplicationBehavior>(), new ArrayList<ApplicationEvent>());
    }

    @Test
    public void testAddObject() throws Exception {
        ApplicationObject obj = new ApplicationObject("objId", "objName", new ArrayList<ObjectAttribute>(), new ArrayList<ObjectAction>());
        app.addObject(obj);
        assertTrue(app.getObjects().size() == 1);
        assertEquals(app.getObjects().get(0), obj);
    }

    @Test
    public void testAddBehavior() throws Exception {
        ApplicationBehavior behavior = new ApplicationBehavior("behaviorId", "behaviorName", new ArrayList<BehaviorAction>());
        app.addBehavior(behavior);
        assertTrue(app.getBehaviors().size() == 1);
        assertEquals(app.getBehaviors().get(0), behavior);
    }

    @Test
    public void testAddEvent() throws Exception {
        ObjectAttribute attr = new ObjectAttribute("attr1", "Number");
        ApplicationObject obj = new ApplicationObject("objId", "objName", Arrays.asList(attr), new ArrayList<ObjectAction>());
        ApplicationEvent event = new ApplicationEvent("eventId", "eventName", obj, attr, "Less Than", "1");
        app.addEvent(event);
        assertTrue(app.getEvents().size() == 1);
        assertEquals(app.getEvents().get(0), event);
    }

    @Test
    public void testRemoveObject() throws Exception {
        ApplicationObject obj = new ApplicationObject("objId", "objName", new ArrayList<ObjectAttribute>(), new ArrayList<ObjectAction>());
        app.addObject(obj);
        assertTrue(app.getObjects().size() == 1);
        app.removeObject(obj);
        assertTrue(app.getObjects().size() == 0);
    }

    @Test
    public void testUpdateObject() throws Exception {
        ApplicationObject obj = new ApplicationObject("objId", "objName", new ArrayList<ObjectAttribute>(), new ArrayList<ObjectAction>());
        app.addObject(obj);
        ObjectAttribute attr = new ObjectAttribute("attr1", "Number");
        ArrayList<ObjectAttribute> attributes = new ArrayList<ObjectAttribute>();
        attributes.add(attr);
        obj.setAttributes(attributes);
        obj.setName("newName");
        app.updateObject(obj);
        ApplicationObject updatedObj = app.getObjects().get(0);
        assertEquals("newName", updatedObj.getName());
        assertEquals(attributes, updatedObj.getAttributes());
    }

    @Test
    public void testRemoveBehavior() throws Exception {
        ApplicationBehavior behavior = new ApplicationBehavior("behaviorId", "behaviorName", new ArrayList<BehaviorAction>());
        app.addBehavior(behavior);
        assertTrue(app.getBehaviors().size() == 1);
        app.removeBehavior(behavior);
        assertTrue(app.getBehaviors().size() == 0);
    }

    @Test
    public void testUpdateBehavior() throws Exception {
        ApplicationBehavior behavior = new ApplicationBehavior("behaviorId", "behaviorName", new ArrayList<BehaviorAction>());
        app.addBehavior(behavior);
        behavior.setName("newBehaviorName");

        ObjectAttribute attr = new ObjectAttribute("attr1", "Number");
        ApplicationObject obj = new ApplicationObject("objId", "objName", Arrays.asList(attr), new ArrayList<ObjectAction>());
        BehaviorAction action = new BehaviorAction(obj, attr, new ArrayList<Condition>(), "SumOfAll");
        ArrayList<BehaviorAction> actions = new ArrayList<BehaviorAction>();
        actions.add(action);
        behavior.setActions(actions);

        app.updateBehavior(behavior);
        ApplicationBehavior updatedBehavior = app.getBehaviors().get(0);
        assertEquals("newBehaviorName", updatedBehavior.getName());
        assertEquals(actions, updatedBehavior.getActions());
    }

    @Test
    public void testRemoveEvent() throws Exception {
        ObjectAttribute attr = new ObjectAttribute("attr1", "Number");
        ApplicationObject obj = new ApplicationObject("objId", "objName", Arrays.asList(attr), new ArrayList<ObjectAction>());
        ApplicationEvent event = new ApplicationEvent("eventId", "eventName", obj, attr, "Less Than", "1");
        app.addEvent(event);
        assertTrue(app.getEvents().size() == 1);
        app.removeEvent(event);
        assertTrue(app.getEvents().size() == 0);
    }

    @Test
    public void testUpdateEvent() throws Exception {
        ObjectAttribute attr = new ObjectAttribute("attr1", "Number");
        ApplicationObject obj = new ApplicationObject("objId", "objName", Arrays.asList(attr), new ArrayList<ObjectAction>());
        ApplicationEvent event = new ApplicationEvent("eventId", "eventName", obj, attr, "Less Than", "1");
        app.addEvent(event);

        event.setName("newEventName");
        attr.setName("newAttr");
        event.setObjectAttribute(attr);

        app.updateEvent(event);
        ApplicationEvent updatedEvent = app.getEvents().get(0);
        assertEquals(event, updatedEvent);
    }

    @Test
    public void testFromDocument() throws Exception {
        // platforms
        ArrayList<String> platforms = new ArrayList<String>();
        platforms.add("ios");
        platforms.add("android");
        // objects
        ArrayList<ApplicationObject> objects = new ArrayList<ApplicationObject>();
        ApplicationObject obj1 = new ApplicationObject("objId1", "objName1", new ArrayList<ObjectAttribute>(), new ArrayList<ObjectAction>());
        ApplicationObject obj2 = new ApplicationObject("objId2", "objName2", new ArrayList<ObjectAttribute>(), new ArrayList<ObjectAction>());
        objects.add(obj1); objects.add(obj2);
        // behaviors
        ArrayList<ApplicationBehavior> behaviors = new ArrayList<ApplicationBehavior>();
        ApplicationBehavior behavior1 = new ApplicationBehavior("behaviorId1", "behaviorName1", new ArrayList<BehaviorAction>());
        ApplicationBehavior behavior2 = new ApplicationBehavior("behaviorId2", "behaviorName2", new ArrayList<BehaviorAction>());
        behaviors.add(behavior1); behaviors.add(behavior2);

        // create document
        Document doc = new Document();
        doc.append("id", "appId");
        doc.append("name", "appName");
        doc.append("username", "appUsername");
        doc.append("platforms", platforms);
        doc.append("objects", objects);
        doc.append("behaviors", behaviors);

        Application application = app.fromDocument(doc);
        // validate results
        assertEquals("appId", application.getId());
        assertEquals("appName", application.getName());
        assertEquals("appUsername", application.getUsername());
        assertEquals(platforms, application.getPlatforms());
        assertEquals(objects, application.getObjects());
        assertEquals(behaviors, application.getBehaviors());
    }
}