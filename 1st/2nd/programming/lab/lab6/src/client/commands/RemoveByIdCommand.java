package client.commands;

import client.ServerConnection;
import common.CommandType;
import common.Request;
import common.Response;

import java.io.IOException;

/**
 * Remove by ID command - removes a product by its ID.
 * Usage: remove_by_id {id}
 */
public class RemoveByIdCommand implements ClientCommand {

    private final ServerConnection connection;

    public RemoveByIdCommand(ServerConnection connection) {
        this.connection = connection;
    }

    /**
     * Sends REMOVE_BY_ID request to server with the given ID.
     */
    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: remove_by_id {id}");
            return;
        }
        try {
            int id = Integer.parseInt(args[1]);
            Response response = connection.sendAndReceive(new Request(CommandType.REMOVE_BY_ID, id));
            if (response != null) System.out.println(response.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public String getName() { return "remove_by_id"; }

    @Override
    public String getDescription() { return "remove product by ID"; }
}