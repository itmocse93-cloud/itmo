package command;

import collectionManager.ProductCollection;
import common.Response;
import model.Product;

/**
 * Update command - updates a product by its ID.
 * Usage: update_by_id {id}
 */
public class UpdateCommand implements Command {

    private final ProductCollection collection;

    public UpdateCommand(ProductCollection collection) { this.collection = collection; }

    @Override
    public Response execute(Object payload) {
        Object[] data    = (Object[]) payload;
        int      id      = (int) data[0];
        Product  updated = (Product) data[1];

        Product existing = collection.getById(id);
        if (existing == null)
            return new Response(false, "Product with ID " + id + " not found.",
                    collection.getSortedByName());

        updated.setId(id);
        updated.setCreationDate(existing.getCreationDate());
        collection.removeById(id);
        collection.add(updated);
        return new Response(true, "Product with ID " + id + " updated.", collection.getSortedByName());
    }

    @Override public String getName()        { return "update_by_id"; }
    @Override public String getDescription() { return "update_by_id {id} -update product by ID"; }
}
