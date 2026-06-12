package command;

import collectionManager.ProductCollection;
import common.Response;

import java.util.Map;

/** Help command - shows all available commands.
 * Usage: help */
public class HelpCommand implements Command {

    private final ProductCollection  collection;
    private final Map<String, Command> commands;

    public HelpCommand(ProductCollection collection, Map<String, Command> commands) {
        this.collection = collection;
        this.commands   = commands;
    }


    @Override
    public Response execute(Object payload) {
        StringBuilder sb = new StringBuilder("Available commands:\n");
        commands.values().stream()
                .filter(c -> !c.getName().startsWith("server"))
                .forEach(c ->
                        sb.append(String.format(" %s%n",
                                 c.getDescription()))
                );
        sb.append(" exit -close client application\n");
        return new Response(true, sb.toString(), collection.getSortedByName());
    }
    @Override public String getName()        { return "help"; }
    @Override public String getDescription() { return "help - show this help message"; }
}