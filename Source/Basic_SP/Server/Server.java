package Basic_SP.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Server {

    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private DataInputStream inboundFromClient = null;
    private DataOutputStream outboundToClient = null;

    private int port = Integer.MIN_VALUE;

    private Logger logger = null;
    private FileHandler fileHandler = null;
    private SimpleFormatter simpleFormatter = null;

    public Server(String server, int port) {
        this.port = port;

        this.initializeLogger();

        this.initialize();
    }

    private void initializeLogger() {
        this.logger = Logger.getLogger("Server_Log");

        try {
            this.fileHandler = new FileHandler("../Logs/Server_Log.log");
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.addHandler(this.fileHandler);

        simpleFormatter = new SimpleFormatter();

        fileHandler.setFormatter(simpleFormatter);
    }

    private void initialize() {

        try {
            logger.info("Attempting to initialize server");

            serverSocket = new ServerSocket(port);

            logger.info("Server initialized successfully");

            logger.info("Attempting to accept client connection");

            socket = serverSocket.accept();

            logger.info("Client Connected Successfully");

            logger.info("Attempting to initialize communication streams");

            inboundFromClient = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            outboundToClient = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            logger.info("Communication streams initialized successfully");
        } catch (IOException e) {
            logger.severe(e.toString());
        }

        String clientMsg = "";
        String serverMsg = "";

        try {
            while ((clientMsg = inboundFromClient.readUTF()) != "Terminate") {
                logger.info("Client Message: " + clientMsg);

                serverMsg = "Received Message";

                logger.info("Server Message: " + serverMsg);

                outboundToClient.writeUTF(serverMsg);
            }
        } catch (IOException e) {
            logger.severe(e.toString());
        }

        try {
            logger.info("Attempting to terminate sockets and communication streams");

            serverSocket.close();
            socket.close();
            inboundFromClient.close();
            outboundToClient.close();

            logger.info("Sockets and communication streams successfully terminated");
        } catch (IOException e) {
            logger.severe(e.toString());
        }
    }
}
