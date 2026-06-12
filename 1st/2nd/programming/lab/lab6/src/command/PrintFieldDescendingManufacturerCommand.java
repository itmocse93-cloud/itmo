package command;

import collectionManager.ProductCollection;
import common.Response;

/** Print manufacturer names in descending order.
 * Usage: print_field_descending_manufacturer */
public class PrintFieldDescendingManufacturerCommand implements Command {

    private final ProductCollection collection;

    public PrintFieldDescendingManufacturerCommand(ProductCollection collection) {
        this.collection = collection;
    }

    @Override
    public Response execute(Object payload) {
        if (collection.getProducts().isEmpty())
            return new Response(true, "Collection is empty.", collection.getSortedByName());

        StringBuilder sb = new StringBuilder("Manufacturer names (descending):\n");
        collection.getManufacturerNamesDescending().forEach(n -> sb.append(n).append("\n"));
        return new Response(true, sb.toString(), collection.getSortedByName());
    }

    @Override public String getName()        { return "print_field_descending_manufacturer"; }
    @Override public String getDescription() { return "print_field_descending_manufacturer - print manufacturer names in descending order"; }
}
