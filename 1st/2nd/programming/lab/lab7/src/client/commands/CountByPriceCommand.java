package client.commands;

import client.ServerConnection;
import common.CommandType;
import common.Request;
import common.Response;

import java.io.IOException;

public class CountByPriceCommand implements ClientCommand {

    private final ServerConnection connection;
    private final String login;
    private final String passwordHash;

    public CountByPriceCommand(ServerConnection connection,
                               String login, String passwordHash) {
        this.connection = connection;
        this.login = login;
        this.passwordHash = passwordHash;
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: count_by_price {price}");
            return;
        }
        try {
            int price = Integer.parseInt(args[0]);
            Response resp = connection.sendAndReceive(
                    new Request(CommandType.COUNT_BY_PRICE, price, login, passwordHash));
            System.out.println(resp.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid price. Usage: count_by_price {price}");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override public String getName() { return "count_by_price"; }
    @Override public String getDescription() { return "count_by_price {price} - count products with a given price "; }
}