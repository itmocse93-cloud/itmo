package client.commands;

import client.ServerConnection;
import common.CommandType;
import common.Request;
import common.Response;

import java.io.IOException;

/**
 * Handles commands that need no arguments and no extra user input.
 */
public class SimpleCommand implements ClientCommand {

    private final ServerConnection connection;
    private final CommandType      type;
    private final String           name;
    private final String           description;

    public SimpleCommand(ServerConnection connection, CommandType type,
                         String name, String description) {
        this.connection  = connection;
        this.type        = type;
        this.name        = name;
        this.description = description;
    }

    @Override
    public void execute(String[] args) {
        try {
            Response r = connection.sendAndReceive(new Request(type));
            if (r != null) System.out.println(r.getMessage());
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override public String getName()        { return name; }
    @Override public String getDescription() { return description; }
}
