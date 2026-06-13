package client.commands;

import client.ServerConnection;
import common.CommandType;
import common.Request;
import common.Response;

import java.io.IOException;

public class RemoveByIdCommand implements ClientCommand {

    private final ServerConnection connection;
    private final String login;
    private final String passwordHash;

    public RemoveByIdCommand(ServerConnection connection,
                             String login, String passwordHash) {
        this.connection = connection;
        this.login = login;
        this.passwordHash = passwordHash;
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: remove_by_id {id}");
            return;
        }
        try {
            int id = Integer.parseInt(args[0]);
            Response resp = connection.sendAndReceive(
                    new Request(CommandType.REMOVE_BY_ID, id, login, passwordHash));
            System.out.println(resp.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Usage: remove_by_id {id}");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override public String getName() { return "remove_by_id"; }
    @Override public String getDescription() { return "Remove product by id. Usage: remove_by_id {id}"; }
}