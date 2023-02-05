package it.polimi.ingsw.client.gui.scene;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.gui.JavaFXInit;
import it.polimi.ingsw.client.viewUtilities.GameInfo;
import it.polimi.ingsw.model.enumerations.CharacterType;
import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.model.enumerations.PlayerState;
import it.polimi.ingsw.model.enumerations.TowerColor;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Main controller class
 */
public class MainController implements GenericSceneController{

    @FXML
    private Pane board1, board2, board3;
    @FXML
    private Group bd1Entrance, bd1Students, bd1Professors, bd1Towers;
    @FXML
    private Group bd2Entrance, bd2Students, bd2Professors, bd2Towers;
    @FXML
    private Group bd3Entrance, bd3Students, bd3Professors, bd3Towers;
    @FXML
    private Label name1, name2, name3;
    @FXML
    private Pane character1, character2, character3;
    @FXML
    private Pane lastAssistant1, lastAssistant2, lastAssistant3;
    @FXML
    private Pane cloud1, cloud2, cloud3, music;
    @FXML
    private Group assistants, tableProfessors;
    @FXML
    private ImageView coin1, coin2, coin3, coinc1, coinc2, coinc3;
    @FXML
    private Label coinNumber1, coinNumber2, coinNumber3;
    @FXML
    private Label info, info1;
    @FXML
    private Pane island1, island2, island3, island4, island5, island6, island7, island8, island9, island10, island11, island12;
    private GameInfo gameInfo;
    private ServerHandler serverHandler;
    private CharacterSceneController controller;
    private CharacterType character = null;
    private boolean characterPlayed;
    private boolean islandSelector, boardSelector;
    private int unionAtStart = 0, unionAtEnd = 0, lastSize = 0, newFirst = 1, newLast = 12;
    private List<Integer> islandsSize = new ArrayList<>();
    private double[] currentLocations = new double[24];

    /**
     * Constructor
     * @param serverHandler the server handler
     */
    public MainController(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

    /**
     *Initialize all the components of the scene.
     */
    @FXML
    public void initialize() {
        Pane[] clouds = {cloud1, cloud2, cloud3};
        ImageView[] coins = {coin1, coin2, coin3};
        Label[] numCoins = {coinNumber1, coinNumber2, coinNumber3};
        Pane[] characters = {character1, character2, character3};
        Pane[] islands = {island1, island2, island3, island4, island5, island6, island7, island8, island9, island10, island11, island12};
        int numPlayer = gameInfo.getNumPlayer();
        for (int i = 0; i < 12; i++) {
            currentLocations[i] = islands[i].getLayoutX();
            currentLocations[12 + i] = islands[i].getLayoutY();
        }
        JavaFXInit.musicEffect(music);
        JavaFXInit.setStopped(true);
        try {
            JavaFXInit.getPlayer().stop();
        } catch (NullPointerException e){
        }
        List<Media> musics = new ArrayList<>();
        musics.add(new Media(String.valueOf(getClass().getClassLoader()
                .getResource("media/Game/Game.mp3"))));
        musics.add(new Media(String.valueOf(getClass().getClassLoader()
                .getResource("media/Game/Game1.mp3"))));
        musics.add(new Media(String.valueOf(getClass().getClassLoader()
                .getResource("media/Game/Game2.mp3"))));
        JavaFXInit.setStopped(false);
        JavaFXInit.changeMusic(musics);

        if (numPlayer == 3) {
            board3.setVisible(true);
            name3.setVisible(true);
            cloud3.setVisible(true);
        }

        if (gameInfo.isExpertMode()){
            for (int i = 0; i < numPlayer; i++) {
                coins[i].setVisible(true);
                numCoins[i].setVisible(true);
            }
            for (int i = 0; i < 3; i++) {
                characters[i].getStyleClass().add(gameInfo.getCharacter(i).getText().toLowerCase());
                characterEffect(characters[i]);
            }
        }

        for (Node i : assistants.getChildren()) {
                assistantEffect(i);
        }

        for (int i = 0; i < numPlayer; i++)
            cloudEffect(clouds[i]);

        int index = 0;
        for (String s : gameInfo.getPlayersName()) {
            if (s.equals(gameInfo.getFrontPlayer()))
                name1.setText(s);
            else if (index == 0) {
                name2.setText(s);
                index++;
            } else
                name3.setText(s);
        }

        for (Pane pane : islands)
            islandEffect(pane);

        for (Node i : bd1Entrance.getChildren())
            studentEffect((Pane) i);

        targetEffect(board1);
        board1.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString() && db.getString().charAt(0) == 'C' && boardSelector){
                controller.setColor(db.getString().substring(1));
                controller.setOk();

                ClipboardContent content = new ClipboardContent();
                content.putString("");
                db.setContent(content);

                success = true;
            }
            else if (db.hasString() && !db.getString().equals("mother") && db.getString().charAt(0) != 'C') {
                serverHandler.moveToDiningRoomRequest(PawnColor.lookup(db.getString()));

                ClipboardContent content = new ClipboardContent();
                content.putString("");
                db.setContent(content);

                success = true;
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void cloudEffect(Pane pane){
        pane.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            serverHandler.cloudChosenRequest(Integer.parseInt(pane.getId().substring(5)) - 1);
        });
    }

    private void assistantEffect(Node node1){
        Pane node = (Pane) node1;

        node.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            if (!node.getStyleClass().isEmpty()) {
                node.setLayoutY(3);
                node.setPrefHeight(106.06);
            }
        });
        node.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> {
            node.setLayoutY(110.0 - 20.0);
            node.setPrefHeight(20);
        });
        node.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (!node.getStyleClass().isEmpty()) {
                serverHandler.useAssistantRequest(Arrays.asList(gameInfo.getAssistants()).indexOf(Integer.parseInt(node.getStyleClass().get(0).substring(9))));
            }
        });
    }

    private void characterEffect(Pane pane){
        pane.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            controller = getCharacterController();
            character = gameInfo.getCharacter(Integer.parseInt(pane.getId().substring(9)) - 1);
            Platform.runLater(() -> SceneController.showCharacter(this, controller, gameInfo, Integer.parseInt(pane.getId().substring(9)) - 1));
            if (gameInfo.getCharacter(Integer.parseInt(pane.getId().substring(9)) - 1).equals(CharacterType.MONK) || gameInfo.getCharacter(Integer.parseInt(pane.getId().substring(9)) - 1).equals(CharacterType.GRANDMOTHER_HERBS)) {
                characterPlayed = true;
                islandSelector = false;
                boardSelector = false;
            }
             else if (character.equals(CharacterType.HERALD)) {
                islandSelector = true;
                characterPlayed = false;
                boardSelector = false;
            }
             else if (gameInfo.getCharacter(Integer.parseInt(pane.getId().substring(9)) - 1).equals(CharacterType.SPOILED_PRINCESS)){
                boardSelector = true;
                islandSelector = false;
                characterPlayed = false;
            } else {
                boardSelector = false;
                islandSelector = false;
                characterPlayed = false;
            }
        });

        for (Node node : pane.getChildren()){
            node.setOnDragDetected(event -> {
                if (!node.getStyleClass().isEmpty() && (characterPlayed || boardSelector)) {
                    Dragboard db = node.startDragAndDrop(TransferMode.MOVE);
                    Pane panel = (Pane) node;
                    ClipboardContent content = new ClipboardContent();
                    content.putString("C"+node.getStyleClass().get(0));

                    if (node.getStyleClass().get(0).equals("doNotEnter")) {
                        db.setDragView(new Image(String.valueOf(getClass().getResource("/photos/deny_island_icon.png")), panel.getHeight(), panel.getWidth(), true, true));
                        node.getStyleClass().clear();
                    }else {
                        db.setDragView(new Image(String.valueOf(getClass().getResource("/photos/students/student_" + panel.getStyleClass().get(0) + ".png")), panel.getHeight(), panel.getWidth(), true, true));
                        int number = Integer.parseInt(((Label) panel.getChildren().get(0)).getText().substring(1));
                        if (number <= 1){
                            node.getStyleClass().clear();
                        }
                        ((Label) ((Pane) node).getChildren().get(0)).setText("x"+(number-1));
                    }

                    db.setContent(content);

                    event.consume();
                }
            });

            node.setOnDragDone(event -> {
                Dragboard db = event.getDragboard();
                if (!db.getString().equals("")) {
                    int number = Integer.parseInt(((Label) ((Pane) node).getChildren().get(0)).getText().substring(1));
                    if (number == 0)
                        node.getStyleClass().add(db.getString().substring(1));
                    ((Label) ((Pane) node).getChildren().get(0)).setText("x"+(number+1));
                } else {
                    characterPlayed = false;
                    boardSelector = false;
                }
                event.consume();
            });
        }
    }

    private void studentEffect(Pane pane){
        pane.setOnDragDetected(event -> {
            if (!pane.getStyleClass().isEmpty()) {
                Dragboard db = pane.startDragAndDrop(TransferMode.MOVE);
                db.setDragView(new Image(String.valueOf(getClass().getResource("/photos/students/student_"+pane.getStyleClass().get(0)+".png")), pane.getHeight(), pane.getWidth(), true, true));
                ClipboardContent content = new ClipboardContent();
                content.putString(pane.getStyleClass().get(0));
                db.setContent(content);
                pane.getStyleClass().clear();
                event.consume();
            }
        });

        pane.setOnDragDone(event -> {
            Dragboard db = event.getDragboard();
            if (!db.getString().equals(""))
                pane.getStyleClass().add(db.getString());
            event.consume();
        });
    }

    private void islandEffect(Pane pane){
        targetEffect(pane);

        ImageView mother = (ImageView) pane.getChildren().get(7);

        mother.setOnDragDetected(event -> {
            if (mother.isVisible()) {
                Dragboard db = mother.startDragAndDrop(TransferMode.MOVE);
                db.setDragView(new Image(String.valueOf(getClass().getResource("/photos/mother_nature.png")), mother.getFitWidth(), mother.getFitHeight(), true, true));
                ClipboardContent content = new ClipboardContent();
                content.putString("mother");
                db.setContent(content);
                mother.setVisible(false);
                event.consume();
            }
        });

        mother.setOnDragDone(event -> {
            Dragboard db = event.getDragboard();
            if (!db.getString().equals(""))
                mother.setVisible(true);
            event.consume();
        });

        Integer[] temp = new Integer[]{1,1,1,1,1,1,1,1,1,1,1,1};
        islandsSize.addAll(List.of(temp));

        pane.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (islandSelector) {
                int i = islandTarget(Integer.parseInt(pane.getId().substring(6)));

                controller.setOk();
                controller.setIslandSelected(i - 1);
            }
        });
        pane.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> {
            if (islandSelector) {
                DropShadow dropShadow = new DropShadow();
                dropShadow.setHeight(15.0);
                dropShadow.setWidth(15.0);
                dropShadow.setSpread(0.8);
                dropShadow.setBlurType(BlurType.GAUSSIAN);
                dropShadow.setColor(Color.ORANGE);
                pane.setEffect(dropShadow);
            }
        });
        pane.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> {
            pane.setEffect(null);
        });

        pane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString() && db.getString().charAt(0) == 'C' && characterPlayed){
                int i = islandTarget(Integer.parseInt(pane.getId().substring(6)));

                controller.setIslandSelected(i - 1);
                controller.setColor(db.getString().substring(1));
                controller.setOk();

                ClipboardContent content = new ClipboardContent();
                content.putString("");
                db.setContent(content);

                success = true;
            } else if (db.hasString() && !db.getString().equals("mother") && db.getString().charAt(0) != 'C') {
                int i = islandTarget(Integer.parseInt(pane.getId().substring(6)));

                serverHandler.moveStudentToIslandRequest(i - 1, PawnColor.lookup(db.getString()));

                ClipboardContent content = new ClipboardContent();
                content.putString("");
                db.setContent(content);

                success = true;
            } else if (db.hasString() && db.getString().equals("mother")){
                int i = islandTarget(Integer.parseInt(pane.getId().substring(6)));

                serverHandler.moveMotherNatureRequest(i-1);

                ClipboardContent content = new ClipboardContent();
                content.putString("");
                db.setContent(content);

                success = true;
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void targetEffect(Pane pane){
        pane.setOnDragOver(event -> {
            if (event.getGestureSource() != pane &&
                    event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        pane.setOnDragEntered(event -> {
            if (event.getGestureSource() != pane &&
                    event.getDragboard().hasString() && !(pane.getId().charAt(0) == 'i' && boardSelector) && !(event.getDragboard().getString().equals("mother") && pane.getId().equals("board1")) && !(event.getDragboard().getString().charAt(0) == 'C' && characterPlayed && pane.getId().equals("board1"))) {
                DropShadow dropShadow = new DropShadow();
                dropShadow.setHeight(15.0);
                dropShadow.setWidth(15.0);
                dropShadow.setSpread(0.8);
                String s = event.getDragboard().getString();
                dropShadow.setBlurType(BlurType.GAUSSIAN);
                dropShadow.setColor(Color.ORANGE);
                if (!s.equals("mother") && !s.equals("CdoNotEnter"))
                    dropShadow.setColor(Color.valueOf((s.charAt(0) == 'C' ? s.substring(1) : s)));
                pane.setEffect(dropShadow);
            }

            event.consume();
        });

        pane.setOnDragExited(event -> {
            pane.setEffect(null);
            event.consume();
        });
    }

    /**
     * update the scene with the new game information
     */
    public void update(){
        characterPlayed = false;
        islandSelector = false;
        boardSelector = false;
        character = null;

        if (gameInfo.getLastMerged()[1] == 0 && gameInfo.getLastMerged()[0] != -1)
            unionAtStart = islandsSize.get(islandsSize.size()-1);
        if (gameInfo.getLastMerged()[1] > 0 && gameInfo.getLastMerged()[2] == 0)
            unionAtEnd = islandsSize.get(0);

        if (lastSize > gameInfo.getNumIsland()){
            int first = newFirst;
            if (unionAtEnd!=0) {
                newFirst += unionAtEnd - 12 + newLast;
                newLast = 12;
            }
            if (unionAtStart != 0){
                newLast -= unionAtStart - first + 1;
                newFirst = 1;
            }
        }

        lastSize = gameInfo.getNumIsland();
        islandsSize.clear();
        for (int i = 0; i < lastSize; i++)
            islandsSize.add(gameInfo.getIslandSize(i));

        info.setText("It's "+gameInfo.getCurrentPlayer()+" Turn!");
        FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
        double max = 0;
        for (int i = 0; i < info.getText().length(); i++){
            max += fontLoader.getCharWidth(info.getText().charAt(i), info.getFont());
        }
        info.setLayoutX(387.5 - max/2);
        info1.setText(""+gameInfo.getPlayerState());
        if (gameInfo.getPlayerState() == PlayerState.ACTION)
            info1.setText("Student Moves Remaining: " + gameInfo.getRemainingMoves());
        else if (gameInfo.getPlayerState().equals(PlayerState.PLANNING))
            info1.setText("Choose an Assistant");
        else if (gameInfo.getPlayerState().equals(PlayerState.ENDTURN))
            info1.setText("Choose a Cloud");
        max = 0;
        for (int i = 0; i < info1.getText().length(); i++){
            max += fontLoader.getCharWidth(info1.getText().charAt(i), info1.getFont());
        }
        info1.setLayoutX(387.5 - max/2);

        Integer[] assistantNumbers = gameInfo.getAssistants();
        for (int index = 1; index < 11; index++)
            assistants.getChildren().get(index).getStyleClass().clear();

        for (int index = 1; index < assistantNumbers.length + 1; index++)
            assistants.getChildren().get(index).getStyleClass().add("assistant"+assistantNumbers[index - 1]);

        ImageView[] coinc = {coinc1, coinc2, coinc3};
        if (gameInfo.isExpertMode()) {
            Pane[] characters = {character1, character2, character3};
            for (int i = 0; i < 3; i++) {
                if (gameInfo.getCharacterCost(i) > gameInfo.getCharacter(i).getPrice())
                    coinc[i].setVisible(true);
                for (int j = 0; j < 5; j++) {
                    Pane tempPane = (Pane) characters[i].getChildren().get(j);
                    tempPane.getStyleClass().clear();
                    tempPane.getChildren().get(0).setVisible(false);
                    if (gameInfo.getCharacterInfo(i)[j] > 0) {
                        tempPane.getStyleClass().add(PawnColor.getColor(j).toString().toLowerCase());
                        Label label = (Label) tempPane.getChildren().get(0);
                        label.setVisible(true);
                        label.setText("x" + gameInfo.getCharacterInfo(i)[j]);
                    }
                }
                for (int j = 0; j < gameInfo.getCharacterInfo(i)[5]; j++) {
                    characters[i].getChildren().get(j).getStyleClass().clear();
                    characters[i].getChildren().get(j).getStyleClass().add("doNotEnter");
                }
            }
        }

        Pane[] clouds = {cloud1, cloud2, cloud3};
        for (Pane pane : clouds)
            for (int i = 0; i < 4; i++)
                pane.getChildren().get(i).getStyleClass().clear();
        for (int number = 0; number < gameInfo.getNumPlayer(); number++) {
            int index = 0;
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < gameInfo.getCloudStudents(number)[i]; j++) {
                    clouds[number].getChildren().get(index).getStyleClass().add(PawnColor.getColor(i).toString().toLowerCase());
                    index++;
                }
            }
        }

        Group[] boardElements = {bd1Entrance, bd2Entrance, bd3Entrance, bd1Students, bd2Students, bd3Students, bd1Professors, bd2Professors, bd3Professors, bd1Towers, bd2Towers, bd3Towers};
        Label[] coinNumbers = {coinNumber1, coinNumber2, coinNumber3};
        Pane[] lastAssistants = {lastAssistant1, lastAssistant2, lastAssistant3};

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 50; j++)
                boardElements[3+i].getChildren().get(j).getStyleClass().clear();
            for (int j = 0; j < 8; j++) {
                boardElements[9 + i].getChildren().get(j).setVisible(false);
                boardElements[i].getChildren().get(j).getStyleClass().clear();
            }
            boardElements[i].getChildren().get(8).getStyleClass().clear();
        }

        for (int i = 0; i < 5; i++)
            if (gameInfo.getProfessors()[i] == 1)
                tableProfessors.getChildren().get(i).setVisible(false);

        int index = 1;
        for (String player : gameInfo.getPlayersName()) {
            if (player.equals(gameInfo.getFrontPlayer())) {
                if (gameInfo.isExpertMode())
                    coinNumber1.setText(""+gameInfo.getNumCoin(player));
                lastAssistant1.getStyleClass().clear();
                lastAssistant1.getStyleClass().add("assistant"+gameInfo.getLastUsed(player));
                for (int i = 0; i < gameInfo.getBoardTower(player); i++) {
                    boardElements[9].getChildren().get(i).setVisible(true);
                    ImageView image = (ImageView) boardElements[9].getChildren().get(i);
                    image.setImage(new Image((String.valueOf(getClass().getResource("/photos/towers/"+TowerColor.getColor(gameInfo.getPlayersTowerColor(player) - 5).toString().toLowerCase()+"_tower.png")))));
                }
                int number = 0;
                for (int i = 0; i < 5; i++)
                    for (int j = 0; j < gameInfo.getEntranceStudents(player)[i]; j++, number++)
                        bd1Entrance.getChildren().get(number).getStyleClass().add(PawnColor.getColor(i).toString().toLowerCase());
                for (int i = 0; i < 5; i++) {
                    bd1Professors.getChildren().get(i).setVisible(false);
                    if (gameInfo.getProfessors(player)[i] == 1)
                        bd1Professors.getChildren().get(i).setVisible(true);
                }
                number = 0;
                for (int i = 0; i < 5; i++)
                    for (int j = 0; j < gameInfo.getDiningStudents(player)[i]; j++, number++) {
                        bd1Students.getChildren().get(number).getStyleClass().add(PawnColor.getColor(i).toString().toLowerCase());
                        bd1Students.getChildren().get(number).setLayoutX(74.0 + j*19.0);
                        bd1Students.getChildren().get(number).setLayoutY(21 + i*28.5);
                    }
            }
            else {
                if (gameInfo.isExpertMode())
                    coinNumbers[index].setText(""+gameInfo.getNumCoin(player));
                lastAssistants[index].getStyleClass().clear();
                lastAssistants[index].getStyleClass().add("assistant"+gameInfo.getLastUsed(player));
                for (int i = 0; i < gameInfo.getBoardTower(player); i++) {
                    boardElements[9 + index].getChildren().get(i).setVisible(true);
                    ImageView image = (ImageView) boardElements[9 + index].getChildren().get(i);
                    image.setImage(new Image((String.valueOf(getClass().getResource("/photos/towers/"+TowerColor.getColor(gameInfo.getPlayersTowerColor(player) - 5).toString().toLowerCase()+"_tower.png")))));
                }
                int number = 0;
                for (int i = 0; i < 5; i++)
                    for (int j = 0; j < gameInfo.getEntranceStudents(player)[i]; j++, number++)
                        boardElements[index].getChildren().get(number).getStyleClass().add(PawnColor.getColor(i).toString().toLowerCase());
                for (int i = 0; i < 5; i++) {
                    boardElements[6 + index].getChildren().get(i).setVisible(false);
                    if (gameInfo.getProfessors(player)[i] == 1)
                        boardElements[6 + index].getChildren().get(i).setVisible(true);
                }
                number = 0;
                for (int i = 0; i < 5; i++)
                    for (int j = 0; j < gameInfo.getDiningStudents(player)[i]; j++, number++) {
                        boardElements[3+index].getChildren().get(number).getStyleClass().add(PawnColor.getColor(i).toString().toLowerCase());
                        boardElements[3+index].getChildren().get(number).setLayoutX(74.0 + j*19.0);
                        boardElements[3+index].getChildren().get(number).setLayoutY(21.0 + i*28.5);
                    }
                index++;
            }
        }

        Pane[] islands = {island1, island2, island3, island4, island5, island6, island7, island8, island9, island10, island11, island12};
        for (Pane pane : islands){
            pane.getChildren().get(6).setVisible(false);
            pane.getChildren().get(7).setVisible(false);
        }
        double [][] diagPositions = {{+40.6137, -56.886},{-40.6137, -56.886},{-40.6137, +56.886},{+40.6137, +56.886}};
        double [][] vertPositions = {{-65.128,0},{+65.128, 0}};
        double [][] horPositions = {{0,-72.628},{0,+72.628}};
        List<int[]> before = new ArrayList<>();
        before.add(new int[]{0, 1, 3, 4, 6, 7, 9, 10});
        before.add(new int[]{5, 11});
        before.add(new int[]{2,8});
        List<int[]> after = new ArrayList<>();
        after.add(new int[]{1, 2, 4, 5, 7, 8, 10, 11});
        after.add(new int[]{0, 6});
        after.add(new int[]{3,9});
        index = newFirst+newLast-13;
        double actualVert;
        double actualHoriz;
        int color;
        for (int i = 0; i < gameInfo.getNumIsland(); i++){
            color = gameInfo.getTowersOnIsland(i);
            if (gameInfo.getIslandSize(i) == 1) {
                islandFill(index, color, i, true, islands[index].getLayoutX(), islands[index].getLayoutY());
            }else {
                int center = gameInfo.getIslandSize(i)/2;
                double v2 = Math.toRadians(15.0 + 30.0 * (index + center));
                actualVert = 230 - 213 * Math.sin(v2);
                actualHoriz = 337.5 - 213 * Math.cos(v2);
                islandFill(index + center + (index + center < 0 ? 12 : 0), color, i, true, actualHoriz, actualVert);
                for (int j = index + center -1; j >= index; j--) {
                    int h = j + (j < 0 ? 12 : 0);
                    switch (islandUnion(before, h)) {
                        case 0:
                            actualVert += diagPositions[h / 3][0];
                            actualHoriz += diagPositions[h / 3][1];
                            islandFill(h, color, 0,false, actualHoriz, actualVert);
                            break;
                        case 1:
                            actualVert += vertPositions[(h - 1) / 6][0];
                            actualHoriz += vertPositions[(h - 1) / 6][1];
                            islandFill(h, color, 0,false, actualHoriz, actualVert);
                            break;
                        case 2:
                            actualVert += horPositions[h / 6][0];
                            actualHoriz += horPositions[h / 6][1];
                            islandFill(h, color, 0,false, actualHoriz, actualVert);
                            break;
                    }
                }
                actualVert = 230 - 213 * Math.sin(v2);
                actualHoriz = 337.5 - 213 * Math.cos(v2);
                for (int j = index + center + 1; j < index + gameInfo.getIslandSize(i); j++) {
                    int h = j - (j < 12 ? 0 : 12);
                    switch (islandUnion(after, h)) {
                        case 0:
                            actualVert -= diagPositions[h / 3][0];
                            actualHoriz -= diagPositions[h / 3][1];
                            islandFill(h, color, 0,false, actualHoriz, actualVert);
                            break;
                        case 1:
                            actualVert += vertPositions[h/ 6][0];
                            actualHoriz += vertPositions[h / 6][1];
                            islandFill(h, color, 0,false, actualHoriz, actualVert);
                            break;
                        case 2:
                            actualVert -= horPositions[h / 6][0];
                            actualHoriz -= horPositions[h / 6][1];
                            islandFill(h, color, 0,false, actualHoriz, actualVert);
                            break;
                    }
                }
            }

            index += gameInfo.getIslandSize(i);
        }
        unionAtEnd = 0;
        unionAtStart = 0;
    }

    private static int islandUnion(List<int[]> list, int island){
        for (int[] array : list)
            for (int i : array)
                if (i == island)
                    return list.indexOf(array);

        return -1;
    }

    private void islandFill(int number, int color, int actual, boolean fill, double newX, double newY){
        Pane[] islands = {island1, island2, island3, island4, island5, island6, island7, island8, island9, island10, island11, island12};
        TranslateTransition translate = new TranslateTransition();
        translate.setNode(islands[number]);
        translate.setDuration(Duration.millis(500));
        translate.setByX(newX - currentLocations[number]);
        translate.setByY(newY - currentLocations[number + 12]);
        translate.play();
        currentLocations[number] = newX;
        currentLocations[number + 12] = newY;

        if (color!=-1) {
            islands[number].getChildren().get(5).getStyleClass().clear();
            islands[number].getChildren().get(5).getStyleClass().add(TowerColor.getColor(color - 5).toString().toLowerCase());
        }
        int index = 0;
        if (fill) {
            if (actual == gameInfo.getMotherNaturePosition())
                islands[number].getChildren().get(7).setVisible(true);
            if (gameInfo.hasNoEntryTile(actual))
                islands[number].getChildren().get(6).setVisible(true);
        }

        for (Integer student : gameInfo.getStudentsOnIsland(actual)) {
            Pane pane = (Pane) islands[number].getChildren().get(index);
            pane.getStyleClass().clear();
            pane.getChildren().get(0).setVisible(false);
            if (fill && student != 0) {
                islands[number].getChildren().get(index).getStyleClass().add(PawnColor.getColor(index).toString().toLowerCase());
                pane.getChildren().get(0).setVisible(true);
                Label label = (Label) pane.getChildren().get(0);
                label.setText(""+student);
            }
            index++;
        }
    }

    /**
     * Set the new Game information used to update the scene
     * @param gameInfo the info about the game
     */
    public void setGameInfo(GameInfo gameInfo){
        this.gameInfo = gameInfo;
    }

    private CharacterSceneController getCharacterController(){
        if (controller == null)
            controller = new CharacterSceneController(serverHandler);

        return controller;
    }

    private int islandTarget(int target){
        int sum = 0;
        int i;
        for (i = 0; i < gameInfo.getNumIsland() && target > sum; i++) {
            sum += gameInfo.getIslandSize(i);
            if (i == 0)
                sum += -13 + newLast + newFirst;
        }
        if (target < newFirst)
            i = gameInfo.getNumIsland();
        if (target > newLast)
            i = 1;

        return i;
    }
}
