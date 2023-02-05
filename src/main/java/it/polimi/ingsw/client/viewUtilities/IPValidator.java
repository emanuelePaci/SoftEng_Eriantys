package it.polimi.ingsw.client.viewUtilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Ip validator class
 */
public class IPValidator {
    private final static String defaultIP = "127.0.0.1";
    private final static String defaultPort = "8080";
    private final static Pattern pattern = Pattern.compile("^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])(\\.(?!$)|$)){4}$");

    /**
     * Constructor
     */
    public IPValidator(){

    }
    /**
     * returns true if the ip has the right pattern
     * @param serverIP the given server ip
     * @return true if the ip has the right pattern
     */
    public static boolean isCorrectIP(String serverIP){
        Matcher matcher = pattern.matcher(serverIP);
        return matcher.matches();
    }

    /**
     * returns true if the port is between 0 and 65536
     * @param serverPort the given server port
     * @return true if the port is between 0 and 65536
     */
    public static boolean isCorrectPort(String serverPort){
        int port;
        try {
            port = Integer.parseInt(serverPort);
        } catch (NumberFormatException e) {
            return false;
        }
        return port > 0 && port < 65536;
    }

    /**
     * gets the default ip
     * @return gets the default ip
     */
    public static String getDefaultIP() {
        return defaultIP;
    }

    /**
     * gets the default port
     * @return the default port
     */
    public static String getDefaultPort() {
        return defaultPort;
    }
}
