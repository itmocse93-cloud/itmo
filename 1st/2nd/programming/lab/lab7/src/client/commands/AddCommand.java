package client.commands;

import client.ProductInputReader;
import client.ServerConnection;
import common.CommandType;
import common.Request;
import common.Response;
import model.Product;

import java.io.IOException;

public class AddCommand implements ClientCommand {
    private final ServerConnection connection;
    private final ProductInputReader reader;
    private final String login;
    private final String passwordHash;

    public AddCommand(ServerConnection conn, ProductInputReader reader,
                      String login, String passwordHash) {
        this.connection = conn;
        this.reader = reader;
        this.login = login;
        this.passwordHash = passwordHash;
    }

    @Override
    public void execute(String[] args) {
        Product product = reader.readProduct();
        try {
            Response resp = connection.sendAndReceive(
                new Request(CommandType.ADD, product, login, passwordHash));
            System.out.println(resp.getMessage());
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override public String getName() { return "add"; }
    @Override public String getDescription() { return "Add a new product to the collection"; }
}
