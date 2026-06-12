package command;

import collectionManager.IdGenerator;
import collectionManager.ProductCollection;
import common.Response;
import model.Product;

/** Add if min command - adds product only if it is the minimum. Usage: add_if_min */
public class AddIfMinCommand implements Command {

    private final ProductCollection collection;

    public AddIfMinCommand(ProductCollection collection) { this.collection = collection; }

    @Override
    public Response execute(Object payload) {
        Product product = (Product) payload;
        product.setId(IdGenerator.nextProductId());
        if (collection.addIfMin(product))
            return new Response(true,  "Product added (is minimum) with ID: " + product.getId(),
                    collection.getSortedByName());
        return new Response(false, "Product not added - it is not the minimum.",
                collection.getSortedByName());
    }

    @Override public String getName()        { return "add_if_min"; }
    @Override public String getDescription() { return "add_if_min -add product only if its price is the minimum"; }
}
