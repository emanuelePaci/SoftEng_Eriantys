package it.polimi.ingsw.client.gui.scene;

import java.io.IOException;

import it.polimi.ingsw.client.viewUtilities.GameInfo;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

/**
 * Scene controller class
 */
public class SceneController {

    private static Scene activeScene;
    private static GenericSceneController activeController;

    /**
     * Sets the active scene based on the event that occurred
     * @param event the event that occurred
     */
    public static void setActiveScene(Event event){
        activeScene = ((Node) event.getSource()).getScene();

    }

    /**
     * Changes the root pane
     * @param controller the scene controller
     * @param fxml the new pane path
     */
    public static void changeRootPane(GenericSceneController controller, String fxml) {
        changeRootPane(controller, activeScene, fxml);
    }

    /**
     * changes the root pane
     * @param controller the scene controller
     * @param scene the active scene
     * @param fxml the new pane path
     */
    public static void changeRootPane(GenericSceneController controller, Scene scene, String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/fxml/" + fxml));
            loader.setController(controller);
            activeController = controller;
            Parent root = loader.load();
            activeScene = scene;
            activeScene.setRoot(root);
        } catch (IOException e) {
        }
    }

    /**
     * Shows the error popup
     * @param type the type of error
     * @param message the error message
     */
    public static void showError(String type, String message) {
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/fxml/errorScene.fxml"));

        Parent parent = null;
        try {
            parent = loader.load();
        } catch (IOException e) {
        }

        ErrorSceneController controller = loader.getController();
        Scene alertScene = new Scene(parent);
        controller.setScene(alertScene);
        controller.setErrorType(type);
        controller.setErroreMessage(message);
        controller.displayAlert();
    }

    /**
     * Shows the character
     * @param mainController the main controller
     * @param controller the character scene controller
     * @param gameInfo the game info
     * @param number the character number
     */
    public static void showCharacter(MainController mainController, CharacterSceneController controller, GameInfo gameInfo, int number) {
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/fxml/character.fxml"));
        loader.setController(controller);

        Parent parent = null;
        try {
            parent = loader.load();
        } catch (IOException e) {
        }
        Scene alertScene = new Scene(parent);
        controller.setScene(alertScene);
        controller.setMain(mainController);
        controller.setInfo(gameInfo, number);
        controller.displayAlert();
    }

    /**
     * Returns the active scene
     * @return the active scene
     */
    public static Scene getActiveScene() {
        return activeScene;
    }

    /**
     * Returns the active controller
     * @return the active controller
     */
    public static Object getActiveController() {
        return activeController;
    }
}