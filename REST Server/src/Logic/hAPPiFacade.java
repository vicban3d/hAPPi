package Logic;

import Database.Database;
import Database.MongoDB;
import Exceptions.CordovaRuntimeException;
import Utility.*;
import com.dropbox.core.DbxException;
import org.bson.Document;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by victor on 11/10/2015.
 *
 */
public class hAPPiFacade implements Facade {

    private final Database database;
    private final AppCompiler compiler;
    private final FileUploader fileUploader;

    /**
     * Constructor and initiator for the Facade.
     */
    public  hAPPiFacade(){
        database = new MongoDB();
        compiler = new CordovaAppCompiler();
        fileUploader = new DropboxAPI();
    }

    @Override
    public void createApplication(Application application) throws CordovaRuntimeException, JSONException {
            database.addApplication(application);
            compiler.createApplication(application.toJSON());
            createPlatforms(application.getPlatforms(), application.getName());
    }

    @Override
    public void updateApplication(Application application, String username) throws IOException, CordovaRuntimeException {
        Application oldApp = Application.fromDocument(database.getApplication(application.getId()));
        String oldApplicationName = oldApp.getName();
        removePlatforms(oldApplicationName);
        FileHandler.renameFolder(Strings.PATH_APPS + "\\" + oldApplicationName, Strings.PATH_APPS + "\\" + application.getName());
        createPlatforms(application.getPlatforms(), application.getName());
        database.updateApplication(application);
    }

    private void createPlatforms(ArrayList<String> platforms, String applicationName) throws CordovaRuntimeException {
        if(platforms == null || platforms.isEmpty()) {
            return;
        }
        for (String platform : platforms){
            compiler.addPlatform(applicationName, platform);
        }
    }

    @Override
    public String buildApplication(String appId, String username) throws CordovaRuntimeException, IOException, DbxException {
        Application application = Application.fromDocument(database.getApplication(appId));
        prepareApplicationForCompilation(application);
        compiler.buildApplication(application.getName());
        return fileUploader.uploadFile(application.getName(), Strings.PATH_APPS + "/" + application.getName() + "/platforms/android/build/outputs/apk/android-debug.apk");

    }

    private void prepareApplicationForCompilation(Application application) throws IOException {
        String name = application.getName();
        ArrayList<ApplicationObject> objects = application.getObjects();
        ArrayList<ApplicationBehavior> behaviors = application.getBehaviors();
        ArrayList<String> allActions = new ArrayList<>();
        for (Document obj : objects){
            allActions.add(obj.toJson());
        }
        ArrayList<String> allBehaviors = new ArrayList<>();
        for (Document beh : behaviors){
            allBehaviors.add(beh.toJson());
        }

        FileHandler.clearFile(Strings.PATH_APPS + "\\" + name + "\\www\\index.html");
        FileHandler.clearFile(Strings.PATH_APPS + "\\" + name + "\\www\\js\\index.js");
        FileHandler.clearFile(Strings.PATH_APPS + "\\" + name + "\\www\\css\\index.css");

        if (Files.exists(Paths.get(Strings.PATH_WEB_CONTENT + "\\model\\main.html"))){
            Files.delete(Paths.get(Strings.PATH_WEB_CONTENT + "\\model\\main.html"));
        }

        FileInputStream stream = new FileInputStream(Strings.PATH_WEB_CONTENT + "\\index.html");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        FileOutputStream out = new FileOutputStream(Strings.PATH_WEB_CONTENT + "\\model\\main.html");
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));

        String strLine;
        boolean startWriting = false;

        writer.write("<!DOCTYPE html>\n" +
                "<!--suppress ALL -->\n" +
                "<html>\n" +
                "<head>\n" +
                "    <script type=\"text/javascript\" src=\"js/jquery-1.10.2.min.js\"></script>\n" +
                "    <script type=\"text/javascript\" src=\"js/bootstrap.min.js\"></script>\n" +
                "    <script type=\"text/javascript\" src=\"js/angular.min.js\"></script>\n" +
                "    <script type=\"text/javascript\" src=\"js/index.js\"></script>\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"css/index.css\">\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"css/bootstrap.min.css\">\n" +
                "</head>\n" +
                "<body data-ng-app=\"main\" data-ng-controller=\"ctrl_main\">\n");
        while ((strLine = reader.readLine()) != null){

            if (strLine.contains("<!--<PHONE_CODE_START>-->")){
                startWriting = true;
                continue;
            }
            if (strLine.contains("<!--<PHONE_CODE_END>-->")){
                break;
            }
            if (startWriting){
                writer.write(strLine + "\n");
            }
        }
        writer.write("</body></html>");
        writer.close();
        reader.close();
        stream.close();
        out.close();

        // model files
        String HTMLContent = FileHandler.readFile(Strings.PATH_WEB_CONTENT + "\\model\\index.html");
        String MAINContent = FileHandler.readFile(Strings.PATH_WEB_CONTENT + "\\model\\main.html");
        String JSContent = FileHandler.readFile(Strings.PATH_WEB_CONTENT + "\\model\\index.js");
        String JSUtilContent = FileHandler.readFile(Strings.PATH_WEB_CONTENT + "\\model\\util.js");
        String CSSContent = FileHandler.readFile(Strings.PATH_WEB_CONTENT + "\\model\\index.css");
        // lib files
        String ANGULARContent = FileHandler.readFile(Strings.PATH_WEB_CONTENT + "\\lib\\angular.min.js");
        String JQUERYContent = FileHandler.readFile(Strings.PATH_WEB_CONTENT + "\\lib\\jquery-1.10.2.min.js");
        String BOOTSTRAPCSSContent = FileHandler.readFile(Strings.PATH_WEB_CONTENT + "\\lib\\bootstrap.min.css");
        String BOOTSTRAPJSContent = FileHandler.readFile(Strings.PATH_WEB_CONTENT + "\\lib\\bootstrap.min.js");

        if (HTMLContent != null && JSContent != null && CSSContent != null) {
            JSContent = JSContent.replace("<[NAME]>", application.getName());
            JSContent = JSContent.replace("<[APP_ID]>", application.getId());
            JSContent = JSContent.replace("<[OBJECTS]>", allActions.toString().replace("\"", "\\\""));
            JSContent = JSContent.replace("<[BEHAVIORS]>", allBehaviors.toString().replace("\"", "\\\""));

            FileHandler.writeFile(Strings.PATH_APPS + "\\" + name + "\\www\\index.html", HTMLContent);
            FileHandler.writeFile(Strings.PATH_APPS + "\\" + name + "\\www\\main.html", MAINContent);
            FileHandler.writeFile(Strings.PATH_APPS + "\\" + name + "\\www\\js\\index.js", JSContent);
            FileHandler.writeFile(Strings.PATH_APPS + "\\" + name + "\\www\\js\\util.js", JSUtilContent);
            FileHandler.writeFile(Strings.PATH_APPS + "\\" + name + "\\www\\js\\angular.min.js", ANGULARContent);
            FileHandler.writeFile(Strings.PATH_APPS + "\\" + name + "\\www\\css\\index.css", CSSContent);
            FileHandler.writeFile(Strings.PATH_APPS + "\\" + name + "\\www\\js\\jquery-1.10.2.min.js", JQUERYContent);
            FileHandler.writeFile(Strings.PATH_APPS + "\\" + name + "\\www\\css\\bootstrap.min.css", BOOTSTRAPCSSContent);
            FileHandler.writeFile(Strings.PATH_APPS + "\\" + name + "\\www\\js\\bootstrap.min.js", BOOTSTRAPJSContent);
        }
    }

    @Override
    public void createObject(String appId, String username, ApplicationObject data) {
        Application application = Application.fromDocument(database.getApplication(appId));
        application.addObject(data);
        database.updateApplication(application);
    }

    @Override
    public void removeObject(String appId, String username, ApplicationObject object) {
        Application application = Application.fromDocument(database.getApplication(appId));
        application.removeObject(object);
        database.updateApplication(application);
    }

    @Override
    public void connectToDatabase() throws IOException {
        database.connect();
    }

    @Override
    public void clearDatabase() {
        database.clearAll();
    }

    @Override
    public void removeApplication(String appId, String username) {
        String appName = Application.fromDocument(database.getApplication(appId)).getName();
        database.removeApplication(appId);
        FileHandler.deleteFolder(Strings.PATH_APPS + "\\" + appName);
        Logger.DEBUG("The application " + appName + " was deleted.");
    }

    @Override
    public void removePlatforms(String fileName){
        FileHandler.deleteFolder(Strings.PATH_APPS + "\\" + fileName+ "\\platforms");
        FileHandler.createFolder(Strings.PATH_APPS + "\\" + fileName + "\\platforms");
    }

    @Override
    public void createBehavior(String appId, String username, ApplicationBehavior behavior) {
        Application application = Application.fromDocument(database.getApplication(appId));
        application.addBehavior(behavior);
        database.updateApplication(application);
    }

    @Override
    public void removeBehavior(String appId, String username, ApplicationBehavior behavior) {
        Application application = Application.fromDocument(database.getApplication(appId));
        application.removeBehavior(behavior);
        database.updateApplication(application);
    }

    @Override
    public Database getDataBase(){
        return database;
    }

    @Override
    public String getPage(String page) {
        return FileHandler.readFile(Strings.PATH_WEB_CONTENT + page);
    }

    @Override
    public void updateApplicationObject(String appId, String username, ApplicationObject object) {
        Application application = Application.fromDocument(database.getApplication(appId));
        application.updateObject(object);
        database.updateApplication(application);
    }

    @Override
    public void updateApplicationBehavior(String appId, String username, ApplicationBehavior behavior) {
        Application application = Application.fromDocument(database.getApplication(appId));
        application.updateBehavior(behavior);
        database.updateApplication(application);
    }

    @Override
    public void addUser(User user) {
        database.addUser(user);
    }

    @Override
    public void addObjectInstance(JSONObject jsonObj) {
        String id = null;
        String app_id = null;
        String objName = null;
        List<String> attributes = new ArrayList<>();
        try {
            id = jsonObj.getString("id");
            app_id = jsonObj.getString("app_id");
            objName = jsonObj.getString("objName");
            JSONArray jsonArray = (JSONArray)jsonObj.get("attributesList");
            if (jsonArray!=null){
                for (int i=0;i<jsonArray.length();i++)
                    attributes.add(jsonArray.getString(i));
            }
        } catch (org.codehaus.jettison.json.JSONException e) {
            e.printStackTrace();
        }
        Boolean isAppInstanceExist = database.isInstanceExist(id, app_id);
        if (!isAppInstanceExist){
            Map<String,List<List<String>>> newMap = new HashMap<>();
            List<List<String>> list = new ArrayList<>();
            list.add(attributes);
            newMap.put(objName,list);
            AppInstance appInstance = new AppInstance(id,app_id,newMap);
            database.addApplicationInstance(appInstance);
        }else{
            AppInstance appInstance = database.getAppInstance(id, app_id);
            appInstance.addObjectInstance(objName, attributes);
            database.updateAppInstance(appInstance);
        }
    }

    @Override
    public void removeObjectInstance(JSONObject jsonObj) {
        String instanceId = null;
        String app_id = null;
        String objName = null;
        int index = 0;

        try {
            instanceId = jsonObj.getString("id");
            app_id = jsonObj.getString("app_id");
            objName = jsonObj.getString("objName");
            index = jsonObj.getInt("index");
        } catch (org.codehaus.jettison.json.JSONException e) {
            e.printStackTrace();
        }


        AppInstance instance = database.getAppInstance(instanceId, app_id);
        instance.removeObjectInstance(objName,index);
        database.updateAppInstance(instance);
    }

    @Override
    public AppInstance getObjectInstance(JSONObject jsonObj) {
        String instanceId = null;
        String app_id = null;

        try {
            instanceId = jsonObj.getString("id");
            app_id = jsonObj.getString("app_id");

        } catch (org.codehaus.jettison.json.JSONException e) {
            e.printStackTrace();
        }

        return getDataBase().getAppInstance(instanceId,app_id);
    }

    @Override
    public byte[] getImageAsBytes(String resource) {
        if (resource == null || resource.charAt(0) == '{'){
            return null;
        }
        try {
            BufferedImage image = ImageIO.read(new File(Strings.PATH_WEB_CONTENT + "img\\" + resource));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void createEvent(String id, String username, ApplicationEvent event) {
        Application application = Application.fromDocument(database.getApplication(id));
        application.addEvent(event);
        database.updateApplication(application);
    }

    @Override
    public void removeEvent(String appId, String username, ApplicationEvent event) {
        Application application = Application.fromDocument(database.getApplication(appId));
        application.removeEvent(event);
        database.updateApplication(application);
    }

    @Override
    public void updateApplicationEvent(String appId, String username, ApplicationEvent event) {
        Application application = Application.fromDocument(database.getApplication(appId));
        application.updateEvent(event);
        database.updateApplication(application);
    }

    @Override
    public List<Application> login(User user) {
        if(!database.isUserExist(user.getUsername()) ||
                !database.isPasswordRight(user.getUsername(),user.getPassword())){
            return null;
        }
        else {
            return database.getApplicationOfUser(user.getUsername());
        }
    }
}
