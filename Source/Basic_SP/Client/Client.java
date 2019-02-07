package Basic_SP.Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Client {

    private Socket socket = null;
    private Scanner userInput = null;
    private DataInputStream inboundFromServer = null;
    private DataOutputStream outboundToServer = null;

    private String server = null;
    private int port = Integer.MIN_VALUE;

    private Logger logger = null;
    private FileHandler fileHandler = null;
    private SimpleFormatter simpleFormatter = null;

    public Client(String server, int port) {
        this.server = server;
        this.port = port;

        this.userInput = new Scanner(System.in);

        this.initializeLogger();

        this.initialize();
    }

    private void initializeLogger() {
        this.logger = Logger.getLogger("Client_Log");

        try {
            this.fileHandler = new FileHandler("../Logs/Client_Log.log");
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.addHandler(this.fileHandler);

        simpleFormatter = new SimpleFormatter();

        fileHandler.setFormatter(simpleFormatter);
    }

    private void initialize() {

        try {
            logger.info("Attempting to initialize connect with server");

            socket = new Socket(server, port);

            logger.info("Connection established successfully with server");

            logger.info("Attempting to establish communication streams");

            inboundFromServer = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            outboundToServer = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            logger.info("Communication streams established successfully");
        } catch (IOException e) {
            logger.severe(e.toString());
        }


        String userMsg = null;

        while ((userMsg = new String(userInput.nextLine())) != "Terminate") {
            try {
                logger.info("Client Message: " + userMsg);

                outboundToServer.writeUTF(userMsg);
            } catch (IOException e) {
                logger.severe(e.toString());
            }
        }

        try {
            logger.info("Attempting to terminate socket and communication streams");

            socket.close();
            inboundFromServer.close();
            outboundToServer.close();
            userInput.close();
            
            logger.info("Socket and communication streams terminated");
        } catch (IOException e) {
            logger.severe(e.toString());
        }
    }
}
