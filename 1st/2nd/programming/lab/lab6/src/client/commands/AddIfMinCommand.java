package client.commands;

import client.ProductInputReader;
import client.ServerConnection;
import common.CommandType;
import common.Request;
import common.Response;
import model.Product;

import java.io.IOException;

/**
 * Add if min command - adds product only if its price is the minimum.
 * Usage: add_if_min
 */
public class AddIfMinCommand implements ClientCommand {

    private final ServerConnection connection;
    private final ProductInputReader inputReader;

    public AddIfMinCommand(ServerConnection connection, ProductInputReader inputReader) {
        this.connection = connection;
        this.inputReader = inputReader;
    }

    /**
     * Reads product from user and sends ADD_IF_MIN request to server.
     * @param args command arguments (not used)
     */
    @Override
    public void execute(String[] args) {
        try {
            Product product = inputReader.readProduct();
            Response response = connection.sendAndReceive(new Request(CommandType.ADD_IF_MIN, product));
            if (response != null) System.out.println(response.getMessage());
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public String getName() { return "add_if_min"; }

    @Override
    public String getDescription() { return "add product only if it is the minimum"; }
}