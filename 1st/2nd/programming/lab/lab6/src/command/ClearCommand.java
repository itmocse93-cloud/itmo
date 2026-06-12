package command;

import collectionManager.ProductCollection;
import common.Response;
/**
 * Clear command - removes all products from the collection.
 * Usage: clear
 */
public class ClearCommand implements Command {

    private final ProductCollection collection;

    public ClearCommand(ProductCollection collection) { this.collection = collection; }

    @Override
    public Response execute(Object payload) {
        collection.clear();
        return new Response(true, "Collection cleared.", collection.getSortedByName());
    }

    @Override public String getName()        { return "clear"; }
    @Override public String getDescription() { return "clear - remove all products from the collection"; }
}
