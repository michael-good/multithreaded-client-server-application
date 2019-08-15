package miguel.angel.bueno.sanchez.application;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class ConnectionHandler implements Runnable {

    private Socket clientSocket;
    private String message;
    private BufferedReader buffer;

    public ConnectionHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            deactivateNagleAlgorithm();
            handleConnection();
        } catch (IOException e) {
            System.err.println("ERROR: Messages could not be read from client ... " + e);
        } finally {
            try {
                buffer.close();
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("ERROR: Unable to close buffer and/or socket" + e);
            }
        }
    }

    private void deactivateNagleAlgorithm() throws SocketException {
        clientSocket.setTcpNoDelay(true);
    }

    private void handleConnection() throws IOException {
        openBufferedReader();
        readMessageFromClient();
        if (!mustTerminate()) {
            addMessageToDatabase();
        }
    }

    private void openBufferedReader() throws IOException {
        InputStream inputStream = clientSocket.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        buffer = new BufferedReader(inputStreamReader);
    }

    private void readMessageFromClient() throws IOException {
        message = buffer.readLine();
    }

    private boolean mustTerminate() {
        if (isMessageReceivedTerminateMessage()) {
            Application.terminateReceived.set(true);
            return true;
        }
        return false;
    }

    private boolean isMessageReceivedTerminateMessage() {
        return (message != null && message.equalsIgnoreCase("terminate"));
    }

    private void addMessageToDatabase() {
        if (isValidMessage()) {
            Application.database.put(message, Application.database.getOrDefault(message, 0) + 1);
        }

    }

    private boolean isValidMessage() {
        return (message != null && message.length() == 9 && StringUtils.isNumeric(message));
    }

}
