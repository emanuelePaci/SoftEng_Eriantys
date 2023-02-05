package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * manages the lobbies
 */
public class LobbyHandler {
    private List<ClientHandler> allClientHandlers;
    private List<ClientHandler[]> lobbies;
    private List<Controller> controllers;
    private List<VirtualView> virtualViews;

    /**
     * Constructor
     * sets client handlers, the lobby list, the controllers and virtual views
     */
    public LobbyHandler(){
        allClientHandlers = new ArrayList<>();
        lobbies = new ArrayList<>();
        controllers = new ArrayList<>();
        virtualViews = new ArrayList<>();
    }

    /**
     * adds a client to the lobby
     * @param lobbyNumber the lobby number
     * @param clientHandler the client handler
     */
    public synchronized void addClient(int lobbyNumber, ClientHandler clientHandler) {
        if (controllers.get(lobbyNumber).getGame().isGameEnded()){
            clientHandler.printInterrupt("", true);
        } else if (controllers.get(lobbyNumber).getGame().getNumPlayer() == lobbies.get(lobbyNumber).length) {
            clientHandler.gameSetUp(true);
        } else {
            ClientHandler[] clients = lobbies.get(lobbyNumber);
            ClientHandler[] clientHandlers = new ClientHandler[clients.length + 1];

            System.arraycopy(clients, 0, clientHandlers, 0, clients.length);
            clientHandlers[clients.length] = clientHandler;

            lobbies.set(lobbyNumber, clientHandlers);

            clientHandler.setVirtualView(virtualViews.get(lobbyNumber));
            virtualViews.get(lobbyNumber).addClientHandler(clientHandler);

            virtualViews.get(lobbyNumber).colorSetUp(clientHandler.getPlayerNickname());
        }
    }

    /**
     * adds a new client handler
     * @param clientHandler the client handler
     */
    public synchronized void newClient(ClientHandler clientHandler){
        ClientHandler[] clientHandlers = {clientHandler};
        lobbies.add(clientHandlers);
        controllers.add(new Controller());
        virtualViews.add(new VirtualView(controllers.get(controllers.size() - 1)));
        clientHandler.setVirtualView(virtualViews.get(virtualViews.size() - 1));
        controllers.get(controllers.size()-1).setVirtualView(virtualViews.get(virtualViews.size() - 1));
        virtualViews.get(virtualViews.size()-1).addClientHandler(clientHandler);

        (new Thread(() -> {

            try {
                controllers.get(controllers.size() - 1).gameStart();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        })).start();

        clientHandler.initialSetUp();
    }

    /**
     * gets the list of the lobbies
     * @return
     */
    private synchronized List<String[]> getLobbies(){
        List<String[]> lobbiesModified = new ArrayList<>();
        boolean valid = false;
        boolean starting = false;
        for (ClientHandler[] c : lobbies) {
            if (controllers.get(lobbies.indexOf(c)).getGame().getNumPlayer() == -1 && !controllers.get(lobbies.indexOf(c)).getGame().isGameEnded())
                starting = true;
            else if (!(controllers.get(lobbies.indexOf(c)).getGame().isGameEnded() || c.length == controllers.get(lobbies.indexOf(c)).getGame().getNumPlayer())){
                valid = true;
                String[] temp = new String[c.length + 3];

                for (int i = 1; i < c.length + 1; i++)
                    temp[i] = c[i - 1].getPlayerNickname();

                temp[0] = "" + lobbies.indexOf(c);
                temp[c.length + 1] = "" + controllers.get(lobbies.indexOf(c)).getGame().getNumPlayer();
                temp[c.length + 2] = "" + controllers.get(lobbies.indexOf(c)).getGame().isExpertMode();

                lobbiesModified.add(temp);
            }
        }
        if (starting && !valid) {
            String[] temp = {"starting"};
            lobbiesModified.add(temp);
        }

        return lobbiesModified;
    }

    /**
     * checks if the given nickname is already used
     * @param nickName the player nickname
     * @return true the client nickname is equal to the given nickname
     */
    public synchronized boolean isNicknameUsed(String nickName){
        if (nickName.equals(""))
            return true;

        for (ClientHandler c : allClientHandlers)
                if (c.getPlayerNickname().equals(nickName))
                    return true;

        return false;
    }

    /**
     * refreshes the lobby list
     * @param clientHandler the client handler
     */
    public synchronized void refreshLobbies(ClientHandler clientHandler) {
        List<String[]> lobby = getLobbies();

        if (lobby.isEmpty())
            clientHandler.refreshLobbies(null, true);
        else
            clientHandler.refreshLobbies(lobby, false);
    }

    /**
     * sets the player nickname
     * @param nickname the player nickname
     * @param clientHandler the client handler
     */
    public synchronized void setPlayerNickname(String nickname, ClientHandler clientHandler) {
        allClientHandlers.add(clientHandler);
        if (!isNicknameUsed(nickname)) {
            clientHandler.setPlayerNickname(nickname);

            clientHandler.gameSetUp();
        } else {
            clientHandler.playerSetUp(true);
        }
    }

    /**
     * removes the lobby with the indicated virtual view
     * @param virtualView the virtual view
     * @param clientHandler the client handler
     */
    public synchronized void terminateLobby(VirtualView virtualView, ClientHandler clientHandler) {
        int index = virtualViews.indexOf(virtualView);
        if (index != -1) {
            controllers.get(index).getGame().endGame();
            allClientHandlers.removeAll((List.of(lobbies.get(index))));
        } else {
            allClientHandlers.remove(clientHandler);
        }
    }
}
