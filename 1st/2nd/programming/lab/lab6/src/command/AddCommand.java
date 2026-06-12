package command;

import collectionManager.IdGenerator;
import collectionManager.ProductCollection;
import common.Response;
import model.Product;

/** Add command - adds a new product to the collection.
 *  Usage: add */
public class AddCommand implements Command {

    private final ProductCollection collection;

    public AddCommand(ProductCollection collection) { this.collection = collection; }

    @Override
    public Response execute(Object payload) {
        Product product = (Product) payload;
        product.setId(IdGenerator.nextProductId());
        collection.add(product);
        return new Response(true, "Product added with ID: " + product.getId(),
                collection.getSortedByName());
    }

    @Override public String getName()        { return "add"; }
    @Override public String getDescription() { return "add -add a new product to the collection"; }
}
