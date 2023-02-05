package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CLIView;

import it.polimi.ingsw.client.gui.JavaFXInit;
import javafx.application.Application;
import org.fusesource.jansi.AnsiConsole;

/**
 * Client class
 */
public class Client {
    /**
     * Constructor
     */
    public Client(){

    }
    /**
     * Main of the Client class
     * @param args the command line parameter
     */
    public static void main(String[] args) {
        AnsiConsole.systemInstall();
        boolean cliParam = false;
        ServerHandler serverHandler = new ServerHandler();
        View view;

        for (String arg : args) {
            if (arg.equals("--cli") || arg.equals("-c")) {
                cliParam = true;
                break;
            }
        }

        if (cliParam){
            view = new CLIView(serverHandler);
            serverHandler.setView(view);
            view.start();
        } else {
            Application.launch(JavaFXInit.class);
        }
    }
}

