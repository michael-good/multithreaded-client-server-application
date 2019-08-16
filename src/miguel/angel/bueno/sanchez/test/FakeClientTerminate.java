package miguel.angel.bueno.sanchez.test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class FakeClientTerminate {

    private static final String terminateWord = "terminate";

    public FakeClientTerminate() {
    }

    public String sendTerminateToServer() {
        return terminateWord + System.lineSeparator();
    }

}
