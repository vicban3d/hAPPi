package Server;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Gila on 12/04/2016.
 */
public class RemoveObjInstanceRequest {
    @XmlElement(required = true)
    String instanceId;

    @XmlElement(required = true)
    String objName;

    @XmlElement(required = true)
    int index;

    public RemoveObjInstanceRequest(@JsonProperty("id")String instanceId,
                                    @JsonProperty("obj_name")String objName,
                                    @JsonProperty("index")int index) {
        this.instanceId = instanceId;
        this.objName = objName;
        this.index = index;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


}
