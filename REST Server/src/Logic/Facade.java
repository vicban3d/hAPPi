package Logic;

/**
 * Created by victor on 11/10/2015.
 *
 */
public interface Facade {

    void createProject(String project);

    void createEntity(String project, String entity);

    void connectToDatabase();
}
