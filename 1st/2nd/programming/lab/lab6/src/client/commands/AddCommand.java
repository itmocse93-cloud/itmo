package client.commands;

import client.ProductInputReader;
import client.ServerConnection;
import common.CommandType;
import common.Request;
import common.Response;
import model.Product;

import java.io.IOException;

/**
 * Add command - adds a new product to the collection.
 * Usage: add
 */
public class AddCommand implements ClientCommand {

    private final ServerConnection connection;
    private final ProductInputReader inputReader;

    public AddCommand(ServerConnection connection, ProductInputReader inputReader) {
        this.connection = connection;
        this.inputReader = inputReader;
    }

    /**
     * Reads product from user and sends ADD request to server.
     * @param args command arguments
     */
    @Override
    public void execute(String[] args) {
        try {
            Product product = inputReader.readProduct();
            Response response = connection.sendAndReceive(new Request(CommandType.ADD, product));
            if (response != null) System.out.println(response.getMessage());
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public String getName() { return "add"; }

    @Override
    public String getDescription() { return "add a new product"; }
}