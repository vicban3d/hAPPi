package Logic;

/**
 * Created by victor on 11/10/2015.
 *
 */

/**
 * A facade interface which allows the connection layer to communicate with the business layer.
 */
public interface Facade {

    /**
     * Creates a new project with the given parameters.
     * @param project - a JSON compatible string describing the project.
     */
    void createProject(String project);

    /**
     * Adds an android component to the given project.
     * @param project - the name of the project.
     */
    void addAndroidToProject(String project);

    /**
     * Adds an ios component to the given project.
     * @param project - the name of the project.
     */
    void addIOSToProject(String project);

    /**
     * Adds a Windows Phone component to the given project.
     * @param project - the name of the project.
     */
    void addWindowsPhoneToProject(String project);

    /**
     * Compiles and builds the project.
     * @param projectName
     */
    void buildProject(String projectName);

    /**
     * Creates a new entity in the given project.
     * @param project - the name of the project.
     * @param entity - the JSON compatible parameters of the entity.
     */
    void createEntity(String project, String entity);

    /**
     * Initiates a connection to the database.
     */
    void connectToDatabase();
}
