package it.polimi.ingsw.server;

import it.polimi.ingsw.client.viewUtilities.IPValidator;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

/**
 * server class
 */
public class Server {
    public static Server server;
    /**
     * the server socket
     */
    public static ServerSocket serverSocket;
    /**
     * the lobby handler
     */
    private static LobbyHandler lobbyHandler;
    private static boolean active = false;

    /**
     * main class for the server
     * @param args the parameters
     */
    public static void main(String[] args) {
        boolean valid;
        int port = 0;
        do {
            valid = true;
            System.out.print("Insert the server PORT or press Enter for default (8080): ");
            Scanner scanner = new Scanner(System.in);
            String tempPort = scanner.nextLine();
            if (Objects.equals(tempPort, "")){
                port = Integer.parseInt(IPValidator.getDefaultPort());
            } else if (!IPValidator.isCorrectPort(tempPort)){
                System.out.print("The inserted port is not valid!\n\n");
                valid = false;
            } else {
                port = Integer.parseInt(tempPort);
            }
        } while (!valid);

        try {
            serverSocket = new ServerSocket(port);
        }
        catch(Exception e){
            System.out.println("Server not started correctly");
            return;
        }
        System.out.println("Server successfully started");
        active = true;
        server= new Server();
        lobbyHandler = new LobbyHandler();
        server.acceptPlayer();
    }

    /**
     * creates a new client handler and accepts the new player
     */
    public void acceptPlayer() {
        int numPlayer=0;
        while(active){
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Connection accepted: " + socket.getRemoteSocketAddress());
                numPlayer++;
                ClientHandler clientHandler=new ClientHandler(socket, "TemporaryName"+numPlayer, lobbyHandler);
                clientHandler.start();
            }
            catch(IOException e){
                System.out.println("Error with connection");
            }
        }
    }
}
