package client.commands;

import client.ServerConnection;
import common.CommandType;
import common.Request;
import common.Response;

import java.io.IOException;

/**
 * Count by price command - counts products with a given price.
 * Usage: count_by_price {price}
 */
public class CountByPriceCommand implements ClientCommand {

    private final ServerConnection connection;

    public CountByPriceCommand(ServerConnection connection) {
        this.connection = connection;
    }

    /**
     * Sends COUNT_BY_PRICE request to server with the given price.
     */
    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: count_by_price {price}");
            return;
        }
        try {
            Integer price = args[1].equalsIgnoreCase("null") ? null : Integer.parseInt(args[1]);
            Response response = connection.sendAndReceive(new Request(CommandType.COUNT_BY_PRICE, price));
            if (response != null) System.out.println(response.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid price format.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public String getName() { return "count_by_price"; }

    @Override
    public String getDescription() { return "count products by price"; }
}