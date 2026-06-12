package command;

import collectionManager.ProductCollection;
import common.Response;

/** Info command - shows collection metadata. Usage: info */
public class InfoCommand implements Command {

    private final ProductCollection collection;

    public InfoCommand(ProductCollection collection) { this.collection = collection; }

    @Override
    public Response execute(Object payload) {
        return new Response(true, collection.getInfo(), collection.getSortedByName());
    }

    @Override public String getName()        { return "info"; }
    @Override public String getDescription() { return "info - show collection metadata"; }
}
