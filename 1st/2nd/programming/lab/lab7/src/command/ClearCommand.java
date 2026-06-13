package command;

import collectionManager.ProductCollection;
import common.Request;
import common.Response;
import database.ProductDAO;

import java.sql.SQLException;

public class ClearCommand implements Command {

    private final ProductCollection collection;
    private final ProductDAO productDAO;

    public ClearCommand(ProductCollection collection, ProductDAO productDAO) {
        this.collection = collection;
        this.productDAO = productDAO;
    }

    @Override
    public Response execute(Request request) {
        try {
            int removed = productDAO.deleteAllByOwner(request.getLogin());
            collection.removeByOwner(request.getLogin());
            return Response.ok("Removed " + removed + " of your products from the collection.");
        } catch (SQLException e) {
            return Response.error("DB error: " + e.getMessage());
        }
    }

    @Override public String getName() { return "clear"; }
    @Override public String getDescription() { return "clear - remove all products from the collection"; }
}
