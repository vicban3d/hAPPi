package Database;

/**
 * Created by victor on 11/12/2015.
 */
public interface Database {
    void connect();

    void addData(String projectName, String categoryName, String data);

    String getData(String projectName, String categoryName, String data);
}
