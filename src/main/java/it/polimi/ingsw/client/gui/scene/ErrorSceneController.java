package it.polimi.ingsw.client.gui.scene;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Error scene controller
 */
public class ErrorSceneController implements GenericSceneController{

    private final Stage stage;

    private double xOffset;
    private double yOffset;

    @FXML
    private StackPane rootPane;
    @FXML
    private TextArea message;
    @FXML
    private Label type;
    @FXML
    private Button button;

    /**
     * Initialize the button click event and the drag event for the root pane
     */
    @FXML
    public void initialize() {
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            stage.close();
            if (type.getText().equals("SERVER DISCONNECTED"))
                System.exit(0);
        });
        rootPane.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onRootPaneMousePressed);
        rootPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onRootPaneMouseDragged);
    }

    /**
     * Constructor. Sets the stage
     */
    public ErrorSceneController() {
        stage = new Stage();
        stage.initOwner(SceneController.getActiveScene().getWindow());
        stage.initModality(Modality.NONE);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setAlwaysOnTop(true);
        xOffset = 0;
        yOffset = 0;
    }

    /**
     * Displays an alert and wait for another event
     */
    public void displayAlert() {
        stage.showAndWait();
    }

    /**
     * Sets the specified scene
     * @param scene the scene to set
     */
    public void setScene(Scene scene) {
        stage.setScene(scene);
    }

    private void onRootPaneMousePressed(MouseEvent event) {
        xOffset = stage.getX() - event.getScreenX();
        yOffset = stage.getY() - event.getScreenY();
    }

    private void onRootPaneMouseDragged(MouseEvent event) {
        stage.setX(event.getScreenX() + xOffset);
        stage.setY(event.getScreenY() + yOffset);
    }

    /**
     * Sets the type of the error with the one specified
     * @param type the type of the error
     */
    public void setErrorType(String type) {
        this.type.setText(type);
        FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
        double max = 0;
        for (int i = 0; i < this.type.getText().length(); i++){
            max += fontLoader.getCharWidth(this.type.getText().charAt(i), this.type.getFont());
        }
        this.type.setLayoutX(250 - max/2);
    }

    /**
     * Sets the text to display with the one specified
     * @param message the text of the error
     */
    public void setErroreMessage(String message) {
        this.message.setText(message);
    }
}
