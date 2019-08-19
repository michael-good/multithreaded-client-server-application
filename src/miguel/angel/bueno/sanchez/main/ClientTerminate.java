package miguel.angel.bueno.sanchez.main;

import java.io.IOException;
import java.net.ConnectException;

public class ClientTerminate extends Client {

    private static final String terminateWord = "terminate";

    public ClientTerminate() throws IOException {
        super();
    }

    @Override
    public void connect() {
        try {
            openBufferedWriter();
            sendTerminateToServer(terminateWord + System.lineSeparator());
        } catch (IOException e) {
            System.err.println("ERROR: Messages could not be sent to server ... " + e);
        } finally {
            try {
                disconnect();
            } catch (IOException e) {
                System.err.println("ERROR: Could not close ClientTerminate ... " + e);
            }
        }
    }

    protected void openBufferedWriter() throws IOException {
        super.openBufferedWriter();
    }

    protected void sendTerminateToServer(String message) throws IOException {
        super.sendMessageToServer(message);
    }

    protected void disconnect() throws IOException {
        super.disconnect();
    }

    public static void main(String[] args) throws IOException {
        try {
            for (int i = 0; i < 2; i++) {
                ClientTerminate client = new ClientTerminate();
                client.connect();
            }
            System.out.println("Terminating server...");
        } catch (ConnectException e) {
            System.err.println("Server closed, disconnecting all clients ... " + e);
        } catch (IOException e) {
            System.err.println("ERROR: Socket could not be initialized ... " + e);
        }
    }
}
