package client.commands;

import client.ProductInputReader;
import client.ServerConnection;
import common.CommandType;
import common.Request;
import common.Response;
import model.Product;

import java.io.IOException;

public class AddIfMinCommandPublic implements ClientCommand {
    private final ServerConnection connection;
    private final ProductInputReader reader;
    private final String login;
    private final String passwordHash;

    public AddIfMinCommandPublic(ServerConnection conn, ProductInputReader reader,
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
            Response resp = connection.sendAndReceive(new Request(CommandType.ADD_IF_MIN, product, login, passwordHash));
            System.out.println(resp.getMessage());
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override public String getName() { return "add_if_min"; }
    @Override public String getDescription() { return "Add product if its price is less than minimum"; }
}
