package Util;

import org.codehaus.jettison.json.JSONObject;

/**
 * Created by victor on 11/11/2015.
 */
public interface AppCompiler {

    void createProject(JSONObject name);
    void addAndroidModule(JSONObject project);
    void addIOSModule(JSONObject project);
    void addWindowsPhoneModule(JSONObject project);
    void buildProject(JSONObject project);
}
