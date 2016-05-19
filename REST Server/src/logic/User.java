package logic;

import org.bson.Document;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Gila on 21/03/2016.
 */
@SuppressWarnings("unused")
@XmlRootElement
public class User extends Document {
    @XmlElement(required=true)
    private String username;
    @XmlElement(required=true)
    private String password;
    @XmlElement(required=true)
    private String email;

    @JsonCreator
    public User(@JsonProperty("username") String username,
                @JsonProperty("password") String password,
                @JsonProperty("email") String email){
        super();
        this.append("username", username);
        this.append("password", password);
        this.append("email", email);

        this.username = username;
        this.password = password;
        this.email = email;
    }


    public String getUsername() {
        return this.getString("username");
    }

    public void setUsername(String name) {
        this.put("username", username);
    }

    public String getPassword() {
        return this.getString("password");
    }

    public void setPassword(String pass) {
        this.put("password", password);
    }

    public String getEmail() {
        return this.getString("email");
    }

    public void setEmail(String name) {
        this.put("email", email);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("username", this.getUsername());
        json.put("password", this.getPassword());
        json.put("email", email);
        return json;
    }

    public String toString(){
        String result = "Users: ";
        result += "username  = " + username + "\n";
        result += "password = " + password + "\n";
        result += "email = " + email + "\n";
        return result;
    }

    public static User fromDocument(Document doc){
        return new User(doc.getString("username"),doc.getString("password"),doc.getString("email"));
    }
}
