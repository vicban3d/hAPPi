package Util;

import org.codehaus.jettison.json.JSONObject;

/**
 * Created by victor on 11/11/2015.
 */
public interface AppCompiler {

    void createProject(JSONObject name);
    void addAndroidModule();
    void addIOSModule();
    void addWindowsPhoneModule();
}
