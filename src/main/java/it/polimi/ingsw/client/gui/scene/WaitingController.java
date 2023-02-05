package it.polimi.ingsw.client.gui.scene;

import it.polimi.ingsw.client.gui.JavaFXInit;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * Waiting controller class
 */
public class WaitingController implements GenericSceneController{

    @FXML
    private ImageView image1, image2, image3, image4;
    @FXML
    private ProgressBar bar;
    @FXML
    private Pane music;

    /**
     * Initializes the controller that manages the client waiting for other clients to join its lobby
     */
    @FXML
    public void initialize(){
        effect(image1);
        effect(image2);
        effect(image3);
        effect(image4);
        if (JavaFXInit.isMuted()){
            music.getStyleClass().clear();
            music.getStyleClass().add("noAudio");
        }
        JavaFXInit.musicEffect(music);
    }

    /**
     * Shows the waiting transition
     * @param node the node
     */
    private void effect(Node node){
        TranslateTransition translate = new TranslateTransition();
        translate.setNode(node);
        translate.setCycleCount(TranslateTransition.INDEFINITE);
        translate.setDuration(Duration.millis(20000));
        translate.setByX(390);
        translate.setAutoReverse(true);
        translate.play();

        RotateTransition rotation = new RotateTransition();
        rotation.setNode(node);
        rotation.setCycleCount(RotateTransition.INDEFINITE);
        rotation.setDuration(Duration.millis(1000));
        rotation.setFromAngle(-10);
        rotation.setToAngle(10);
        rotation.setAutoReverse(true);
        rotation.play();
    }
}
