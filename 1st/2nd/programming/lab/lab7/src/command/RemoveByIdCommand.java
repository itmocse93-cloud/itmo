package command;

import collectionManager.ProductCollection;
import common.Request;
import common.Response;
import database.ProductDAO;
import model.Product;

import java.sql.SQLException;
import java.util.Optional;

public class RemoveByIdCommand implements Command {

    private final ProductCollection collection;
    private final ProductDAO productDAO;

    public RemoveByIdCommand(ProductCollection collection, ProductDAO productDAO) {
        this.collection = collection;
        this.productDAO = productDAO;
    }

    @Override
    public Response execute(Request request) {
        int id = (int) request.getPayload();
        Optional<Product> existing = collection.getById(id);
        if (existing.isEmpty()) return Response.error("No product with id=" + id);

        if (!request.getLogin().equals(collection.getOwnerLogin(id))) {
            return Response.error("Access denied: you can only remove your own products.");
        }

        try {
            boolean ok = productDAO.deleteById(id, request.getLogin());
            if (ok) {
                collection.removeById(id);
                return Response.ok("Product id=" + id + " removed.");
            }
            return Response.error("Failed to remove product id=" + id);
        } catch (SQLException e) {
            return Response.error("DB error: " + e.getMessage());
        }
    }

    @Override public String getName() { return "remove_by_id"; }
    @Override public String getDescription() { return "remove_by_id {id} - remove product by ID "; }
}
