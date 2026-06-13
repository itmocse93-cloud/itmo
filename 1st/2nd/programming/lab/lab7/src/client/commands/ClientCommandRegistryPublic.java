package client.commands;

import client.ProductInputReader;
import client.ServerConnection;
import common.CommandType;

import java.util.Scanner;

/**
 * Factory that builds and wires all client commands for an authenticated user.
 * Each command gets the connection and credentials it needs — nothing more.
 */
public class ClientCommandRegistryPublic {

    private final ClientCommandRegistry registry = new ClientCommandRegistry();

    private ClientCommandRegistryPublic() {}

    public ClientCommand getByName(String name) {
        return registry.getByName(name);
    }

    public static ClientCommandRegistryPublic build(ServerConnection connection,
                                                     Scanner scanner,
                                                     String login,
                                                     String passwordHash) {
        ClientCommandRegistryPublic pub = new ClientCommandRegistryPublic();
        ProductInputReader reader = new ProductInputReader(scanner);
        ClientCommandRegistry reg = pub.registry;

        // Commands that need product input
        reg.register(new AddCommand(connection, reader, login, passwordHash));
        reg.register(new AddIfMinCommand(connection, reader, login, passwordHash));
        reg.register(new UpdateCommand(connection, reader, login, passwordHash));

        // Commands that need only an ID or price
        reg.register(new RemoveByIdCommand(connection, login, passwordHash));
        reg.register(new CountByPriceCommand(connection, login, passwordHash));

        // Simple commands — no extra user input required
        reg.register(new SimpleCommand(connection, CommandType.REMOVE_LAST,
                "remove_last",   "Remove last product (only if it's yours)",          login, passwordHash));
        reg.register(new SimpleCommand(connection, CommandType.CLEAR,
                "clear",         "Remove all your products from the collection",       login, passwordHash));
        reg.register(new SimpleCommand(connection, CommandType.SHOW,
                "show",          "Show all products in the collection",                login, passwordHash));
        reg.register(new SimpleCommand(connection, CommandType.INFO,
                "info",          "Show collection type, date and size",                login, passwordHash));
        reg.register(new SimpleCommand(connection, CommandType.SHUFFLE,
                "shuffle",       "Shuffle elements of the collection",                 login, passwordHash));
        reg.register(new SimpleCommand(connection, CommandType.PRINT_DESCENDING,
                "print_descending",
                "Print all products in descending order",                              login, passwordHash));
        reg.register(new SimpleCommand(connection, CommandType.PRINT_FIELD_DESCENDING_MANUFACTURER,
                "print_field_descending_manufacturer",
                "Print manufacturer names in descending order",                        login, passwordHash));
        reg.register(new SimpleCommand(connection, CommandType.HELP,
                "help",          "Show all available commands",                        login, passwordHash));

        // Script execution — added last so it can reference all commands above
        reg.register(new ExecuteScriptCommand(reg));

        return pub;
    }
}
