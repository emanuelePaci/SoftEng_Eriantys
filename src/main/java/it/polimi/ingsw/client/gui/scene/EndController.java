package it.polimi.ingsw.client.gui.scene;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import it.polimi.ingsw.client.gui.JavaFXInit;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;


/**
 * End controller class
 */
public class EndController implements GenericSceneController{

    @FXML
    private Button yesBtn, noBtn;
    @FXML
    private Text winner, draw;
    @FXML
    private ImageView newGame, yes, no;

    /**
     * Initialize the mouse clicked event
     */
    @FXML
    public void initialize(){
        yesBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            JavaFXInit.setStopped(true);
            try {
                JavaFXInit.getPlayer().stop();
            } catch (NullPointerException e){
            }
            JavaFXInit.setStopped(false);
            JavaFXInit.getStage().close();
            Platform.runLater(JavaFXInit::reStart);
        });
        noBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            System.exit(0);
        });
    }

    /**
     * Displays the winner(s) of the game
     * @param winner1 the nickname of the winner
     * @param winner2 the nickname of the eventual second winner in case of draw. It may be null if there is only one winner
     * @param nickname the nickname of the client
     */
    public void winner(String winner1, String winner2, String nickname){
        winner.setVisible(true);
        if(winner2==null){
            if(winner1.equals(nickname))
                winner.setText("YOU HAVE WON!!!");
            else
                winner.setText(winner1+" HAS WON");
        }
        else{
            String won = " have won the game";
            draw.setVisible(true);
            if(winner1.equals(nickname))
                winner.setText("You and " +winner2+ won);
            else if(winner2.equals(nickname))
                winner.setText("You and " +winner1+ won);
            else
                winner.setText(winner1 +" and " +winner2+ won);
        }

        FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
        double max = 0;
        for (int i = 0; i < winner.getText().length(); i++){
            max += fontLoader.getCharWidth(winner.getText().charAt(i), winner.getFont());
        }
        winner.setLayoutX((1280 - max)/2);
    }

    /**
     * Displays the text "Would you like to start a new game?" and the button Yes and the button and the button No
     */
    public void newGame(){
        yesBtn.setVisible(true);
        noBtn.setVisible(true);
        newGame.setVisible(true);
        yes.setVisible(true);
        no.setVisible(true);
    }
}
