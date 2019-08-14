package miguel.angel.bueno.sanchez.application;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ClientTerminate implements Runnable {

    private Socket clientSocket;
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
            sendRandomNumberToServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRandomNumberToServer() throws IOException {
        OutputStream os = clientSocket.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(osw);
        bw.write(terminateWord + "\n");
        bw.flush();
    }

    public static void main(String[] args) throws IOException {
        ClientTerminate client = new ClientTerminate();
        client.run();
        client.run();

    }
}
