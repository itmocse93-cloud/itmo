package command;

import collectionManager.ProductCollection;
import common.Request;
import common.Response;

public class CheckIdCommand implements Command {

    private final ProductCollection collection;

    public CheckIdCommand(ProductCollection collection) {
        this.collection = collection;
    }

    @Override
    public Response execute(Request request) {
        int id = (int) request.getPayload();
        return collection.getById(id).isPresent()
                ? Response.ok("ID " + id + " exists.")
                : Response.error("No product with id=" + id);
    }

    @Override
    public String getName() { return "check_id"; }

    @Override
    public String getDescription() { return "Check if a product with given id exists"; }

    @Override
    public boolean isVisibleInHelp() { return false; }
}
