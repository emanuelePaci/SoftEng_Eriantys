package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.viewUtilities.AnsiGraphics;
import it.polimi.ingsw.client.viewUtilities.GameInfo;
import it.polimi.ingsw.client.viewUtilities.IPValidator;
import it.polimi.ingsw.exceptions.ClientException;
import it.polimi.ingsw.exceptions.ErrorType;
import it.polimi.ingsw.model.enumerations.CharacterType;
import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.model.enumerations.TowerColor;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static it.polimi.ingsw.model.enumerations.PawnColor.getColor;
import static it.polimi.ingsw.model.enumerations.PawnColor.lookup;

/**
 * This class manages the CLI view of a specific client, reading input and sending specific messages to the server
 */
public class CLIView implements View {
    private GameInfo gameInfo;
    private Scanner scanner;
    private Scanner threadScanner;
    private ServerHandler serverHandler;
    private Boolean clearer;

    /**
     * Constructor
     * Initializes this client view.
     * @param serverHandler the specific client of this view.
     */
    public CLIView(ServerHandler serverHandler){
        scanner = new Scanner(System.in);
        this.serverHandler = serverHandler;
    }

    /**
     * Asks the ServerIp and ServerPort, then gives it to the ServerHandler which will establish the connection between server and Client
     */
    @Override
    public synchronized void start() {
        String serverIP;
        String serverPort;
        boolean valid;

        System.out.print(AnsiGraphics.getTitle()+ "\n");
        System.out.print(AnsiGraphics.createMenu());
        System.out.print(AnsiGraphics.setTitle("SERVER IP SELECTION"));
        System.out.print(AnsiGraphics.putText(" > Enter the server IP [Press enter for 127.0.0.1]", false, true));
        do {
            serverIP = scanner.nextLine();

            if (serverIP.equals("")){
                valid = true;
                serverIP = IPValidator.getDefaultIP();
            } else {
                valid = true;
                if (!IPValidator.isCorrectIP(serverIP)){
                    valid = false;
                    System.out.print(AnsiGraphics.putText("  > Server IP not valid. Please try again", true, false));
                }
            }
        } while (!valid);

        System.out.print(AnsiGraphics.setTitle("SERVER PORT SELECTION"));
        System.out.print(AnsiGraphics.putText(" > Enter the server PORT [Press enter for 8080]", false, true));
        do {
            serverPort = scanner.nextLine();

            if (serverPort.equals("")){
                valid = true;
                serverPort = IPValidator.getDefaultPort();
            } else {
                valid = true;
                if (!IPValidator.isCorrectPort(serverPort)){
                    valid = false;
                    System.out.print(AnsiGraphics.putText("  > Server PORT not valid. Please try again", true, false));
                }
            }
        } while (!valid);

        serverHandler.initConnection(serverIP, serverPort);
    }

    /**
     * Asks the client its nickname
     * @param requestAgain if is it true, the client inserts a nickname which has already been chosen, so it has to choose another
     */
    @Override
    public synchronized void playerSetUp(boolean requestAgain) {
        System.out.print(AnsiGraphics.setTitle("NICKNAME SELECTION"));
        if (requestAgain) {
            System.out.print(AnsiGraphics.putText("  > This nickname has already been taken, please try again", true, false));
        } else {
            System.out.print(AnsiGraphics.putText(" > Choose a nickname", false, true));
        }

        String nickname = scanner.nextLine();
        serverHandler.setPlayerInfo(nickname);
    }

    /**
     * Asks the client if it wants to start a new game or prefers to enter a lobby created previously by another player
     */
    @Override
    public synchronized void gameSetUp() {
        boolean valid;
        String response;

        System.out.print(AnsiGraphics.setTitle("LOBBY SELECTION"));
        System.out.print(AnsiGraphics.putText(" > Do you want to start a fresh new game? [Yes/No]", false, true));
        do {
            valid = true;
            response = scanner.nextLine().toUpperCase();
            if (!(response.equals("YES") || response.equals("NO"))){
                valid = false;
                System.out.print(AnsiGraphics.putText("  > Invalid choice, try again", true, false));
            }
        } while (!valid);

        if (response.equals("YES"))
            serverHandler.setGame(true, -1);
        else {
            serverHandler.refreshLobbies();
        }
    }

    /**
     * Creates a new lobby if there are no other present, otherwise prints the list of available lobbies and asks which one the client wants to join
     * @param lobbies the list of the names of the lobbies present
     * @param firstLobby a flag which is true only if there are no lobbies
     */
    @Override
    public void refreshLobbies(List<String[]> lobbies, boolean firstLobby) {
        if (firstLobby){
            System.out.print(AnsiGraphics.putText("  > There isn't any lobby, we will create one just for you", true, false));
            synchronized (this){
                try{
                    this.wait(3000);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            }
            serverHandler.setGame(true, -1);
        } else
            lobbySelection(lobbies);
    }

    /**
     * Lobby selection method
     * @param lobbies the lobby list to be print
     */
    private synchronized void lobbySelection(List<String[]> lobbies) {
        boolean valid;
        boolean valid2;
        boolean lobbiesStarting = false;
        int starting = 0;
        if (lobbies.get(0)[0].equals("starting")) {
            System.out.print(AnsiGraphics.putText(" > There are some lobby starting. Press Enter to refresh or type \"NewGame\" to create a lobby", false, true));
            lobbiesStarting = true;
        } else
            System.out.print(AnsiGraphics.putText(" > Insert the number of the lobby, press Enter to refresh or type \"NewGame\" to create a lobby", false, true));

        do {
            valid2 = true;
            if (!lobbiesStarting) {
                AnsiGraphics.clean();
                printLobbies(lobbies, starting);
            }
            do {
                valid = true;
                String selection = scanner.nextLine().toLowerCase();
                if (selection.equals(""))
                    serverHandler.refreshLobbies();
                else if (selection.matches("newgame"))
                    serverHandler.setGame(true, -1);
                else if (selection.matches("prev") || selection.matches("next")) {
                    valid2 = false;
                    if (starting + 7 < lobbies.size() && selection.matches("next"))
                        starting += 7;
                    else if (starting > 0 && selection.matches("prev"))
                        starting -= 7;
                } else if (selection.matches("\\d+") && !lobbiesStarting) {
                    int numLobby = Integer.parseInt(selection);
                    if (numLobby > lobbies.size() || numLobby < 1) {
                        System.out.print(AnsiGraphics.putText("  > Inserted number don't correspond to any Lobby, try again", true, false));
                        valid = false;
                    } else {
                        serverHandler.setGame(false, Integer.parseInt(lobbies.get(numLobby - 1)[0]));
                    }
                } else {
                    System.out.print(AnsiGraphics.putText("  > You haven't inserted a positive number, try again", true, false));
                    valid = false;
                }
            } while (!valid);
        } while (!valid2);
    }

    /**
     * Prints the lobbies available
     * @param lobbies
     * @param starting
     */
    private void printLobbies(List<String[]> lobbies, int starting) {
        int k = starting;
        int maxLength = 30;
        StringBuilder base = new StringBuilder();
        while(k < Math.min(7+starting, lobbies.size())) {
            k++;
            base.delete(0, base.length());
            base.append("  > Lobby-").append(k).append(" : ");
            for (int j = 1; j < lobbies.get(k - 1).length - 2; j++)
                if (j == 1) {
                    base.append(lobbies.get(k - 1)[j], 0, Math.min(lobbies.get(k - 1)[j].length(), maxLength));
                } else {
                    base.append(", ").append(lobbies.get(k - 1)[j], 0, Math.min(lobbies.get(k - 1)[j].length(), maxLength));
                }
            String expert = "Yes";
            if (lobbies.get(k - 1)[lobbies.get(k - 1).length - 1].equals("false"))
                expert = "No";
            System.out.print(AnsiGraphics.putText(base + "       NumPlayers: "+lobbies.get(k - 1)[lobbies.get(k - 1).length - 2]+" - ExpertMode: " + expert, false, false));

        }
        if (lobbies.size() > 7 && starting == 0)
            System.out.print(AnsiGraphics.putText("  > ... type [Next] to see other lobbies", false, false));
        else if (lobbies.size() > 7 && lobbies.size()-starting <= 7)
            System.out.print(AnsiGraphics.putText("  > ... type [Prev] to go back", false, false));
        else if (lobbies.size() > 7)
            System.out.print(AnsiGraphics.putText("  > ... type [Prev/Next] to move through lobbies", false, false));
    }

    /**
     * Informs the client that the lobby it chooses is full, then prints the list of the lobbies available
     */
    @Override
    public void fullLobby(){
        System.out.print(AnsiGraphics.putText("  > The lobby is full, try again", true, false));
        synchronized (this){
            try{
                this.wait(3000);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }
        serverHandler.refreshLobbies();
    }

    /**
     * Asks at the client, only if it creates a new lobby, the number of player and the difficulty of the game it wants to create
     */
    @Override
    public synchronized void initialSetUp() {
        boolean expert= false;
        boolean valid1 = false;
        boolean valid2;
        int numPlayer=0;
        System.out.print(AnsiGraphics.setTitle("GAME SETTINGS"));
        System.out.print(AnsiGraphics.putText(" > Insert the number of player [2/3]", false, true));
        do {
            String str=scanner.nextLine();
            if(str.matches("\\d+")) {
                numPlayer = Integer.parseInt(str);
                if (numPlayer < 2 || numPlayer > 3)
                    System.out.print(AnsiGraphics.putText("  > You inserted a wrong number, try again", true, false));
                else
                    valid1 = true;
            }
            else
                System.out.print(AnsiGraphics.putText("  > You have to insert a positive number, try again", true, false));

        } while (!valid1);
        System.out.print(AnsiGraphics.putText(" > Do you want to play in Expert Mode? [Yes/No]", false, true));
        do {
            valid2 = true;
            String expertString = scanner.nextLine();
            if ((expertString.equalsIgnoreCase("YES"))) {
                expert = true;
            } else if (!(expertString.equalsIgnoreCase("NO"))) {
                System.out.print(AnsiGraphics.putText("  > You inserted a wrong answer, try again", true, false));
                valid2 = false;
            }
        }while(!valid2);

        serverHandler.initialSetUp(numPlayer, expert);
    }

    /**
     * Asks at the client which color wants for its towers
     * @param tower the list of the colors of the towers available
     * @param requestAgain if is it true, the client inserts a color which has already been chosen, so it has to choose another
     */
    @Override
    public synchronized void colorSetUp(List<TowerColor> tower, boolean requestAgain) {
        boolean valid=false;
        String colorText;
        TowerColor color = null;
        System.out.print(AnsiGraphics.setTitle("COLOR SELECTION"));
        if(tower.size() == 1){
            color = tower.get(0);
            colorText = tower.get(0).getText();
            if (colorText.equals("BLACK"))
                colorText = "BLUE";
            if (requestAgain)
                System.out.print(AnsiGraphics.putText("  > All color has already been assigned", true, true));
            System.out.print(AnsiGraphics.putText("  > Your color will be " + colorText, true, false));
            synchronized (this){
                try{
                    this.wait(3000);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        else {
            StringBuilder string = new StringBuilder();
            string.append(" > Insert a color ");
            for (TowerColor t : tower) {
                if (t.equals(TowerColor.BLACK))
                    string.append("BLUE ");
                else
                    string.append(t).append(" ");
            }
            System.out.print(AnsiGraphics.putText(string.toString(), false, true));
            if (requestAgain)
                System.out.print(AnsiGraphics.putText("  > The previous color was already taken", true, false));
            do {
                String colorString = scanner.nextLine().toUpperCase();
                if (!colorString.equalsIgnoreCase("BLACK") && (tower.contains(TowerColor.getColor(colorString)) || colorString.equalsIgnoreCase("BLUE"))) {
                    valid = true;
                    if (colorString.equals("BLUE"))
                        color = TowerColor.BLACK;
                    else
                        color = TowerColor.getColor(colorString);
                } else {
                    System.out.print(AnsiGraphics.putText("  > You inserted a wrong color, try again", true, false));
                }

            } while (!valid);
        }
        if (!requestAgain) {
            System.out.print(AnsiGraphics.setTitle("WAITING ROOM"));
            System.out.print(AnsiGraphics.putText("  > Waiting for other players...", true, true));
        }
        serverHandler.setPlayerColor(color);
    }

    /**
     * Prints at screen the actual status of the game
     * @param gameInfo contains the information of the status of the game
     */
    @Override
    public void printGameBoard(GameInfo gameInfo){
        this.gameInfo = gameInfo;
        System.out.print("\n\n\n"+AnsiGraphics.CLEAR);
        System.out.print(AnsiGraphics.createGame(gameInfo));
    }

    /**
     * Clears the buffer of a client while it is not its turn
     */
    @Override
    public void bufferClearer() {
        InputStreamReader control = new InputStreamReader(System.in);
        threadScanner = new Scanner(System.in);
        while (Boolean.TRUE.equals(clearer)) {
            try {
                if (control.ready())
                    threadScanner.nextLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Stops the cleaning of the buffer
     */
    @Override
    public void setClearer(boolean condition) {
        clearer = condition;
    }

    /**
     * Asks at the client which action it would like to do among the actions available
     */
    @Override
    public synchronized void choseAction(){
        boolean valid;
        int action;
        do {
            valid = true;
            System.out.print("Insert a command: ");
            String code = scanner.nextLine();
            action = 0;
            if (code.matches("\\d+"))
                action = Integer.parseInt(code);
            else
                System.out.println("You must insert a number");

            switch (action) {
                case 1:
                    correctAssistant();
                    break;
                case 2:
                    PawnColor color2 = getColor(correctEntranceColor(1).get(0));
                    serverHandler.moveToDiningRoomRequest(color2);
                    break;
                case 3:
                    int islandPosition = correctIslandInput();
                    PawnColor color = getColor(correctEntranceColor(1).get(0));
                    serverHandler.moveStudentToIslandRequest(islandPosition, color);
                    break;
                case 4:
                    int endPosition = correctIslandInput();
                    serverHandler.moveMotherNatureRequest(endPosition);
                    break;
                case 5:
                    printCharacterDescription();
                    valid = false;
                    break;
                case 6:
                    if (!correctCharacter())
                        valid = false;
                    break;
                case 7:
                    correctCloud();
                    break;
                default:
                    valid = false;
            }
        }while (!valid);
    }

    /**
     * Prints a description of some possible exception, if one of them occurs
     * @param exception the exception occurred
     */
    @Override
    public void printError(ClientException exception){
        if (exception.getErrorType().equals(ErrorType.NOT_ENOUGH_MONEY))
            System.out.println(exception.getErrorType().getErrorText() + exception.getPrice() + ")");
        else if (exception.getErrorType().equals(ErrorType.MAX_STUDENT_REACHED))
            System.out.println(exception.getErrorType().getErrorText() + exception.getPawnColor());
        else if (exception.getErrorType().equals(ErrorType.SERVER_OFFLINE))
            System.out.print(AnsiGraphics.putText("  > Server may be offline. Please try again", true, false));
        else
            System.out.println(exception.getErrorType().getErrorText());
        if (!exception.getErrorType().equals(ErrorType.SERVER_OFFLINE))
            choseAction();
    }

    /**
     * Prints the winner of the game, then calls {@link #newGame(String)} method
     * @param winner1 the nickname of the winner
     * @param winner2 the nickname of the eventual second winner in case of draw. It may be null if there is only one winner
     * @param nickname the nickname of the client
     */
    @Override
    public void printWinner(String winner1, String winner2, String nickname) {
        serverHandler.endConnection();
        if(winner2==null){
            if(winner1.equals(nickname))
                System.out.print(AnsiGraphics.finalMessages("you have won!!!"));
            else
                System.out.print(AnsiGraphics.finalMessages(winner1+" has won"));
        }
        else{
            if(winner1.equals(nickname))
                System.out.print(AnsiGraphics.finalMessages("It's a draw! You and " +winner2+ " have won the game"));
            else if(winner2.equals(nickname))
                System.out.print(AnsiGraphics.finalMessages("It's a draw! You and " +winner1+ " have won the game"));
            else
                System.out.print(AnsiGraphics.finalMessages("It's a draw! "+ winner1 +" and " +winner2+ " have won the game"));
        }
        scanner.nextLine();
        newGame("");
    }

    /**
     * Informs the client, while it is choosing a lobby, if that lobby is no more available because of a disconnection of one of the player in the lobby, otherwise calls {@link #newGame(String)} method
     * @param nickname the nickname of the player disconnected
     * @param notEntered true if the client is not entered in the lobby
     */
    @Override
    public synchronized void printInterrupt(String nickname, boolean notEntered) {
        if (notEntered){
            System.out.print(AnsiGraphics.putText(" > The game was interrupted just before your entrance, please choose another Lobby.", true, true));
            synchronized (this){
                try{
                    this.wait(2000);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            }
            serverHandler.refreshLobbies();
        }else{
            newGame(nickname);
        }
    }

    /**
     * Asks the client if it wants to start a new game
     * @param nickname the nickname of the player disconnected, if present. It may be null
     */
    @Override
    public void newGame(String nickname) {
        boolean valid2;

        System.out.print(AnsiGraphics.getTitle()+ "\n");
        System.out.print(AnsiGraphics.createMenu());
        System.out.print(AnsiGraphics.setTitle("GAME ENDED"));
        System.out.print(AnsiGraphics.putText(" > Would you like to start a new game? [Yes/No]", false, true));
        if (!nickname.equals(""))
            System.out.print(AnsiGraphics.putText("  > The game was interrupted, " + nickname + " disconnected from the server.", true, false));
        do {
            valid2 = true;
            String expertString = scanner.nextLine();
            if ((expertString.equalsIgnoreCase("YES"))) {
                String[] cli = {"-c"};
                Client.main(cli);
            } else if (!(expertString.equalsIgnoreCase("NO"))) {
                System.out.print(AnsiGraphics.putText("  > Wrong choice, try again", true, false));
                valid2 = false;
            }
        }while(!valid2);
    }

    /**
     * Informs the client that the game is ended due to a problem with the server
     */
    @Override
    public synchronized void printServerDown() {
        System.out.print(AnsiGraphics.getTitle()+ "\n");
        System.out.print(AnsiGraphics.createMenu());
        System.out.print(AnsiGraphics.setTitle("GAME ENDED"));
        System.out.print(AnsiGraphics.putText(" > Server Unreachable.", true, true));
    }

    /**
     * Print in System.Out the description of the Character Cards used in this game.
     */
    private void printCharacterDescription(){
        if (!gameInfo.isExpertMode()){
            System.out.println("You can do that only in expert mode");
            return;
        }
        for(int i=1;i<4; i++)
            System.out.println("Character "+i+" is: "+ gameInfo.getCharacter(i-1).getText() + ", " + gameInfo.getCharacter(i-1).getDescription());
    }

    /**
     * Ask the player to insert the number of the island in which the action will take place.
     * Check if the number inserted is valid.
     * @return the number of the island starting from 0
     */
    private int correctIslandInput(){
        boolean correct=false;
        int islandPosition=0;
        do {
            System.out.print("Insert the number of the island you want to choose: ");
            String strNumIsland = scanner.nextLine();
            if (strNumIsland.matches("\\d+")) {
                islandPosition = Integer.parseInt(strNumIsland);
                if(islandPosition >0 && islandPosition<=gameInfo.getNumIsland())
                    correct=true;
                else
                    System.out.println("Invalid Island number \n");
            } else
                System.out.println("Invalid Island number \n");
        } while (!correct);
        return islandPosition-1;
    }

    /**
     * Ask player the color of a student to move from the entrance, will ask maxRequest times.
     * @param maxRequest the maximum number of student to move.
     * @return list of indexes of the colors selected
     */
    private List<Integer> correctEntranceColor(int maxRequest){
        int correct=0;
        List<Integer> colors= new ArrayList<>();
        int [] color = {0,0,0,0,0};
        do{
            System.out.print("Insert the color of a student you want to move from the entrance: ");
            String colorString = scanner.nextLine();
            if (lookup(colorString) == null)
                System.out.print("This is not a valid color \n");
            else if (gameInfo.getEntranceStudents(gameInfo.getCurrentPlayer())[lookup(colorString).getIndex()] < color[lookup(colorString).getIndex()]+1)
                System.out.print("You don't have enough student of this color \n");
            else {
                correct++;
                colors.add(lookup(colorString).getIndex());
                color[lookup(colorString).getIndex()]++;
            }
        } while (correct<maxRequest);
        return colors;
    }

    /**
     *Ask player the color of a student to move from the character card, will ask maxRequest times.
     * @param position the position in list of the character chosen
     * @param maxRequest the maximum amount of student movable
     * @return list of indexes of the colors selected
     */
    private List<Integer> correctCharacterColor(int position, int maxRequest){
        int correct=0;
        List<Integer> colors= new ArrayList<>();
        int [] color = {0,0,0,0,0};
        do{
            System.out.print("Insert the color of a student you want to move from the character card: ");
            String colorString = scanner.nextLine();
            if (lookup(colorString) == null)
                System.out.print("This is not a valid color \n");
            else if (gameInfo.getCharacterInfo(position)[lookup(colorString).getIndex()] < color[lookup(colorString).getIndex()]+1)
                System.out.print("The card doesn't have this student \n");
            else {
                correct++;
                colors.add(lookup(colorString).getIndex());
                color[lookup(colorString).getIndex()]++;
            }
        } while (correct<maxRequest);
        return colors;
    }


    /**Ask player the color of a student to move from the dining, will ask maxRequest times.
     * @param maxRequest the maximum amount of student movable
     * @return list of indexes of the colors selected
     */
    private List<Integer> correctDiningColor(int maxRequest){
        int correct=0;
        List<Integer> colors= new ArrayList<>();
        int [] color = {0,0,0,0,0};
        do{
            System.out.print("Insert the color of a student you want to move from the dining: ");
            String colorString = scanner.nextLine();
            if (lookup(colorString) == null)
                System.out.print("This is not a valid color \n");
            else if (gameInfo.getDiningStudents(gameInfo.getCurrentPlayer())[lookup(colorString).getIndex()] < color[lookup(colorString).getIndex()]+1)
                System.out.print("You don't have this student \n");
            else {
                correct++;
                colors.add(lookup(colorString).getIndex());
                color[lookup(colorString).getIndex()]++;
            }
        } while (correct<maxRequest);
        return colors;
    }

    /**
     * Ask the player to choose an assistant and validate the input given.
     * If is correct call {@link ServerHandler#useAssistantRequest(int) useAssistantRequest} method.
     */
    private void correctAssistant() {
        boolean correct=false;
        do {
            System.out.print("Insert the number of the assistant card you want to use: ");
            String strAssistant = scanner.nextLine();
            if (strAssistant.matches("\\d+")) {
                int weight = Integer.parseInt(strAssistant);
                if (Arrays.asList(gameInfo.getAssistants()).contains(weight)) {
                    serverHandler.useAssistantRequest(Arrays.asList(gameInfo.getAssistants()).indexOf(weight));
                    correct = true;
                } else System.out.println("Invalid Assistant Choice \n");
            } else
                System.out.println("Invalid Choice \n");
        } while (!correct);
    }

    /**
     * Ask the player to insert the number of the cloud.
     * Check if the inserted value is correct.
     * If is correct calls {@link ServerHandler#cloudChosenRequest cloudChosenRequest}
     */
    private void correctCloud() {
        boolean correct=false;
        do {
            System.out.print("Insert the number of the cloud you want to chose: ");
            String strCloud = scanner.nextLine();
            if (strCloud.matches("\\d+")) {
                int cloudPosition = Integer.parseInt(strCloud);
                if (cloudPosition < 1 || cloudPosition > gameInfo.getNumPlayer())
                    System.out.println("Invalid Cloud Number \n");
                else {
                    serverHandler.cloudChosenRequest(cloudPosition - 1);
                    correct = true;
                }
            } else
                System.out.println("Invalid Choice  \n");
        } while (!correct);
    }

    /**
     * Ask the player which character he wants to use.
     * Then asks the necessary information to use the selected character.
     * @return if the character has been called successfully
     */
    private boolean correctCharacter(){
        boolean valid=false;
        if (!gameInfo.isExpertMode()){
            System.out.println("You can do that only in expert mode");
            return false;
        }
        do {
            System.out.print("Insert the position of the character card you want to use: ");
            String strCharacter = scanner.nextLine();
            if (strCharacter.matches("\\d+")) {
                int position = Integer.parseInt(strCharacter);
                if(position>0 && position<=3) {
                    position--;
                    CharacterType type = gameInfo.getCharacter(position);
                    if (type == CharacterType.CENTAUR || type == CharacterType.KNIGHT || type == CharacterType.MAGIC_DELIVERY_MAN || type == CharacterType.FARMER)
                        serverHandler.useCharacterRequest(position);
                    else if (type == CharacterType.HERALD || type == CharacterType.GRANDMOTHER_HERBS)
                        serverHandler.useCharacterRequest(correctIslandInput(), position);
                    else if (type == CharacterType.SPOILED_PRINCESS || type == CharacterType.THIEF || type == CharacterType.MUSHROOM_HUNTER) {
                        if (type == CharacterType.SPOILED_PRINCESS) {
                            serverHandler.useCharacterRequest(getColor(correctCharacterColor(position,1).get(0)), position);
                        } else {
                            boolean correct = false;
                            do {
                                System.out.print("Insert a color: ");
                                String colorString = scanner.nextLine();
                                PawnColor color = lookup(colorString);
                                if (color == null)
                                    System.out.print("This is not a valid color \n");
                                else {
                                    serverHandler.useCharacterRequest(color, position);
                                    correct = true;
                                }
                            } while (!correct);
                        }
                    } else if (type == CharacterType.JESTER || type == CharacterType.MINSTREL) {
                        List<Integer> tmpColors= new ArrayList<>();
                        int [] colors;
                        if (type == CharacterType.JESTER) {
                            colors= new int [6];
                            tmpColors.addAll(correctEntranceColor(3));
                            tmpColors.addAll(correctCharacterColor(position, 3));
                            for (int i=0; i<6; i++)
                                colors[i]=tmpColors.get(i);

                        } else {
                            colors = new int[4];
                            tmpColors.addAll(correctEntranceColor(2));
                            tmpColors.addAll(correctDiningColor(2));
                            for (int i = 0; i < 4; i++)
                                colors[i] = tmpColors.get(i);
                        }
                        serverHandler.useCharacterRequest(colors, position);
                    } else {
                        serverHandler.useCharacterRequest(correctIslandInput(), getColor(correctCharacterColor(position,1).get(0)), position);
                    }
                    valid = true;
                }
                else
                    System.out.println("Invalid Choice \n");
            } else
                System.out.println("Invalid Choice \n");
        }while(!valid);
        return true;
    }
}
