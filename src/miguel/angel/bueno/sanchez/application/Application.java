package miguel.angel.bueno.sanchez.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Application {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private Logger logger;
    private String message;
    private BufferedReader buffer;
    private Timer timer;
    public static Map<String, Integer> database;
    public static final String ipAddress = "localhost";
    public static final int port = 4000;
    public static final int backlog = 50;

    public Application() throws IOException {
        serverSocket = new ServerSocket(port, backlog, InetAddress.getByName(ipAddress));
        database = new ConcurrentHashMap<>();
    }

    public static void main(String[] args) throws Exception {
        Application application = new Application();
        application.initializeLogFile();
        //application.initializeTimer();

        System.out.println(System.lineSeparator() + "Running Server: " +
                "Host=" + application.getSocketAddress().getHostAddress() +
                " Port=" + application.port);

        application.listen();
    }

    public void initializeLogFile() {
        logger = Logger.getLogger(Application.class.getName());
        FileHandler fileHandler;
        try {
            fileHandler = new FileHandler("numbers.log");
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            //logger.setUseParentHandlers(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initializeTimer() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("System executing timer task");
            }
        };
        Timer timer = new Timer("ApplicationTimer");
        timer.scheduleAtFixedRate(timerTask, 30, 3000);
    }

    private void listen() throws IOException {
        while (true) {
            clientSocket = serverSocket.accept();
            handleConnection();
        }
    }

    private void handleConnection() throws IOException {
        openBufferedReader();
        readMessageFromClientAndLog();
    }

    private void openBufferedReader() throws IOException {
        InputStream inputStream = clientSocket.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        buffer = new BufferedReader(inputStreamReader);
    }

    private void readMessageFromClientAndLog() throws IOException {
        message = buffer.readLine();
        if (bufferIsNotEmptyAndMessageLengthEqualsNine()) {
            logger.log(Level.INFO, message + System.lineSeparator());
        }
    }

    private boolean bufferIsNotEmptyAndMessageLengthEqualsNine() {
        return (message != null && message.length() == 9);

    }

    public InetAddress getSocketAddress() {
        return this.serverSocket.getInetAddress();
    }

}
