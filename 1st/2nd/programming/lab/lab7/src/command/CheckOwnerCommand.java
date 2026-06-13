package command;

import collectionManager.ProductCollection;
import common.Request;
import common.Response;

public class CheckOwnerCommand implements Command {

    private final ProductCollection collection;

    public CheckOwnerCommand(ProductCollection collection) {
        this.collection = collection;
    }

    @Override
    public Response execute(Request request) {
        int id = (int) request.getPayload();
        String owner = collection.getOwnerLogin(id);
        if (owner == null) {
            return Response.error("No product with id=" + id);
        }
        if (!owner.equals(request.getLogin())) {
            return Response.error("Access denied: product id=" + id + " belongs to '" + owner + "'.");
        }
        return Response.ok("Owner verified.");
    }

    @Override public String getName() { return "check_owner"; }
    @Override public String getDescription() { return "Check if product belongs to current user"; }
    @Override public boolean isVisibleInHelp() { return false; }
}