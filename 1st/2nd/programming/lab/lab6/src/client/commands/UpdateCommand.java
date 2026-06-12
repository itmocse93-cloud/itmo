package client.commands;

import client.ProductInputReader;
import client.ServerConnection;
import common.CommandType;
import common.Request;
import common.Response;
import model.Product;

import java.io.IOException;

/** Update command - updates a product by its ID.
 * Usage: update_by_id {id} */
public class UpdateCommand implements ClientCommand {

    private final ServerConnection connection;
    private final ProductInputReader inputReader;

    public UpdateCommand(ServerConnection connection, ProductInputReader inputReader) {
        this.connection = connection;
        this.inputReader = inputReader;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: update_by_id {id}");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID.");
            return;
        }

        try {
            // Check ID exists using sendAndReceive (which has retry logic)
            Response check = connection.sendAndReceive(new Request(CommandType.CHECK_ID, id));
            if (check == null || !check.isSuccess()) {
                System.out.println("Product ID " + id + " not found. Use 'show' to see existing IDs.");
                return;
            }
            System.out.println("Product found - enter new details:");
            Product updated = inputReader.readProduct();
            Response response = connection.sendAndReceive(
                    new Request(CommandType.UPDATE, new Object[]{id, updated}));
            if (response != null) System.out.println(response.getMessage());
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public String getName() { return "update_by_id"; }

    @Override
    public String getDescription() { return "update a product by ID"; }
}