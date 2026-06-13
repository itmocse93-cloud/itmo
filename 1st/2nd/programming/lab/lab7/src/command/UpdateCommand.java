package command;

import collectionManager.ProductCollection;
import common.Request;
import common.Response;
import database.ProductDAO;
import model.Product;

import java.sql.SQLException;
import java.util.Optional;

public class UpdateCommand implements Command {

    private final ProductCollection collection;
    private final ProductDAO productDAO;

    public UpdateCommand(ProductCollection collection, ProductDAO productDAO) {
        this.collection = collection;
        this.productDAO = productDAO;
    }

    @Override
    public Response execute(Request request) {
        Object[] payload = (Object[]) request.getPayload();
        int id = (int) payload[0];
        Product updated = (Product) payload[1];

        Optional<Product> existing = collection.getById(id);
        if (existing.isEmpty()) return Response.error("No product with id=" + id);

        if (!request.getLogin().equals(collection.getOwnerLogin(id))) {
            return Response.error("Access denied: you can only modify your own products.");
        }

        try {
            updated.setId(id);
            boolean ok = productDAO.update(updated, request.getLogin());
            if (ok) {
                collection.updateById(id, updated);
                return Response.ok("Product id=" + id + " updated successfully.");
            }
            return Response.error("Update failed.");
        } catch (SQLException e) {
            return Response.error("DB error: " + e.getMessage());
        }
    }

    @Override public String getName() { return "update_by_id"; }
    @Override public String getDescription() { return "update_by_id {id} -update product by ID"; }
}
