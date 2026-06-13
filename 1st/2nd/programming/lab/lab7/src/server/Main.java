package server;

import collectionManager.ProductCollection;
import command.CommandDispatcher;
import common.CommandType;
import common.Request;
import common.Response;
import database.DatabaseManager;
import database.ProductDAO;
import database.UserDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.concurrent.*;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final int PORT = 12345;
    private static final int THREAD_POOL_SIZE = 8;

    public static void main(String[] args) {
        String dbHost     = System.getenv().getOrDefault("DB_HOST", "pg");
        String dbName     = System.getenv().getOrDefault("DB_NAME", "studs");
        String dbUser     = System.getenv().getOrDefault("DB_USER", "s470649");
        String dbPassword = System.getenv().getOrDefault("DB_PASSWORD", "");

        // ---- Database setup ----
        DatabaseManager db = new DatabaseManager(dbHost, dbName, dbUser, dbPassword);
        try {
            db.initTables();    logger.info("Tables initialized.");
        } catch (SQLException e) {
            logger.fatal("Failed to init DB: {}", e.getMessage());    System.exit(1);
        }

        ProductDAO productDAO = new ProductDAO(db);
        UserDAO userDAO = new UserDAO(db);

        // ---- Load collection into memory ----
        ProductCollection collection = new ProductCollection();
        try {
            collection.loadFromList(productDAO.loadAll());
            logger.info("Loaded {} products from DB.", collection.size());
        } catch (SQLException e) {
            logger.error("Failed to load collection: {}", e.getMessage());
        }

        CommandDispatcher dispatcher = new CommandDispatcher(collection, productDAO);

        // ---- Thread pools ----
        ExecutorService processingPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        ForkJoinPool sendPool = new ForkJoinPool(THREAD_POOL_SIZE);

        logger.info("Server starting on port {}...", PORT);

        try (ServerSocketChannel serverChannel = ServerSocketChannel.open()) {
            serverChannel.bind(new InetSocketAddress(PORT));
            serverChannel.configureBlocking(true);
            logger.info("Server ready. Waiting for connections.");

            while (true) {
                SocketChannel clientChannel = serverChannel.accept();
                logger.info("Client connected: {}", clientChannel.getRemoteAddress());

                // NEW THREAD for reading each request
                Thread readerThread = new Thread(() -> {
                    try {
                        Request request = readRequest(clientChannel);
                        if (request == null) return;

                        logger.debug("Received command: {} from user: {}",
                                request.getCommandType(), request.getLogin());

                        // FIXED THREAD POOL for process
                        processingPool.submit(() -> {
                            Response response = processRequest(request, userDAO, dispatcher);

                            // FORK JOIN POOL for sending response
                            sendPool.submit(() -> {
                                try {
                                    sendResponse(clientChannel, response);
                                    logger.debug("Response sent to: {}", request.getLogin());
                                } catch (IOException e) {
                                    logger.error("Send error: {}", e.getMessage());
                                } finally {
                                    try { clientChannel.close(); }
                                    catch (IOException ignored) {}
                                }
                            });
                        });

                    } catch (IOException e) {
                        logger.error("Read error: {}", e.getMessage());
                        try { clientChannel.close(); } catch (IOException ignored) {}
                    }
                });
                readerThread.setDaemon(true);
                readerThread.start();
            }
        } catch (IOException e) {
            logger.fatal("Server fatal error: {}", e.getMessage());
        }
    }

    private static Request readRequest(SocketChannel channel) throws IOException {
        ByteBuffer lenBuf = ByteBuffer.allocate(4);
        int read = 0;
        while (read < 4) {
            int r = channel.read(lenBuf);
            if (r == -1) return null;
            read += r;
        }
        lenBuf.flip();
        int length = lenBuf.getInt();

        ByteBuffer dataBuf = ByteBuffer.allocate(length);
        read = 0;
        while (read < length) {
            int r = channel.read(dataBuf);
            if (r == -1) return null;
            read += r;
        }
        dataBuf.flip();
        byte[] bytes = new byte[length];
        dataBuf.get(bytes);

        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return (Request) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Cannot deserialize request", e);
        }
    }

    private static Response processRequest(Request request, UserDAO userDAO,
                                            CommandDispatcher dispatcher) {
        CommandType type = request.getCommandType();

        // Handle REGISTER
        if (type == CommandType.REGISTER) {
            try {
                String login = request.getLogin();
                String rawPassword = (String) request.getPayload();
                if (userDAO.exists(login)) {
                    return Response.error("User '" + login + "' already exists.");
                }
                userDAO.register(login, rawPassword);
                logger.info("New user registered: {}", login);
                return Response.ok("User '" + login + "' registered successfully.");
            } catch (SQLException e) {
                logger.error("Registration error: {}", e.getMessage());
                return Response.error("Registration failed: " + e.getMessage());
            }
        }

        // Authenticate all other commands
        try {
            if (!userDAO.authenticate(request.getLogin(), request.getPasswordHash())) {
                logger.warn("Auth failed for user: {}", request.getLogin());
                return Response.error("Authentication failed. Wrong login or password.");
            }
        } catch (SQLException e) {
            logger.error("Auth DB error: {}", e.getMessage());
            return Response.error("Auth error: " + e.getMessage());
        }

        if (type == CommandType.LOGIN) {
            logger.info("User logged in: {}", request.getLogin());
            return Response.ok("Login successful. Welcome, " + request.getLogin() + "!");
        }

        return dispatcher.dispatch(request);
    }

    private static void sendResponse(SocketChannel channel, Response response) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(response);
        }
        byte[] bytes = baos.toByteArray();
        ByteBuffer buf = ByteBuffer.allocate(4 + bytes.length);
        buf.putInt(bytes.length);
        buf.put(bytes);
        buf.flip();
        while (buf.hasRemaining()) channel.write(buf);
    }
}
