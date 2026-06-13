package command;

import collectionManager.ProductCollection;
import common.Request;
import common.Response;

public class ShuffleCommand implements Command {

    private final ProductCollection collection;

    public ShuffleCommand(ProductCollection collection) {
        this.collection = collection;
    }

    @Override
    public Response execute(Request request) {
        if (collection.isEmpty()) {
            return Response.error("Collection is empty.");
        }
        collection.shuffle();
        return Response.ok("Collection shuffled successfully.");
    }

    @Override
    public String getName() { return "shuffle"; }

    @Override public String getDescription() { return "shuffle - randomly shuffle the collection"; }
}
