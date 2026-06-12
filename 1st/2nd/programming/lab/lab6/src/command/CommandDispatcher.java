package command;

import collectionManager.ProductCollection;
import common.CommandType;
import common.Request;
import common.Response;
import model.Product;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Routes requests to the correct Command object.
 */
public class CommandDispatcher {

    private final Map<CommandType, Command> byType = new LinkedHashMap<>();
    private final Map<String, Command> byName = new LinkedHashMap<>();
    private final ProductCollection collection;

    public CommandDispatcher(ProductCollection collection, String filename) {
        this.collection = collection;
        register(CommandType.ADD, new AddCommand(collection));
        register(CommandType.ADD_IF_MIN, new AddIfMinCommand(collection));
        register(CommandType.UPDATE, new UpdateCommand(collection));
        register(CommandType.REMOVE_BY_ID, new RemoveByIdCommand(collection));
        register(CommandType.REMOVE_LAST, new RemoveLastCommand(collection));
        register(CommandType.CLEAR, new ClearCommand(collection));
        register(CommandType.SHOW, new ShowCommand(collection));
        register(CommandType.INFO, new InfoCommand(collection));
        register(CommandType.SHUFFLE, new ShuffleCommand(collection));
        register(CommandType.COUNT_BY_PRICE, new CountByPriceCommand(collection));
        register(CommandType.PRINT_DESCENDING, new PrintDescendingCommand(collection));
        register(CommandType.PRINT_FIELD_DESCENDING_MANUFACTURER, new PrintFieldDescendingManufacturerCommand(collection));
        register(CommandType.SERVER_SAVE, new ServerSaveCommand(collection, filename));
        register(CommandType.HELP, new HelpCommand(collection, Collections.unmodifiableMap(byName)));


        byName.put("execute_script", new Command() {
            @Override public Response execute(Object payload) {
                return new Response(false, "execute_script runs on client side.");
            }
            @Override public String getName() { return "execute_script"; }
            @Override public String getDescription() { return "execute_script {file_name} - run commands from a script file"; }
        });
    }


    public Response execute(Request request) {
        if (request.getCommandType() == CommandType.CHECK_ID) {
            int id = (int) request.getPayload();
            Product found = collection.getById(id);
            return new Response(found != null, found != null ? "exists" : "not found");
        }

        Command command = byType.get(request.getCommandType());
        if (command == null) {
            return new Response(false, "Unknown command: " + request.getCommandType(),
                    collection.getSortedByName());
        }

        try {
            return command.execute(request.getPayload());
        } catch (Exception e) {
            return new Response(false, "Command error: " + e.getMessage(),
                    collection.getSortedByName());
        }
    }

    /** Server-only save command. */
    public Response save() {
        Command cmd = byType.get(CommandType.SERVER_SAVE);
        return cmd != null ? cmd.execute(null) : new Response(false, "Save not available", collection.getSortedByName());
    }

    private void register(CommandType type, Command command) {
        byType.put(type, command);
        byName.put(command.getName(), command);
    }
}