package client.commands;

import client.ProductInputReader;
import client.ServerConnection;
import common.CommandType;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Registers all client-side commands.
 */
public class ClientCommandRegistry {

    private final Map<String, ClientCommand> commands = new LinkedHashMap<>();

    public ClientCommandRegistry(ServerConnection connection, ProductInputReader inputReader) {
        // Commands that need product input
        register(new AddCommand(connection, inputReader));
        register(new AddIfMinCommand(connection, inputReader));
        register(new UpdateCommand(connection, inputReader));

        // Commands that need an ID argument
        register(new RemoveByIdCommand(connection));
        register(new CountByPriceCommand(connection));

        // Script command - needs registry reference for nested dispatch
        register(new ExecuteScriptCommand(connection, this));

        // Simple no-arg commands
        register(simple(connection, CommandType.HELP,   "help",   "show available commands"));
        register(simple(connection, CommandType.INFO,   "info",   "show collection metadata"));
        register(simple(connection, CommandType.SHOW,   "show",   "show all products sorted by name"));
        register(simple(connection, CommandType.CLEAR,  "clear",  "remove all products"));
        register(simple(connection, CommandType.REMOVE_LAST, "remove_last", "remove the last product"));
        register(simple(connection, CommandType.SHUFFLE, "shuffle", "randomly shuffle the collection"));
        register(simple(connection, CommandType.PRINT_DESCENDING,
                "print_descending", "print products in descending price order"));
        register(simple(connection, CommandType.PRINT_FIELD_DESCENDING_MANUFACTURER,
                "print_field_descending_manufacturer", "print manufacturer names in descending order"));
    }

    /** Returns command by name  */
    public ClientCommand resolve(String name) {
        return commands.get(name.toLowerCase());
    }

    /** Returns unmodifiable map of all commands. */
    public Map<String, ClientCommand> all() {
        return Collections.unmodifiableMap(commands);
    }

    private void register(ClientCommand cmd) {
        commands.put(cmd.getName(), cmd);
    }

    private SimpleCommand simple(ServerConnection conn, CommandType type,
                                 String name, String description) {
        return new SimpleCommand(conn, type, name, description);
    }
}