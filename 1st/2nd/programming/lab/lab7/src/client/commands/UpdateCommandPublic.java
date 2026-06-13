package client.commands;

import client.ProductInputReader;
import client.ServerConnection;
import common.CommandType;
import common.Request;
import common.Response;
import model.Product;

import java.io.IOException;
import java.util.Scanner;

public class UpdateCommandPublic implements ClientCommand {
    private final ServerConnection connection;
    private final ProductInputReader reader;
    private final Scanner scanner;
    private final String login;
    private final String passwordHash;

    public UpdateCommandPublic(ServerConnection conn, ProductInputReader reader, Scanner scanner,
                                String login, String passwordHash) {
        this.connection = conn;
        this.reader = reader;
        this.scanner = scanner;
        this.login = login;
        this.passwordHash = passwordHash;
    }

    @Override
    public void execute(String[] args) {
        System.out.print("Enter product ID to update: ");
        int id;
        try { id = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("Invalid ID."); return; }

        try {
            Response check = connection.sendAndReceive(new Request(CommandType.CHECK_ID, id, login, passwordHash));
            if (!check.isSuccess()) { System.out.println(check.getMessage()); return; }
        } catch (IOException e) { System.out.println("Error: " + e.getMessage()); return; }

        Product updated = reader.readProduct();
        try {
            Response resp = connection.sendAndReceive(
                new Request(CommandType.UPDATE, new Object[]{id, updated}, login, passwordHash));
            System.out.println(resp.getMessage());
        } catch (IOException e) { System.out.println("Error: " + e.getMessage()); }
    }

    @Override public String getName() { return "update"; }
    @Override public String getDescription() { return "Update a product by id"; }
}
