package Tests.systemTests;

import Logic.*;
import Utility.FileHandler;
import Utility.Strings;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Gila-Ber on 04/05/2016.
 */
public class hAPPiFacadeTest {

    hAPPiFacade facade = new hAPPiFacade();
    Application app;

    @Before
    public void setup() throws IOException {
        // clear the database
        facade.connectToDatabase();
        facade.getDataBase().clearAll();

        // clear the applications directory
        FileHandler.deleteFolder(Strings.PATH_APPS);
        FileHandler.createFolder(Strings.PATH_APPS);

        //create app
        ArrayList<String> platforms = new ArrayList<String>();
        platforms.add("android");
        app = new Application("appId", "appName", "username", platforms, new ArrayList<ApplicationObject>(),
                new ArrayList<ApplicationBehavior>(), new ArrayList<ApplicationEvent>());
    }

    @Test
    public void testCreateApplication() throws Exception {
        facade.createApplication(app);
        assertTrue(FileHandler.isFileExist(Strings.PATH_APPS + "\\" + app.getName()));
        // validate platforms
        assertTrue(FileHandler.isFileExist(Strings.PATH_APPS + "\\" + app.getName()
                + "\\platforms\\android"));
    }

    @Test
    public void testCreateApplicationDuplicateName() throws Exception {
        facade.createApplication(app);
        boolean duplicateNameError = false;
        try{
            facade.createApplication(app);
        }
        catch(Exception e) {
            duplicateNameError = true;
        }
        assertTrue(duplicateNameError);
    }

    @Test
    public void testUpdateApplication() throws Exception {
        String appOldName = app.getName();
        facade.createApplication(app);
        app.setName("newAppName");
        ArrayList<String> platforms = new ArrayList<String>();
        app.setPlatforms(platforms);
        facade.updateApplication(app, app.getUsername());
        assertTrue(FileHandler.isFileExist(Strings.PATH_APPS + "\\" + app.getName()));
        // validate platforms

        assertFalse(FileHandler.isFileExist(Strings.PATH_APPS + "\\" + appOldName));
        assertFalse(FileHandler.isFileExist(Strings.PATH_APPS + "\\" + app.getName()
                + "\\platforms\\android"));
    }

    @Test
    public void testBuildApplication() throws Exception {
        //TODO
    }

    @Test
    public void testCreateObject() throws Exception {
        //TODO - only DB changed
    }

    @Test
    public void testRemoveObject() throws Exception {
        //TODO - only DB changed
    }

    @Test
    public void testConnectToDatabase() throws Exception {
        //TODO
    }

    @Test
    public void testClearDatabase() throws Exception {
        //TODO
    }

    @Test
    public void testRemoveApplication() throws Exception {
        facade.createApplication(app);
        assertTrue(FileHandler.isFileExist(Strings.PATH_APPS + "\\" + app.getName()));
        facade.removeApplication(app.getId(), app.getUsername());
        assertFalse(FileHandler.isFileExist(Strings.PATH_APPS + "\\" + app.getName()));
    }

    @Test
    public void testRemovePlatforms() throws Exception {
        facade.createApplication(app);
        assertTrue(FileHandler.isFileExist(Strings.PATH_APPS + "\\" + app.getName()
                + "\\platforms\\android"));
        facade.removePlatforms(app.getName());
        assertFalse(FileHandler.isFileExist(Strings.PATH_APPS + "\\" + app.getName()
                + "\\platforms\\android"));
    }

    @Test
    public void testCreateBehavior() throws Exception {
        //TODO - only DB changed
    }

    @Test
    public void testRemoveBehavior() throws Exception {
        //TODO - only DB changed

    }

    @Test
    public void testGetDataBase() throws Exception {
        //TODO
    }

    @Test
    public void testGetPage() throws Exception {
        //TODO
    }

    @Test
    public void testUpdateApplicationObject() throws Exception {
        //TODO - only DB changed
    }

    @Test
    public void testUpdateApplicationBehavior() throws Exception {
        //TODO - only DB changed
    }

    @Test
    public void testAddUser() throws Exception {
        //TODO - only DB
    }

    @Test
    public void testAddObjectInstance() throws Exception {
        //TODO - only DB
    }

    @Test
    public void testRemoveObjectInstance() throws Exception {
        //TODO - only DB changed
    }

    @Test
    public void testGetImageAsBytes() throws Exception {
        //TODO
    }

    @Test
    public void testCreateEvent() throws Exception {
        //TODO - only DB
    }

    @Test
    public void testRemoveEvent() throws Exception {
        //TODO - only DB changed
    }

    @Test
    public void testUpdateApplicationEvent() throws Exception {
        //TODO - only DB changed
    }

    @Test
    public void testGetUser() throws Exception {
        //TODO - only DB
    }

    @Test
    public void testLogin() throws Exception {
        //TODO - only DB
    }
}