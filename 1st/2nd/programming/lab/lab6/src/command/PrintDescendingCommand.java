package command;

import collectionManager.ProductCollection;
import common.Response;
import model.Product;

/** Print descending command - prints products by price descending. Usage: print_descending */
public class PrintDescendingCommand implements Command {

    private final ProductCollection collection;

    public PrintDescendingCommand(ProductCollection collection) { this.collection = collection; }

    @Override
    public Response execute(Object payload) {
        if (collection.getProducts().isEmpty())
            return new Response(true, "Collection is empty.", collection.getSortedByName());

        StringBuilder sb = new StringBuilder("Products (descending by price):\n");
        collection.getDescending().forEach(p -> sb.append(p).append("\n"));
        return new Response(true, sb.toString(), collection.getSortedByName());
    }

    @Override public String getName()        { return "print_descending"; }
    @Override public String getDescription() { return "print_descending - print all products in descending price order"; }
}
