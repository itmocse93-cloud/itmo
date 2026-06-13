package client.commands;

import client.ServerConnection;
import common.CommandType;
import common.Request;
import common.Response;

import java.io.IOException;

/**
 * Generic command for server commands that require no additional user input.
 * Examples: show, info, shuffle, help, remove_last, clear,
 *           print_descending, print_field_descending_manufacturer
 */
public class SimpleCommand implements ClientCommand {

    private final ServerConnection connection;
    private final CommandType commandType;
    private final String name;
    private final String description;
    private final String login;
    private final String passwordHash;

    public SimpleCommand(ServerConnection connection, CommandType commandType,
                         String name, String description,
                         String login, String passwordHash) {
        this.connection = connection;
        this.commandType = commandType;
        this.name = name;
        this.description = description;
        this.login = login;
        this.passwordHash = passwordHash;
    }

    @Override
    public void execute(String[] args) {
        try {
            Response resp = connection.sendAndReceive(
                    new Request(commandType, login, passwordHash));
            System.out.println(resp.getMessage());
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public String getName() { return name; }

    @Override
    public String getDescription() { return description; }
}
