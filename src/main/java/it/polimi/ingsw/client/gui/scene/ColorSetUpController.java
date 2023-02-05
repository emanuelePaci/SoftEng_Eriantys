package it.polimi.ingsw.client.gui.scene;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.model.enumerations.TowerColor;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * Color setup controller
 */
public class ColorSetUpController implements GenericSceneController{

    private ServerHandler serverHandler;
    @FXML
    private Pane tower1, tower2, tower3;

    /**
     * Initialize the events on the three towers
     */
    @FXML
    public void initialize(){
        event(tower1);
        event(tower2);
        event(tower3);
    }
    /**
     * Constructor
     * Initializes the server handler with it is associated
     * @param serverHandler the server handler to associate
     */
    public ColorSetUpController(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

    /**
     * Sets the mouse entered, exited and clicked events
     * @param pane the pane
     */
    public void event(Pane pane){
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(20.0);
        dropShadow.setHeight(40.0);
        dropShadow.setWidth(40.0);
        dropShadow.setBlurType(BlurType.GAUSSIAN);
        Color paint = new Color(0.2763, 0.5559, 0.9474, 1.0);
        dropShadow.setColor(paint);
        pane.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> {
            pane.setEffect(dropShadow);
        });
        pane.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> {
            pane.setEffect(null);
        });
        pane.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            serverHandler.setPlayerColor(TowerColor.getColor(pane.getStyleClass().get(0).toUpperCase()));
            Platform.runLater(() -> SceneController.changeRootPane(new WaitingController(), "waiting.fxml"));
        });
    }

    /**
     * Setsthe towers position
     * @param tower the list of towers
     */
    public void setUp(List<TowerColor> tower) {
        Pane[] colors = {tower1, tower2, tower3};
        tower1.setVisible(false);
        tower2.setVisible(false);
        tower3.setVisible(false);
        for (int i = 0; i < tower.size(); i++) {
            colors[i].setVisible(true);
            colors[i].getStyleClass().clear();
            colors[i].getStyleClass().add(tower.get(i).toString().toLowerCase());
        }
        if (tower.size() == 1)
            tower1.setLayoutX(250.0);
        else if (tower.size() == 2){
            tower1.setLayoutX(175.0);
            tower2.setLayoutX(325.0);
        } else {
            tower1.setLayoutX(100);
            tower2.setLayoutX(250);
            tower3.setLayoutX(400);
        }
    }
}
