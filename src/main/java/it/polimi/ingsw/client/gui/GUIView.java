package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.gui.scene.*;
import it.polimi.ingsw.client.viewUtilities.GameInfo;
import it.polimi.ingsw.exceptions.ClientException;
import it.polimi.ingsw.exceptions.ErrorType;
import it.polimi.ingsw.model.enumerations.TowerColor;

import javafx.application.Platform;

import java.util.List;

/**
 * This class manages the GUI view of a specific client, reading input and sending specific messages to the server
 */
public class GUIView implements View {
    private ServerHandler serverHandler;

    /**
     * Constructor
     * Initializes the client view
     * @param serverHandler the server handler to associate
     */
    public GUIView(ServerHandler serverHandler){
        this.serverHandler = serverHandler;
    }

    /**
     * Method of the Interface View, not used in this implementation
     */
    @Override
    public void start() {
    }

    /**
     * Asks the client if it wants to start a new game or prefers to enter a lobby created previously by another player
     */
    @Override
    public void gameSetUp() {
        GameSetupController gameSetupController = getGameSetupController();
    }

    /**
     * Creates a new lobby if there are no other present, otherwise prints the list of available lobbies and asks which one the client wants to join
     * @param lobbies the list of the names of the lobbies present
     * @param firstLobby a flag which is true only if there are no lobbies
     */
    @Override
    public void refreshLobbies(List<String[]> lobbies, boolean firstLobby) {
        GameSetupController gameSetupController = getGameSetupController();
        if (firstLobby){
            serverHandler.setGame(true, -1);
            Platform.runLater(() -> SceneController.showError("SETUP WARNING", "There isn't any lobby, we have created one for you"));
        } else {
            Platform.runLater(() -> gameSetupController.lobbySelection(lobbies));
        }
    }

    /**
     * Informs the client that the lobby it chooses is full, then displays the list of the lobbies available
     */
    @Override
    public void fullLobby() {
        Platform.runLater(() -> SceneController.showError("LOBBY SELECTION", "The lobby is full, try again."));
        serverHandler.refreshLobbies();
    }

    /**
     * Asks at the client, only if it creates a new lobby, the number of player and the difficulty of the game it wants to create
     */
    @Override
    public void initialSetUp() {
        GameSetupController gameSetupController = getGameSetupController();
        Platform.runLater(gameSetupController::newGame);
    }

    /**
     * Asks the client its nickname
     * @param requestAgain if is it true, the client inserts a nickname which has already been chosen, so it has to choose another
     */
    @Override
    public void playerSetUp(boolean requestAgain) {
        if (!requestAgain) {
            NicknameController nicknameController = new NicknameController(serverHandler);
            Platform.runLater(() -> SceneController.changeRootPane(nicknameController, "nickname.fxml"));
        } else
            Platform.runLater(() -> SceneController.showError("NICKNAME SELECTION ERROR", "This nickname has already been taken, please try again"));
    }

    /**
     * Asks at the client which color wants for its towers
     * @param tower the list of the colors of the towers available
     * @param requestAgain if is it true, the client inserts a color which has already been chosen, so it has to choose another
     */
    @Override
    public void colorSetUp(List<TowerColor> tower, boolean requestAgain) {
        ColorSetUpController colorSetUpController = getColorSetUpController();
        if (requestAgain)
            Platform.runLater(() -> SceneController.showError("COLOR SELECTION", "This color has already been taken."));
        Platform.runLater(() -> colorSetUpController.setUp(tower));
    }

    /**
     * Displays the actual status of the game
     * @param gameInfo contains the information of the status of the game
     */
    @Override
    public void printGameBoard(GameInfo gameInfo) {
        MainController mainController = getMainController(gameInfo);
        Platform.runLater(mainController::update);
    }

    /**
     * Method of the Interface View, not used in this implementation
     */
    @Override
    public void choseAction() {
        return;
    }

    /**
     * Displays the winner of the game, then calls {@link #newGame(String)} method
     * @param winner1 the nickname of the winner
     * @param winner2 the nickname of the eventual second winner in case of draw. It may be null if there is only one winner
     * @param nickname the nickname of the client
     */
    @Override
    public void printWinner(String winner1, String winner2, String nickname) {
        EndController endController = getEndController();
        serverHandler.endConnection();
        Platform.runLater(() -> endController.winner(winner1, winner2, nickname));
        newGame("");
    }

    /**
     * Informs the client, while it is choosing a lobby, if that lobby is no more available because of a disconnection of one of the player in the lobby, otherwise calls {@link #newGame(String)} method
     * @param nickname the nickname of the player disconnected
     * @param notEntered true if the client is not entered in the lobby
     */
    @Override
    public void printInterrupt(String nickname, boolean notEntered) {
        if (notEntered) {
            Platform.runLater(() -> SceneController.showError("LOBBY SELECTION", "The game was interrupted just before your entrance, please choose another Lobby."));
            Platform.runLater(() -> serverHandler.refreshLobbies());
        } else {
            Platform.runLater(() -> SceneController.showError("PLAYER DISCONNECTION", nickname + " disconnected from the server. Game ended!"));
            Platform.runLater(() -> newGame(""));
        }
    }

    /**
     * Asks the client if it wants to start a new game
     * @param nickname the nickname of the player disconnected, if present. It may be null
     */
    @Override
    public void newGame(String nickname) {
        EndController endController = getEndController();
        Platform.runLater(endController::newGame);
    }

    /**
     * Informs the client that the game is ended due to a problem with the server
     */
    @Override
    public void printServerDown() {
        Platform.runLater(() -> SceneController.showError("SERVER DISCONNECTED", "Server is down, the game will close."));
    }

    /**
     * Method of the Interface View, not used in this implementation
     */
    @Override
    public void setClearer(boolean condition) {}

    /**
     * Method of the Interface View, not used in this implementation
     */
    @Override
    public void bufferClearer() {}

    /**
     * Displays a description of some possible exception, if one of them occurs
     * @param exception the exception occurred
     */
    @Override
    public void printError(ClientException exception) {
        if (exception.getErrorType().equals(ErrorType.NOT_ENOUGH_MONEY))
            Platform.runLater(() -> SceneController.showError(exception.getErrorType().toString(), exception.getErrorType().getErrorText() + exception.getPrice() + ")"));
        else if (exception.getErrorType().equals(ErrorType.MAX_STUDENT_REACHED))
            Platform.runLater(() -> SceneController.showError(exception.getErrorType().toString(), exception.getErrorType().getErrorText() + exception.getPawnColor()));
        else
            Platform.runLater(() -> SceneController.showError(exception.getErrorType().toString(), exception.getErrorType().getErrorText()));

        MainController controller;
        try {
            controller = (MainController) SceneController.getActiveController();
            Platform.runLater(controller::update);
        } catch (ClassCastException ignored){}

    }

    /**
     * If already present return the instance of {@link GameSetupController} if not create a new one.
     * @return The instance of {@link GameSetupController}
     */
    private GameSetupController getGameSetupController() {
        GameSetupController controller;
        try {
            controller = (GameSetupController) SceneController.getActiveController();
        } catch (ClassCastException e) {
            controller = new GameSetupController(serverHandler);
            GameSetupController finalController = controller;
            Platform.runLater(() -> SceneController.changeRootPane(finalController, "gameSetup.fxml"));
        }
        return controller;
    }

    /**
     *If already present return the instance of {@link MainController} if not create a new one.
     * update the controller with gameInfo.
     * @param gameInfo the current game information to display
     * @return The instance of {@link MainController}
     */
    private MainController getMainController(GameInfo gameInfo) {
        MainController controller;
        try {
            controller = (MainController) SceneController.getActiveController();
            MainController finalController1 = controller;
            Platform.runLater(() -> finalController1.setGameInfo(gameInfo));
        } catch (ClassCastException e) {
            controller = new MainController(serverHandler);
            MainController finalController = controller;
            Platform.runLater(() -> finalController.setGameInfo(gameInfo));
            Platform.runLater(() -> SceneController.changeRootPane(finalController, "main.fxml"));
        }
        return controller;
    }

    /**
     * If already present return the instance of {@link ColorSetUpController} if not create a new one.
     * @return The instance of {@link ColorSetUpController}
     */
    private ColorSetUpController getColorSetUpController() {
        ColorSetUpController controller;
        try {
            controller = (ColorSetUpController) SceneController.getActiveController();
        } catch (ClassCastException e) {
            controller = new ColorSetUpController(serverHandler);
            ColorSetUpController finalController = controller;
            Platform.runLater(() -> SceneController.changeRootPane(finalController, "colorSetUp.fxml"));
        }
        return controller;
    }

    /**
     * If already present return the instance of {@link EndController} if not create a new one.
     * @return The instance of {@link EndController}
     */
    private EndController getEndController(){
        EndController controller;
        try {
            controller = (EndController) SceneController.getActiveController();
        } catch (ClassCastException | NoClassDefFoundError e) {
            controller = new EndController();
            EndController finalController = controller;
            Platform.runLater(() -> SceneController.changeRootPane(finalController, "end.fxml"));
        }
        return controller;
    }

}
