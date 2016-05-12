package Tests.UnitTests;

import Logic.User;
import org.bson.Document;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Gila-Ber on 04/05/2016.
 */
public class UserTest {

    @Test
    public void testFromDocument() throws Exception {
        Document doc = new Document();
        doc.append("username", "testUser");
        doc.append("password", "123456");
        doc.append("email", "testUser@gmail.com");

        User user = User.fromDocument(doc);
        assertEquals("testUser", user.getUsername());
        assertEquals("123456", user.getPassword());
        assertEquals("testUser@gmail.com", user.getEmail());
    }
}