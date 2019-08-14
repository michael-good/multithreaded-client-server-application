package miguel.angel.bueno.sanchez.application;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
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
    private static final int timeout = 10000;
    private static final String ipAddress = "localhost";
    private static final int maxNumberOfThreads = 5;
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
                    System.out.println(database);
                } else {
                    this.cancel();
                }
            }
        };
        Timer timer = new Timer("ApplicationTimer");
        timer.scheduleAtFixedRate(timerTask, 0, 10000);
        //logger.log(Level.INFO, message);
    }

    private void listen() throws IOException {
        while (!terminateReceived.get()) {
            serverSocket.setSoTimeout(timeout);
            clientSocket = serverSocket.accept();
            pool.execute(new ConnectionHandler(clientSocket));
        }
        terminateThreadPool();
        terminateServer();
    }

    private void terminateThreadPool() {
        System.out.println("Terminating Application...");
        pool.shutdown();
        try {
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                pool.shutdownNow();
                if (!pool.awaitTermination(5, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
        }
    }

    private void terminateServer() {
        Thread.currentThread().interrupt();
    }

    public InetAddress getSocketAddress() {
        return this.serverSocket.getInetAddress();
    }

    public ServerSocket getServerSocket() {
        return this.serverSocket;
    }

}
