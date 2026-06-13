package command;

import collectionManager.ProductCollection;
import common.Request;
import common.Response;

public class ShowCommand implements Command {

    private final ProductCollection collection;

    public ShowCommand(ProductCollection collection) {
        this.collection = collection;
    }

    @Override
    public Response execute(Request request) {
        if (collection.isEmpty()) {
            return Response.ok("Collection is empty.");
        }
        StringBuilder sb = new StringBuilder();
        collection.getProducts().forEach(p -> sb.append(p).append("\n"));
        return Response.ok(sb.toString().trim());
    }

    @Override
    public String getName() { return "show"; }

    @Override public String getDescription() { return "show - show all products sorted by name"; }

}
