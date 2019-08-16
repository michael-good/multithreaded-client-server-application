package miguel.angel.bueno.sanchez.main;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public abstract class Client {

    protected Socket clientSocket;
    protected BufferedWriter buffer;
    private static final int port = 4000;
    private static final String host = "localhost";

    public Client() throws IOException {
        InetAddress ipAddress = getIpAddress();
        clientSocket = new Socket(ipAddress, port);
    }

    private InetAddress getIpAddress() throws UnknownHostException {
        return InetAddress.getByName(host);
    }

    public abstract void connect();

    protected void disconnect() throws IOException {
        buffer.flush();
        buffer.close();
        clientSocket.close();
    }

    protected void openBufferedWriter() throws IOException {
        OutputStream outputStream = clientSocket.getOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        buffer = new BufferedWriter(outputStreamWriter);
    }

    protected void sendMessageToServer(String message) throws IOException {
        buffer.write(message + System.lineSeparator());
    }
}
