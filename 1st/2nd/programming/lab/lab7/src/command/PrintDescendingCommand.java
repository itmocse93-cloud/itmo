package command;

import collectionManager.ProductCollection;
import common.Request;
import common.Response;
import model.Product;

import java.util.List;

public class PrintDescendingCommand implements Command {

    private final ProductCollection collection;

    public PrintDescendingCommand(ProductCollection collection) {
        this.collection = collection;
    }

    @Override
    public Response execute(Request request) {
        if (collection.isEmpty()) {
            return Response.ok("Collection is empty.");
        }
        List<Product> descending = collection.getSortedDescending();
        StringBuilder sb = new StringBuilder();
        descending.forEach(p -> sb.append(p).append("\n"));
        return Response.ok(sb.toString().trim());
    }

    @Override
    public String getName() { return "print_descending"; }

    @Override public String getDescription() { return "print_descending - print all products in descending price order"; }
}
