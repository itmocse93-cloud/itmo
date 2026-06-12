package command;

import collectionManager.ProductCollection;
import common.Response;
import model.Product;

/** Show command - displays all products sorted by name.
 * Usage: show */
public class ShowCommand implements Command {

    private final ProductCollection collection;

    public ShowCommand(ProductCollection collection) { this.collection = collection; }

    @Override
    public Response execute(Object payload) {
        if (collection.getProducts().isEmpty())
            return new Response(true, "Collection is empty.", collection.getSortedByName());

        StringBuilder sb = new StringBuilder("Collection (sorted by name):\n");
        collection.getSortedByName().forEach(p -> sb.append(p).append("\n"));
        return new Response(true, sb.toString(), collection.getSortedByName());
    }

    @Override public String getName()        { return "show"; }
    @Override public String getDescription() { return "show - show all products sorted by name"; }
}
