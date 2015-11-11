package Logic;

/**
 * Created by victor on 11/10/2015.
 *
 */
public interface Facade {

    void createProject(String project);

    void addAndroidToProject(String project);
    void addIOSToProject(String project);
    void addWindowsPhoneToProject(String project);

    void buildProject(String project);

    void createEntity(String project, String entity);

    void connectToDatabase();
}
