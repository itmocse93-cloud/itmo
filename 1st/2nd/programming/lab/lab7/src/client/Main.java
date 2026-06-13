package client;

import client.commands.ClientCommandRegistryPublic;
import common.CommandType;
import common.Request;
import common.Response;
import util.PasswordHasher;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static final String SERVER_HOST = System.getenv().getOrDefault("SERVER_HOST", "localhost");
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ServerConnection connection = new ServerConnection(SERVER_HOST, SERVER_PORT);
        Runtime.getRuntime().addShutdownHook(new Thread(connection::close));

        System.out.println("=== Product Collection Client  ===");

        String login = null;
        String passwordHash = null;

        // ---- Auth loop ----
        while (login == null) {
            System.out.println("\n1. Login\n2. Register\n3. Exit");
            System.out.print("Choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    System.out.print("Login: ");String l = scanner.nextLine().trim();
                    System.out.print("Password: ");String p = scanner.nextLine().trim();
                    String ph = PasswordHasher.hashMD2(p);
                    try {
                        Response resp = connection.sendAndReceive(new Request(CommandType.LOGIN, l, ph));
                        System.out.println(resp.getMessage());
                        if (resp.isSuccess()) { login = l; passwordHash = ph; }
                    } catch (IOException e) {System.out.println("Connection error: " + e.getMessage());}
                }
                case "2" -> {
                    System.out.println("\n--- Registration ---");
                    System.out.print("Enter login: ");String l = scanner.nextLine().trim();
                    if (l.isEmpty()) { System.out.println("Login cannot be empty."); break; }

                    System.out.print("Enter password: ");String p = scanner.nextLine().trim();
                    if (p.isEmpty()) { System.out.println("Password cannot be empty."); break; }

                    System.out.print("Confirm password: ");
                    String p2 = scanner.nextLine().trim();
                    if (!p.equals(p2)) { System.out.println("Passwords do not match."); break; }

                    try {
                        Request req = new Request(CommandType.REGISTER, p, l, null);
                        Response resp = connection.sendAndReceive(req);
                        System.out.println(resp.getMessage());
                        if (resp.isSuccess()) {
                            login = l;
                            passwordHash = PasswordHasher.hashMD2(p);
                        }
                    } catch (IOException e) {
                        System.out.println("Connection error: " + e.getMessage());
                    }
                }
                case "3" -> { System.out.println("Goodbye."); return; }
                default -> System.out.println("Enter 1, 2, or 3.");
            }
        }

        // ---- Command loop ----
        ClientCommandRegistryPublic registry = ClientCommandRegistryPublic.build(
            connection, scanner, login, passwordHash);

        System.out.println("\nLogged in as: " + login);
        System.out.println("Type 'help' for commands, 'exit' to quit,'logout' for logging out from this account.\n");

        while (true) {
            System.out.print(login + "> ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;
            if (line.equalsIgnoreCase("exit")) { System.out.println("Goodbye."); break; }
            if (line.equalsIgnoreCase("logout")) {System.out.println("Logged out.");main(args);return;}

            String[] parts = line.split("\\s+", 2);
            String cmdName = parts[0].toLowerCase();
            String[] cmdArgs = parts.length > 1 ? parts[1].split("\\s+") : new String[0];

            var cmd = registry.getByName(cmdName);
            if (cmd != null) cmd.execute(cmdArgs);
            else System.out.println("Unknown command: " + cmdName + ". Type 'help'.");
        }
    }
}
