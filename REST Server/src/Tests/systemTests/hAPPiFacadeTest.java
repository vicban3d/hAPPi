package tests.systemTests;


import database.Database;
import exceptions.CordovaRuntimeException;
import exceptions.InvalidUserCredentialsException;
import utility.FileHandler;
import utility.Strings;
import logic.*;
import org.codehaus.jettison.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Gila-Ber on 04/05/2016.
 */
public class hAPPiFacadeTest {

    hAPPiFacade facade = new hAPPiFacade();
    Database dataBase = facade.getDataBase();
    Application app ;

    @Before
    public void setup() throws IOException, InvalidUserCredentialsException {
        facade.connectToDatabase();
        // clear the database
        dataBase.clearAll();

        // clear the applications directory
        FileHandler.deleteFolder(Strings.PATH_APPS);
        FileHandler.createFolder(Strings.PATH_APPS);

        //create user
        User user = new User("username", "username", "username@test");
        facade.addUser(user);

        //create app
        ArrayList<String> platforms = new ArrayList<String>();
        platforms.add("android");
        app = new Application("appId", "testApp", user.getUsername(), platforms, new ArrayList<ApplicationObject>(),
                new ArrayList<ApplicationBehavior>(), new ArrayList<ApplicationEvent>());
    }

    @Test
    public void testCreateApplication() throws Exception {
        facade.createApplication(app);
        assertTrue(FileHandler.isFileExist(Strings.PATH_APPS + "\\" + app.getUsername() + "\\" + app.getName()));
        // validate platforms
        assertTrue(FileHandler.isFileExist(Strings.PATH_APPS + "\\" + app.getUsername() + "\\" + app.getName()
                + "\\platforms\\android"));

        // validate application in DB
        Application applicationFromDB = dataBase.getApplication(app.getId());
        validateApplicationResults(app, applicationFromDB);
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
        assertTrue(FileHandler.isFileExist(Strings.PATH_APPS + "\\" + app.getUsername() + "\\" + app.getName()));
        // validate platforms
        assertFalse(FileHandler.isFileExist(Strings.PATH_APPS + "\\" + app.getUsername() + "\\" + appOldName));
        assertFalse(FileHandler.isFileExist(Strings.PATH_APPS + "\\" + app.getUsername() + "\\" + app.getName()
                + "\\platforms\\android"));
        // validate application in DB
        Application applicationFromDB = dataBase.getApplication(app.getId());
        validateApplicationResults(app, applicationFromDB);
    }

    @Test
    public void testRemoveApplication() throws Exception {
        facade.createApplication(app);
        assertTrue(FileHandler.isFileExist(Strings.PATH_APPS + "\\" + app.getUsername() + "\\" + app.getName()));
        facade.removeApplication(app.getId(), app.getUsername());
        assertFalse(FileHandler.isFileExist(Strings.PATH_APPS + "\\" + app.getUsername() + "\\" + app.getName()));
        // validate app removed from DB
        boolean appExist = true;
        try{
            dataBase.getApplication(app.getId());
        } catch (Exception e){
            appExist = false;
        }
        assertFalse(appExist);
    }

    @Test
    public void testRemovePlatforms() throws Exception {
        facade.createApplication(app);
        assertTrue(FileHandler.isFileExist(Strings.PATH_APPS + "\\" + app.getUsername() + "\\" + app.getName()
                + "\\platforms\\android"));
        facade.removePlatforms(app.getName(), app.getUsername());
        assertFalse(FileHandler.isFileExist(Strings.PATH_APPS + "\\" + app.getUsername() + "\\" + app.getName()
                + "\\platforms\\android"));
    }

    @Test
    public void testUpdateApplicationWithNameAlreadyExist() throws CordovaRuntimeException, JSONException {
        String appOldName = app.getName();
        facade.createApplication(app);
        app.setName("newAppName");
        app.setId("newAppId");
        facade.createApplication(app);

        boolean updateFailed = false;
        app.setName(appOldName);
        ArrayList<String> platforms = new ArrayList<String>();
        app.setPlatforms(platforms);

        try{
            facade.updateApplication(app, app.getUsername());
        }catch(Exception e) {
            updateFailed = true;
        }
        assertTrue(updateFailed);
    }

    // from here i need to add to ADD file

    @Test
    public void testRemoveNotExistApplication () {
        //TODO
    }

    @Test
    public void testUpdateNotExistApplication () {
        //TODO
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

    private void validateApplicationResults(Application expectedApp, Application actualApp) {
        assertEquals(expectedApp.getName(), actualApp.getName());
        assertEquals(expectedApp.getPlatforms(), actualApp.getPlatforms());
        assertEquals(expectedApp.getObjects(), actualApp.getObjects());
        assertEquals(expectedApp.getBehaviors(), actualApp.getBehaviors());
        assertEquals(expectedApp.getEvents(), actualApp.getEvents());
    }
}