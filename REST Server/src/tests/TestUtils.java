package tests;

import logic.AppInstance;
import logic.Application;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by Gila-Ber on 21/05/2016.
 */
public class TestUtils {

    public static void validateApplicationResults(Application expectedApp, Application actualApp) {
        assertEquals(expectedApp.getName(), actualApp.getName());
        assertEquals(expectedApp.getPlatforms(), actualApp.getPlatforms());
        assertEquals(expectedApp.getObjects(), actualApp.getObjects());
        assertEquals(expectedApp.getBehaviors(), actualApp.getBehaviors());
        assertEquals(expectedApp.getEvents(), actualApp.getEvents());
    }

    public static AppInstance createAppInstance() {
        Map<String, Map<String, List<String>>> instances = new HashMap<String, Map<String, List<String>>>();
        Map<String, List<String>> instanceValues = new HashMap<>();
        instanceValues.put("insId1", Arrays.asList("1", "2"));
        instanceValues.put("insId2", Arrays.asList("3","4"));
        instances.put("Attr1", instanceValues);
        return new AppInstance("appInstanceId","appId", instances);
    }

}
