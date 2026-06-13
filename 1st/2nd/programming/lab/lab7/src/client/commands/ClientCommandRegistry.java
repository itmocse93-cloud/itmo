package client.commands;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Holds all available client-side commands, keyed by name.
 * Insertion order is preserved for consistent help output.
 */
public class ClientCommandRegistry {

    private final Map<String, ClientCommand> commands = new LinkedHashMap<>();

    public void register(ClientCommand command) {
        commands.put(command.getName(), command);
    }

    public ClientCommand getByName(String name) {
        return commands.get(name);
    }

    public Collection<ClientCommand> getAll() {
        return commands.values();
    }
}
