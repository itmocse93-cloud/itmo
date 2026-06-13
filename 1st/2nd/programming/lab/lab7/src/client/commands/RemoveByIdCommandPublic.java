package client.commands;

import client.ServerConnection;
import common.CommandType;
import common.Request;
import common.Response;

import java.io.IOException;
import java.util.Scanner;

public class RemoveByIdCommandPublic implements ClientCommand {
    private final ServerConnection connection;
    private final Scanner scanner;
    private final String login;
    private final String passwordHash;

    public RemoveByIdCommandPublic(ServerConnection conn, Scanner scanner,
                                    String login, String passwordHash) {
        this.connection = conn;
        this.scanner = scanner;
        this.login = login;
        this.passwordHash = passwordHash;
    }

    @Override
    public void execute(String[] args) {
        System.out.print("Enter product ID to remove: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Response resp = connection.sendAndReceive(new Request(CommandType.REMOVE_BY_ID, id, login, passwordHash));
            System.out.println(resp.getMessage());
        } catch (NumberFormatException e) { System.out.println("Invalid ID.");
        } catch (IOException e) { System.out.println("Error: " + e.getMessage()); }
    }

    @Override public String getName() { return "remove_by_id"; }
    @Override public String getDescription() { return "Remove product by id"; }
}
