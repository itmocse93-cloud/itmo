package command;

import collectionManager.ProductCollection;
import common.Response;

/**
 * Remove by ID command - removes a product by its ID.
 * Usage: remove_by_id {id}
 */
public class RemoveByIdCommand implements Command {

    private final ProductCollection collection;

    public RemoveByIdCommand(ProductCollection collection) {
        this.collection = collection; }

    @Override
    public Response execute(Object payload) {
        int id = (int) payload;
        if (collection.removeById(id))
            return new Response(true,  "Product with ID " + id + " removed.", collection.getSortedByName());
        return new Response(false, "Product with ID " + id + " not found.",  collection.getSortedByName());
    }

    @Override public String getName()        { return "remove_by_id"; }
    @Override public String getDescription() { return "remove_by_id {id} - remove product by ID "; }
}
