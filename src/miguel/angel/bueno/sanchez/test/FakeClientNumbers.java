package miguel.angel.bueno.sanchez.test;

import miguel.angel.bueno.sanchez.main.ClientNumbers;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

public class FakeClientNumbers {

    public static final int randomIntegerUpperBoundExclusive = 1000000000;

    public FakeClientNumbers() {
    }

    public int createRandomNumber() {
        ThreadLocalRandom randomIntegerGenerator = ThreadLocalRandom.current();
        int randomNumber = randomIntegerGenerator.nextInt(0, randomIntegerUpperBoundExclusive);
        return randomNumber;
    }

    public String addLeadingZeroesToRandomNumber(int randomNumber) {
        DecimalFormat df = new DecimalFormat("000000000");
        String randomNumberFormatted = df.format(randomNumber);
        return randomNumberFormatted;
    }

    public String appendNewLineSequenceToFormattedRandomNumber(String randomNumberFormatted) {
        String newLineSequence = System.lineSeparator();
        randomNumberFormatted += newLineSequence;
        return randomNumberFormatted;
    }

}
