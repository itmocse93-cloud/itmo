package command;

import collectionManager.ProductCollection;
import common.Response;
import model.Product;

/**
 * Remove by ID command - removes a product by its ID.
 * Usage: remove_by_id {id}
 */
public class RemoveLastCommand implements Command {

    private final ProductCollection collection;

    public RemoveLastCommand(ProductCollection collection) { this.collection = collection; }

    @Override
    public Response execute(Object payload) {
        if (collection.getProducts().isEmpty())
            return new Response(false, "Collection is empty.", collection.getSortedByName());

        Product last = collection.getProducts().lastElement();
        collection.removeLast();
        return new Response(true, "Removed last product (ID: " + last.getId() + ").",
                collection.getSortedByName());
    }

    @Override public String getName()        { return "remove_last"; }
    @Override public String getDescription() { return "remove_last - remove the last product in the collection"; }
}
