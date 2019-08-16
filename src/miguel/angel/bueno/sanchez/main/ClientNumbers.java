package miguel.angel.bueno.sanchez.main;

import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

public class ClientNumbers {

    private Socket clientSocket;
    private String randomNumberFormatted;
    private BufferedWriter buffer;
    private static final int port = 4000;
    private static final String host = "localhost";
    private static final int randomIntegerUpperBoundExclusive = 1000000000;

    public ClientNumbers() throws IOException {
        InetAddress ipAddress = InetAddress.getByName(host);
        clientSocket = new Socket(ipAddress, port);
    }

    public void run() {
        try {
            deactivateNagleAlgorithm();
            int randomNumber = createRandomNumber();
            addLeadingZeroesToRandomNumber(randomNumber);
            appendNewLineSequenceToFormattedRandomNumber();
            openBufferedWriter();
            sendRandomNumberToServer();
        } catch (IOException e) {
            System.err.println("ERROR: Messages could not be sent to server ... " + e);
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

    private void openBufferedWriter() throws IOException {
        OutputStream outputStream = clientSocket.getOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        buffer = new BufferedWriter(outputStreamWriter);
    }

    private void sendRandomNumberToServer() throws IOException {
        buffer.write(randomNumberFormatted);
        buffer.flush();
        buffer.close();
    }

    public static void main(String[] args) {
        for (;;) {
            try {
                ClientNumbers client = new ClientNumbers();
                client.run();
            } catch (ConnectException e) {
                System.err.println("Server closed, disconnecting all clients ... " + e);
                return;
            } catch (IOException e) {
                System.err.println("ERROR: Socket could not be initialized ... " + e);
            }
        }
    }
}
