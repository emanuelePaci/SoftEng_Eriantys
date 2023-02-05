package it.polimi.ingsw.client.gui.scene;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import it.polimi.ingsw.client.ServerHandler;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.util.List;

/**
 * Game setup controller class
 */
public class GameSetupController implements GenericSceneController{

    private ServerHandler serverHandler;
    @FXML
    private Button yesBtn, noBtn, createBtn, refreshBtn, newGameBtn, enterBtn;
    @FXML
    private ListView<String> lobbyList;
    @FXML
    private Text label, expertMode, playerNumber, numPlayer, isExpert, lobbySelection;
    @FXML
    private RadioButton expertNo, expertYes, players3, players2;
    private boolean refreshed = false;
    private List<String[]> lobbies;

    /**
     * Constructor
     * Initializes the server handler with it is associated
     * @param serverHandler1 the server handler to associate
     */
    public GameSetupController(ServerHandler serverHandler1){
        serverHandler = serverHandler1;
    }

    /**
     * Sets the mouse click events and the observer for the lobby list
     */
    @FXML
    public void initialize(){
        yesBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onYesBtnClick);
        noBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onNoBtnClick);
        createBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onCreateBtnClicked);
        refreshBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onRefreshBtnClicked);
        newGameBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onNewGameBtnClicked);
        enterBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onEnterBTnClicked);
        lobbyList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (lobbyList.getSelectionModel().getSelectedItem() == null) {
                    numPlayer.setText("PLAYERS:                           n.a");
                    isExpert.setText("EXPERT MODE:               n.a");
                } else {
                    String player1Name = lobbyList.getSelectionModel().getSelectedItem().substring(0, (!lobbyList.getSelectionModel().getSelectedItem().contains(",") ? lobbyList.getSelectionModel().getSelectedItem().length() : lobbyList.getSelectionModel().getSelectedItem().indexOf(",")));
                    for (String[] lobb : lobbies)
                        if (lobb.length > 1 && lobb[1].equals(player1Name)) {
                            numPlayer.setText("PLAYERS:                             " + lobb[lobb.length - 2]);
                            isExpert.setText("EXPERT MODE:               " + (lobb[lobb.length - 1].equals("true") ? "YES" : "NO"));
                        }
                }
                textOffset(numPlayer);
                textOffset(isExpert);
            }
        });
    }

    private <T extends Event> void onRefreshBtnClicked(T t) {
        serverHandler.refreshLobbies();
    }

    private <T extends Event> void onNewGameBtnClicked(T t) {
        serverHandler.setGame(true, -1);
    }

    private <T extends Event> void onEnterBTnClicked(T t) {
        if (lobbyList.getSelectionModel().getSelectedItem() == null)
            Platform.runLater(() -> SceneController.showError("LOBBY SELECTION", "Please select a Lobby in order to continue!"));
        else {
            String player1Name = lobbyList.getSelectionModel().getSelectedItem();
            player1Name = player1Name.substring(0, (!player1Name.contains(",") ? player1Name.length() : player1Name.indexOf(",")));
            for (String[] s : lobbies)
                if (s[1].equals(player1Name)) {
                    Platform.runLater(() -> serverHandler.setGame(false, Integer.parseInt(s[0])));
                    return;
                }
        }
    }

    @FXML
    private void onYesBtnClick(Event event) {
        serverHandler.setGame(true, -1);
    }

    @FXML
    private void onNoBtnClick(Event event) {
        refreshed = true;
        serverHandler.refreshLobbies();
    }

    /**
     * Displays the text "NUMBER OF PLAYERS" and at its right it displays two RatioButton with 2 for the first, and 3 for the second.
     * Displays the text "EXPERT MODE" and at its right it displays two RatioButton with YES for the first, and NO for the second.
     */
    public void newGame() {
        change(false);
        expertNo.setVisible(true);
        expertYes.setVisible(true);
        players2.setVisible(true);
        players3.setVisible(true);
        playerNumber.setVisible(true);
        expertMode.setVisible(true);
        createBtn.setVisible(true);
    }

    private void onCreateBtnClicked(Event event) {
        int numPlayer = 2;
        if (players3.isSelected())
            numPlayer = 3;
        boolean expert = expertYes.isSelected();
        serverHandler.initialSetUp(numPlayer, expert);
    }

    /**
     * Informs the client that there is a lobby that are starting but they are not ready yet. Otherwise, shows the available lobbies to select
     * @param lobbies the list of lobbies available
     */
    public void lobbySelection(List<String[]> lobbies) {
        this.lobbies = lobbies;
        change(true);
        if(lobbies.get(0)[0].equals("starting") && !refreshed){
            Platform.runLater(() -> SceneController.showError("LOBBY SELECTION", "There are some lobby starting try Refreshing or start a New Game"));
            return;
        }
        refreshed = false;

        lobbyList.getItems().clear();
        StringBuilder builder;
        String[] playersName = new String[lobbies.size()];
        for (String[] s : lobbies){
            builder = new StringBuilder();
            for (int i = 1; i < s.length - 2; i++) {
                if (i == 1)
                    builder.append(s[i]);
                else
                    builder.append(", ").append(s[i]);
            }
            playersName[lobbies.indexOf(s)] = builder.toString();
        }

        lobbyList.getItems().addAll(playersName);
    }

    private void change(boolean lobby){
        yesBtn.setVisible(false);
        noBtn.setVisible(false);
        label.setVisible(false);
        numPlayer.setVisible(lobby);
        isExpert.setVisible(lobby);
        lobbySelection.setVisible(lobby);
        lobbyList.setVisible(lobby);
        refreshBtn.setVisible(lobby);
        newGameBtn.setVisible(lobby);
        enterBtn.setVisible(lobby);
    }

    private void textOffset(Text text){
        FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
        double max = 0;
        for (int i = 0; i < text.getText().length(); i++){
            max += fontLoader.getCharWidth(text.getText().charAt(i), text.getFont());
        }
        text.setWrappingWidth(max);
        text.setLayoutX(345.0);
    }
}
