package client;

import client.commands.ClientCommand;
import client.commands.ClientCommandRegistry;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Usage: java client.Main
 */
public class Main {

    private static final String HOST = "localhost";
    private static final int PORT = 7649;
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY = 2000;

    private final ServerConnection connection;
    private final ClientCommandRegistry registry;
    private final Scanner scanner;

    public Main() {
        this.scanner = new Scanner(System.in);
        this.connection = new ServerConnection(HOST, PORT, MAX_RETRIES, RETRY_DELAY);
        this.registry = new ClientCommandRegistry(connection, new ProductInputReader(scanner));
    }

    /**
     * Starts the client application - connects to server and runs command loop.
     */
    public void start() {

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            connection.close();
            scanner.close();
        }));

        if (!connection.connect()) {
            System.out.println("Could not connect to server. Please start the server and try again.");
            return;
        }
        System.out.println("\n=== Product Collection Client ===");
        System.out.println("Type 'help' for commands, 'exit' to quit.\n");

        while (true) {
            System.out.print("> ");
            String line;
            try {
                line = scanner.nextLine().trim();
            }
            catch (NoSuchElementException e) {
                break;
            }
            if (line.isEmpty()) continue;

            String[] args = line.split("\\s+");
            String cmdName = args[0].toLowerCase();

            if (cmdName.equals("exit")) {
                break;
            }

            ClientCommand cmd = registry.resolve(cmdName);
            if (cmd == null) {
                System.out.println("Unknown command '" + cmdName + "'. Type 'help' for the list.");
                continue;
            }

            try {
                cmd.execute(args);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                reconnectIfNeeded();
            }
        }

        connection.close();
        scanner.close();
    }

    private void reconnectIfNeeded() {
        if (!connection.isConnected()) {
            System.out.println("Connection lost. Reconnecting...");
            if (connection.connect()) System.out.println("Reconnected.");
            else {
                System.out.println("Cannot reconnect. Exiting.");
                System.exit(1);
            }
        }
    }

    public static void main(String[] args) {
        new Main().start();
    }
}