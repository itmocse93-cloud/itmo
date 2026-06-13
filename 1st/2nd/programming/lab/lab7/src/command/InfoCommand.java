package command;

import collectionManager.ProductCollection;
import common.Request;
import common.Response;

public class InfoCommand implements Command {

    private final ProductCollection collection;

    public InfoCommand(ProductCollection collection) {
        this.collection = collection;
    }

    @Override
    public Response execute(Request request) {
        return Response.ok(collection.getInfo());
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "info - show collection metadata";
    }
}