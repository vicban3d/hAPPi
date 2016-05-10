package Logic;

import org.bson.Document;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gila on 30/03/2016.
 */
public class AppInstance extends Document{
    // instance id - id per instance
    @XmlElement(required=true)
    private String id;

    // the app id
    @XmlElement(required = true)
    private String app_id;

    // map between object and his attributes
    @XmlElement(required = true)
    private Map<String, List<String>> objectInstances;

    @JsonCreator
    public AppInstance(@JsonProperty("id")String id,
                       @JsonProperty("app_id")String app_id,
                       @JsonProperty("object_map")Map<String, List<String>> objectInstances){
        super();


        this.id = id;
        this.app_id = app_id;
        this.objectInstances = objectInstances;

        this.append("id",id);
        this.append("app_id",app_id);
        this.append("object_map",objectInstances);

    }

    public String getId() {
        return this.getString("id");
    }

    public void setId(String id) {
        this.put("id",id);
    }

    public String getApp_id() {
        return this.getString("app_id");
    }

    public void setApp_id(String app_id) {
        this.put("app_id",app_id);
    }

    public Map<String, List<String>> getObjectInstances() {
        return (Map)this.get("object_map");
    }

    public void setObjectInstances(HashMap<String, List<String>> objectInstances) {
        this.put("object_map",objectInstances);
    }

    public String toString(){
        Application app = getApp(id);
        return "Application name = " + app.getName();
    }

    public static AppInstance fromDocument(Document data) {
        return new AppInstance(data.getString("id"),data.getString("app_id"),(Map)data.get("object_map"));
    }

    public void addObjectInstance(String objName, List<String> attributes){
        String objNameTmp = objName;
        if (objectInstances.get(objNameTmp)!=null)
        {
            int i=1;
            while (objectInstances.get(objNameTmp)!=null){
                objNameTmp = objName + "_" +String.valueOf(i);
                i++;
            }

        }
        objectInstances.put(objNameTmp,attributes);
    }

    public void removeObjectInstance(String objName,int index){
        String realObjname = objName;
        if (index>0){
            realObjname += "_" + String.valueOf(index);
        }
        objectInstances.remove(realObjname);
    }

    private static Application getApp(String id){
        return null;
    }
}
