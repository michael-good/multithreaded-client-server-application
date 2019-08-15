package miguel.angel.bueno.sanchez.application;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ClientTerminate implements Runnable {

    private Socket clientSocket;
    private BufferedWriter buffer;
    private static final int port = 4000;
    private static final String host = "localhost";
    private static final String terminateWord = "terminate";

    public ClientTerminate() throws IOException {
        InetAddress ipAddress = InetAddress.getByName(host);
        clientSocket = new Socket(ipAddress, port);
    }

    @Override
    public void run() {
        try {
            openBufferedWriter();
            sendTerminateToServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openBufferedWriter() throws IOException {
        OutputStream outputStream = clientSocket.getOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        buffer = new BufferedWriter(outputStreamWriter);
    }

    private void sendTerminateToServer() throws IOException {
        buffer.write(terminateWord + "\n");
        buffer.flush();
    }

    public static void main(String[] args) throws IOException {
        ClientTerminate client = new ClientTerminate();
        client.run();
        ClientTerminate client1 = new ClientTerminate();
        client1.run();

    }
}
