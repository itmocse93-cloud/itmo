package server;

import collectionManager.ProductCollection;
import command.CommandDispatcher;
import util.LoggerConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Scanner;
import java.util.NoSuchElementException;

public class Main {

    private static final int PORT = 7649;
    private final RequestHandler handler;
    private final CommandDispatcher dispatcher;
    private final String filename;
    private volatile boolean running = true;

    public Main(String filename) {
        this.filename = filename;

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            running = false;
            System.out.println("\nServer shutting down...");
            LoggerConfig.logServerShutdown();
        }));

        ProductCollection collection = new ProductCollection();
        new CollectionFileManager(filename, collection).load();
        this.dispatcher = new CommandDispatcher(collection, filename);
        this.handler = new RequestHandler(dispatcher);
    }

    public void start() {
        LoggerConfig.logServerStart(PORT, filename);

        Thread consoleThread = new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (true) {
                String line;
                try {
                    line = sc.nextLine().trim().toLowerCase();
                } catch (NoSuchElementException e) {
                    System.out.println("\nServer shutting down...");
                    System.exit(0);
                    break;
                }
                if (line.equals("save")) {
                    System.out.println(dispatcher.save().getMessage());
                }
            }
        });
        consoleThread.setDaemon(true);
        consoleThread.start();

        try (Selector selector = Selector.open();
             ServerSocketChannel serverChannel = ServerSocketChannel.open()) {

            serverChannel.bind(new InetSocketAddress(PORT));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            LoggerConfig.logServerReady();

            while (running) {
                selector.select(1000);
                processSelectedKeys(selector, serverChannel);
            }

        } catch (IOException e) {
            LoggerConfig.logError("Server", "Fatal error", e);
        }
    }

    private void processSelectedKeys(Selector selector, ServerSocketChannel serverChannel)
            throws IOException {
        Iterator<SelectionKey> it = selector.selectedKeys().iterator();
        while (it.hasNext()) {
            SelectionKey key = it.next();
            it.remove();
            if (!key.isValid()) continue;
            if (key.isAcceptable()) acceptConnection(selector, serverChannel);
            else if (key.isReadable()) handleRead(key);
        }
    }

    private void acceptConnection(Selector selector, ServerSocketChannel serverChannel)
            throws IOException {
        SocketChannel client = serverChannel.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        LoggerConfig.logConnected(client.getRemoteAddress().toString());
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        boolean alive = handler.handle(channel);
        if (!alive) channel.close();
    }

    public static void main(String[] args) {
        String filename = args.length > 0 ? args[0] : "data.csv";
        System.out.println("=== Product Collection Server ===");
        new Main(filename).start();
    }
}