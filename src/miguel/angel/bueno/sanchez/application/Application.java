package miguel.angel.bueno.sanchez.application;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Application {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private Logger logger;
    private Timer timer;
    private ExecutorService pool;
    public static AtomicBoolean terminateReceived;
    public static Map<String, Integer> database;
    private int previousDatabaseSize = 0;
    private static final int timeout = 10000;
    private static final String ipAddress = "localhost";
    private static final int maxNumberOfThreads = 8;
    private static final int port = 4000;
    private static final int backlog = 50;

    public Application() throws IOException {
        serverSocket = new ServerSocket(port, backlog, InetAddress.getByName(ipAddress));
        database = new ConcurrentHashMap<>();
        pool = Executors.newFixedThreadPool(maxNumberOfThreads);
        terminateReceived = new AtomicBoolean(false);
    }

    public static void main(String[] args) throws Exception {
        Application application = new Application();
        application.initializeLogFile();
        application.initializeTimer();

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
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initializeTimer() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (!terminateReceived.get()) {
                    int numberOfDuplicates = 0;
                    for (String key : database.keySet()) {
                        if (database.get(key) > 1) {
                            numberOfDuplicates++;
                            database.put(key, 1);
                        }
                    }
                    System.out.print("Received " +
                            (database.size() - numberOfDuplicates - previousDatabaseSize) +
                            " unique numbers, ");
                    previousDatabaseSize = database.size();
                    System.out.print(numberOfDuplicates + " duplicates. ");
                    System.out.println("Unique total " + database.size());
                } else {
                    this.cancel();
                }
            }
        };
        Timer timer = new Timer("ApplicationTimer");
        timer.scheduleAtFixedRate(timerTask, 0, 10000);
    }

    private void listen() throws IOException {
        while (!terminateReceived.get()) {
            try {
                //serverSocket.setSoTimeout(timeout);
                clientSocket = serverSocket.accept();
                pool.execute(new ConnectionHandler(clientSocket));
            } catch (SocketTimeoutException e) {
                System.err.println("System timed out...");
                terminateReceived.set(true);
            }
        }
        terminateThreadPool();
        writeNumbersIntoLogFile();
        terminateServer();
    }

    private void terminateThreadPool() {
        System.err.println("Terminating Application...");
        pool.shutdown();
        try {
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                pool.shutdownNow();
                System.err.println("Pool shutting down now");
                if (!pool.awaitTermination(5, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
        }
    }

    private void writeNumbersIntoLogFile() {
        for (String key : database.keySet()) {
            logger.log(Level.INFO, key + System.lineSeparator());
        }
        System.err.println("Log generated");
    }

    private void terminateServer() {
        System.exit(0);
    }

    public InetAddress getSocketAddress() {
        return this.serverSocket.getInetAddress();
    }

    public ServerSocket getServerSocket() {
        return this.serverSocket;
    }

}
