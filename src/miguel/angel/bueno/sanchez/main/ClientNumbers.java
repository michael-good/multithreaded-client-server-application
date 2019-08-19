package miguel.angel.bueno.sanchez.main;

import java.io.*;
import java.net.ConnectException;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

public class ClientNumbers extends Client {

    private String randomNumberFormatted;
    private static final int randomIntegerUpperBoundExclusive = 1000000000;

    public ClientNumbers() throws IOException {
        super();
    }

    @Override
    public void connect() {
        try {
            deactivateNagleAlgorithm();
            int randomNumber = createRandomNumber();
            addLeadingZeroesToRandomNumber(randomNumber);
            appendNewLineSequenceToFormattedRandomNumber();
            openBufferedWriter();
            sendRandomNumberToServer(randomNumberFormatted);
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

    private void deactivateNagleAlgorithm() throws SocketException {
        clientSocket.setTcpNoDelay(true);
    }

    private int createRandomNumber() {
        ThreadLocalRandom randomIntegerGenerator = ThreadLocalRandom.current();
        int randomNumber = randomIntegerGenerator.nextInt(0, randomIntegerUpperBoundExclusive);
        return randomNumber;
    }

    private void addLeadingZeroesToRandomNumber(int randomNumber) {
        DecimalFormat df = new DecimalFormat("000000000");
        randomNumberFormatted = df.format(randomNumber);
    }

    private void appendNewLineSequenceToFormattedRandomNumber() {
        String newLineSequence = System.lineSeparator();
        randomNumberFormatted += newLineSequence;
        System.out.print(randomNumberFormatted);
    }

    protected void openBufferedWriter() throws IOException {
        super.openBufferedWriter();
    }

    protected void sendRandomNumberToServer(String message) throws IOException {
        super.sendMessageToServer(message);
    }

    protected void disconnect() throws IOException {
        super.disconnect();
    }

    public static void main(String[] args) {
        for (; ; ) {
            try {
                ClientNumbers client = new ClientNumbers();
                client.connect();
            } catch (ConnectException e) {
                System.err.println("Server closed, disconnecting all clients ... " + e);
                return;
            } catch (IOException e) {
                System.err.println("ERROR: Socket could not be initialized ... " + e);
                return;
            }
        }
    }
}
