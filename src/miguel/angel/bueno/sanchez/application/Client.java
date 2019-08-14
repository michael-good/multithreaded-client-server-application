package miguel.angel.bueno.sanchez.application;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

public class Client {

    private Socket clientSocket;
    private String randomNumberFormatted;
    private static final int port = 4000;
    private static final String host = "localhost";
    private static final int randomIntegerUpperBoundExclusive = 1000000000;

    public Client() throws IOException {
        InetAddress ipAddress = InetAddress.getByName(host);
        clientSocket = new Socket(ipAddress, port);
    }

    public void run() throws IOException {
        int randomNumber = createRandomNumber();
        addLeadingZeroesToRandomNumber(randomNumber);
        appendNewLineSequenceToFormattedRandomNumber();
        try {
            sendRandomNumberToServer();
        } catch (IOException e){
            System.err.println("There was an error writing data into buffer");
        }
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
        System.out.print(randomNumberFormatted.replace("\n", "\\n"));
    }

    private void sendRandomNumberToServer() throws IOException {
        OutputStream os = clientSocket.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(osw);
        bw.write(randomNumberFormatted);
        bw.flush();
        bw.close();
    }

    public static void main(String[] args) throws IOException {

        for (int i = 0; i < 5; i++) {
            Client client = new Client();
            client.run();
        }
    }

}
