package command;

import collectionManager.ProductCollection;
import common.CommandType;
import common.Request;
import common.Response;
import database.ProductDAO;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Routes incoming requests to the appropriate Command implementation.
 * Each command is registered once at construction — Open/Closed Principle.
 */
public class CommandDispatcher {

    private final Map<CommandType, Command> commands;

    public CommandDispatcher(ProductCollection collection, ProductDAO productDAO) {
        Map<CommandType, Command> map = new LinkedHashMap<>();

        // Mutating commands — need both collection and productDAO
        map.put(CommandType.ADD,           new AddCommand(collection, productDAO));
        map.put(CommandType.ADD_IF_MIN,    new AddIfMinCommand(collection, productDAO));
        map.put(CommandType.UPDATE,        new UpdateCommand(collection, productDAO));
        map.put(CommandType.REMOVE_BY_ID,  new RemoveByIdCommand(collection, productDAO));
        map.put(CommandType.REMOVE_LAST,   new RemoveLastCommand(collection, productDAO));
        map.put(CommandType.CLEAR,         new ClearCommand(collection, productDAO));

        // Read-only commands — collection only
        map.put(CommandType.SHOW,          new ShowCommand(collection));
        map.put(CommandType.INFO,          new InfoCommand(collection));
        map.put(CommandType.SHUFFLE,       new ShuffleCommand(collection));
        map.put(CommandType.COUNT_BY_PRICE,new CountByPriceCommand(collection));
        map.put(CommandType.PRINT_DESCENDING,
                                           new PrintDescendingCommand(collection));
        map.put(CommandType.PRINT_FIELD_DESCENDING_MANUFACTURER,
                                           new PrintFieldDescendingManufacturerCommand(collection));

        // Meta commands
        map.put(CommandType.CHECK_ID,      new CheckIdCommand(collection));
        map.put(CommandType.CHECK_OWNER, new CheckOwnerCommand(collection));
        map.put(CommandType.HELP,          new HelpCommand(Collections.unmodifiableMap(map)));
        map.put(CommandType.EXECUTE_SCRIPT, new Command() {
            @Override
            public Response execute(Request request) {
                return Response.ok("execute_script runs on client side.");
            }
            @Override public String getName() { return "execute_script"; }
            @Override public String getDescription() { return "execute_script {file_name} - run commands from a script file"; }
        });

        this.commands = Collections.unmodifiableMap(map);
    }

    public Response dispatch(Request request) {
        Command command = commands.get(request.getCommandType());
        if (command == null) return Response.error("Unknown command: " + request.getCommandType());
        return command.execute(request);
    }
}
