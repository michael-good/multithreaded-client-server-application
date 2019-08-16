package miguel.angel.bueno.sanchez.main;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ClientTerminate {

    private Socket clientSocket;
    private BufferedWriter buffer;
    private static final int port = 4000;
    private static final String host = "localhost";
    private static final String terminateWord = "terminate";

    public ClientTerminate() throws IOException {
        InetAddress ipAddress = InetAddress.getByName(host);
        clientSocket = new Socket(ipAddress, port);
    }

    public void run() {
        try {
            openBufferedWriter();
            sendTerminateToServer();
        } catch (IOException e) {
            System.err.println("ERROR: Messages could not be sent to server ... " + e);
        }
    }

    private void openBufferedWriter() throws IOException {
        OutputStream outputStream = clientSocket.getOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        buffer = new BufferedWriter(outputStreamWriter);
    }

    private void sendTerminateToServer() throws IOException {
        buffer.write(terminateWord + System.lineSeparator());
        buffer.flush();
        buffer.close();
    }

    public static void main(String[] args) throws IOException {
        try {
            for (int i = 0; i < 2; i++) {
                ClientTerminate client = new ClientTerminate();
                client.run();
            }
            System.out.println("Terminating server...");
        } catch (IOException e) {
            System.err.println("Server closed, disconnecting all clients ... " + e);
        }
    }
}
