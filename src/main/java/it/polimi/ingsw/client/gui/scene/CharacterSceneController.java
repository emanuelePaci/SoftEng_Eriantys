package it.polimi.ingsw.client.gui.scene;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.viewUtilities.GameInfo;
import it.polimi.ingsw.model.enumerations.CharacterType;
import it.polimi.ingsw.model.enumerations.PawnColor;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CharacterSceneController implements GenericSceneController {

    private final Stage stage;

    private double xOffset;
    private double yOffset;
    private GameInfo gameInfo;
    private int number, islandSelected;
    private boolean playable = false;
    private CharacterType characterType;
    private ServerHandler serverHandler;
    private MainController mainController;
    private String color;
    private int[] colorPresent = {0,0,0,0,0,0,0,0,0,0};
    private int[] colorSect = {-1,-1,-1,-1,-1,-1};
    private List<String> color1 = new ArrayList<>();
    private List<String> color2 = new ArrayList<>();
    @FXML
    private StackPane rootPane;
    @FXML
    private Button close, play;
    @FXML
    private Text request, characterName, ok;
    @FXML
    private TextArea description;
    @FXML
    private ImageView coin;
    @FXML
    private Pane character;
    @FXML
    private ListView<String> colors;
    @FXML
    private ChoiceBox<String> choice01, choice02, choice03, choice11, choice12, choice13;
    @FXML
    private Label label0, label1, label2, label3, label4, label5;
    private List<ChoiceBox<String>> selection;

    /**
     * Initialize the mouse pressed, dragged and clicked events
     */
    @FXML
    public void initialize() {
        selection = new ArrayList<>();
        Collections.addAll(selection, choice01, choice02, choice03, choice11, choice12, choice13);
        rootPane.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onRootPaneMousePressed);
        rootPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onRootPaneMouseDragged);
        close.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            Platform.runLater(() -> mainController.update());
            stage.close();
        });
        play.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onPlayClicked);
        for (ChoiceBox<String> choice : selection)
            choiceBoxEffect(choice);
    }

    private void choiceBoxEffect(ChoiceBox<String> choiceBox){
        Label[] labels = {label0, label1, label2, label3, label4, label5};
        choiceBox.setOnAction(event -> {
            if (choiceBox.getValue() != null) {
                colorSect[Integer.parseInt(choiceBox.getId().substring(7)) - (Integer.parseInt(choiceBox.getId().substring(6, 7)) == 0 ? 1 : -2)] = PawnColor.lookup(choiceBox.getValue()).getIndex();
                labels[Integer.parseInt(choiceBox.getId().substring(7)) - (Integer.parseInt(choiceBox.getId().substring(6, 7)) == 0 ? 1 : -2)].setText(PawnColor.lookup(choiceBox.getValue()).toString());
                updateChoiceBox(characterType.equals(CharacterType.MINSTREL) ? 2 : 3);
            }
        });
    }

    private <T extends Event> void onPlayClicked(T t) {
        int sum1 = 0;
        int sum2=0;
        int[] colorListFinal = new int[(characterType.equals(CharacterType.MINSTREL) ? 4 : 6)];
        for (int i = 0; i < (characterType.equals(CharacterType.MINSTREL) ? 2 : 3); i++){
            if (colorSect[i] != -1)
                sum1++;
            if (colorSect[i+3] != -1)
                sum2++;
            colorListFinal[i] = colorSect[i];
            colorListFinal[i+(characterType.equals(CharacterType.MINSTREL) ? 2 : 3)] = colorSect[i+3];
        }

        boolean close = true;

        if (characterType.equals(CharacterType.FARMER) || characterType.equals(CharacterType.MAGIC_DELIVERY_MAN) || characterType.equals(CharacterType.CENTAUR) || characterType.equals(CharacterType.KNIGHT))
            serverHandler.useCharacterRequest(number);
        else if ((characterType.equals(CharacterType.HERALD) || characterType.equals(CharacterType.GRANDMOTHER_HERBS))&& islandSelected!=-1)
            serverHandler.useCharacterRequest(islandSelected, number);
        else if ((characterType.equals(CharacterType.MONK)) && islandSelected!= -1 && color != null)
            serverHandler.useCharacterRequest(islandSelected, PawnColor.lookup(color), number);
        else if (((characterType.equals(CharacterType.SPOILED_PRINCESS)) && color!= null) || ((characterType.equals(CharacterType.MUSHROOM_HUNTER) || characterType.equals(CharacterType.THIEF)) && colors.getSelectionModel().getSelectedItem() != null))
            serverHandler.useCharacterRequest(color != null ? PawnColor.lookup(color) : PawnColor.lookup(colors.getSelectionModel().getSelectedItem()) , number);
        else if ((characterType.equals(CharacterType.MINSTREL) || (characterType.equals(CharacterType.JESTER))) && sum1 == sum2 && sum1 > 0)
            serverHandler.useCharacterRequest(colorListFinal, number);
        else if ((characterType.equals(CharacterType.MINSTREL) || (characterType.equals(CharacterType.JESTER))) && sum1 != sum2){
            Platform.runLater(() -> SceneController.showError("CHARACTER PLAY", "You must choose the same amount of student to move"));
            close = false;
        } else {
            Platform.runLater(() -> SceneController.showError("CHARACTER PLAY", "Some actions are required in order to play this card, Please fulfill them."));
            close = false;
        }

        if (close)
            stage.close();
    }

    /**
     * Constructor
     * Sets the stage
     * @param serverHandler the server handler
     */
    public CharacterSceneController(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
        stage = new Stage();
        stage.initOwner(SceneController.getActiveScene().getWindow());
        stage.initModality(Modality.NONE);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setAlwaysOnTop(true);
        xOffset = 0;
        yOffset = 0;
    }

    /**
     * Shows the stage
     */
    public void displayAlert() {
        stage.show();
    }

    /**
     * Sets the scene
     * @param scene
     */
    public void setScene(Scene scene) {
        stage.setScene(scene);
    }

    /**
     * on Root Pane Mouse Pressed sets the x and y offset
     * @param event
     */
    private void onRootPaneMousePressed(MouseEvent event) {
        xOffset = stage.getX() - event.getScreenX();
        yOffset = stage.getY() - event.getScreenY();
    }

    /**
     * on Root Pane Mouse Dragged sets the x and y
     * @param event
     */
    private void onRootPaneMouseDragged(MouseEvent event) {
        stage.setX(event.getScreenX() + xOffset);
        stage.setY(event.getScreenY() + yOffset);
    }

    /**
     * sets the character infos and actions
     * @param gameInfo
     * @param number
     */
    public void setInfo(GameInfo gameInfo, int number) {
        coin.setVisible(false);
        Label[] labels = {label0, label1, label2, label3, label4, label5};
        for (Label i : labels)
            i.setText("");
        ok.setVisible(false);
        for (ChoiceBox<String> i : selection) {
            i.setVisible(false);
            i.getItems().clear();
        }

        color1.clear();
        color2.clear();
        colorSect = new int[]{-1,-1,-1,-1,-1,-1};
        colorPresent = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        islandSelected = -1;
        color = null;
        playable = false;
        colors.setVisible(false);
        this.gameInfo = gameInfo;
        this.number = number;
        characterType = gameInfo.getCharacter(number);
        character.getStyleClass().clear();
        if (characterType.getPrice() + 1 == gameInfo.getCharacterCost(number))
            coin.setVisible(true);
        character.getStyleClass().add(gameInfo.getCharacter(number).getText().toLowerCase());
        description.setText(gameInfo.getCharacter(number).getDescription());
        characterName.setText(gameInfo.getCharacter(number).getText().replaceAll("_", " "));
        FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
        double max = 0;
        for (int i = 0; i < characterName.getText().length(); i++){
            max += fontLoader.getCharWidth(characterName.getText().charAt(i), characterName.getFont());
        }
        characterName.setLayoutX((600-max)/2);
        if (characterType.equals(CharacterType.FARMER) || characterType.equals(CharacterType.MAGIC_DELIVERY_MAN) || characterType.equals(CharacterType.CENTAUR) || characterType.equals(CharacterType.KNIGHT)){
            setText("");
            playable = true;
        }
        if ((characterType.equals(CharacterType.HERALD)))
            setText("Select an Island");
        if (characterType.equals(CharacterType.GRANDMOTHER_HERBS))
            setText("Move a No Entry Tile on an Island");
        if ((characterType.equals(CharacterType.MONK)))
            setText("Move a Student on an Island");
        if ((characterType.equals(CharacterType.SPOILED_PRINCESS)))
            setText("Move a Student on the Board");
        if (characterType.equals(CharacterType.MUSHROOM_HUNTER) || characterType.equals(CharacterType.THIEF)) {
            setText("Select a Color");
            colors.setVisible(true);
            colors.getItems().addAll("Green", "Red", "Yellow", "Pink", "Blue");
        }
        int dimension = 0;
        if (characterType.equals(CharacterType.MINSTREL)) {
            setText("Entrance - Dining");
            dimension = 2;
        }
        if (characterType.equals(CharacterType.JESTER)) {
            setText("Entrance - Card");
            dimension = 3;
        }


        for (int i = 0; i < 5; i++){
            colorPresent[i] = gameInfo.getEntranceStudents(gameInfo.getFrontPlayer())[i];
            if (gameInfo.getEntranceStudents(gameInfo.getFrontPlayer())[i] > 0)
                color1.add(PawnColor.getColor(i).toString());
            if (dimension == 2) {
                colorPresent[i + 5] = gameInfo.getDiningStudents(gameInfo.getFrontPlayer())[i];
                if (gameInfo.getDiningStudents(gameInfo.getFrontPlayer())[i] > 0)
                    color2.add(PawnColor.getColor(i).toString());
            }
            else {
                colorPresent[i + 5] = gameInfo.getCharacterInfo(number)[i];
                if (gameInfo.getCharacterInfo(number)[i] > 0)
                    color2.add(PawnColor.getColor(i).toString());
            }
        }
        for (int i = 0; i < dimension; i++){
            selection.get(i).setVisible(true);
            selection.get(i).getItems().addAll(color1);
            selection.get(i+3).setVisible(true);
            selection.get(i+3).getItems().addAll(color2);
        }

    }

    /**
     * Sets the main controller
     * @param mainController the main controller to set
     */
    public void setMain(MainController mainController) {
        this.mainController = mainController;
    }

    /**
     * Sets the specified island
     * @param i the island to set
     */
    public void setIslandSelected(int i) {
        islandSelected = i;
    }

    /**
     * Sets the specified color
     * @param string the string representing the color to set
     */
    public void setColor(String string) {
        color = string;
    }

    /**
     * Sets the text with the specified string
     * @param s the string to set
     */
    public void setText(String s){
        request.setText(s);
        FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
        double max = 0;
        for (int i = 0; i < request.getText().length(); i++){
            max += fontLoader.getCharWidth(request.getText().charAt(i), request.getFont());
        }
        request.setLayoutX(225 + (335-max)/2);
    }

    /**
     * Display the text "OK"
     */
    public void setOk(){
        ok.setVisible(true);
    }

    private void updateChoiceBox(int max){
        color1.clear();
        color2.clear();
        int minus1, minus2;
        for (int i = 0; i < 5; i++){
            minus1 = 0; minus2 = 0;
            for (int j = 0; j < 3; j++){
                if (colorSect[j] == i)
                    minus1++;
                if (colorSect[j+3] == i)
                    minus2++;
            }
            if (colorPresent[i] - minus1 > 0)
                color1.add(PawnColor.getColor(i).toString());
            if (colorPresent[i+5] - minus2> 0)
                color2.add(PawnColor.getColor(i).toString());
        }
        for (int i = 0; i < max; i++){
            selection.get(i).getItems().clear();
            selection.get(i).getItems().addAll(color1);
            selection.get(i+3).getItems().clear();
            selection.get(i+3).getItems().addAll(color2);
        }
    }
}
