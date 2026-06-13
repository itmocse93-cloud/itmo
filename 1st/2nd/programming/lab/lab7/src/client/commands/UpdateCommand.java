package client.commands;

import client.ProductInputReader;
import client.ServerConnection;
import common.CommandType;
import common.Request;
import common.Response;
import model.Product;

import java.io.IOException;

public class UpdateCommand implements ClientCommand {

    private final ServerConnection connection;
    private final ProductInputReader reader;
    private final String login;
    private final String passwordHash;

    public UpdateCommand(ServerConnection connection, ProductInputReader reader,
                         String login, String passwordHash) {
        this.connection = connection;
        this.reader = reader;
        this.login = login;
        this.passwordHash = passwordHash;
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: update {id}");
            return;
        }
        int id;
        try {
            id = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Usage: update {id}");
            return;
        }

        if (!checkOwner(id)) return;

        Product updated = reader.readProduct();
        try {
            Response resp = connection.sendAndReceive(
                    new Request(CommandType.UPDATE, new Object[]{id, updated}, login, passwordHash));
            System.out.println(resp.getMessage());
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private boolean checkOwner(int id) {
        try {
            Response resp = connection.sendAndReceive(
                    new Request(CommandType.CHECK_OWNER, id, login, passwordHash));
            if (resp.isSuccess()) {
                System.out.println(resp.getMessage());
                return true;
            } else {
                System.out.println(resp.getMessage());
                return false;
            }
        } catch (IOException e) {
            System.out.println("Error checking product: " + e.getMessage());
            return false;
        }
    }


    @Override public String getName() { return "update_by_id"; }
    @Override public String getDescription() { return "Update product by id. Usage: update {id}"; }
}