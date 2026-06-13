package command;

import collectionManager.ProductCollection;
import common.Request;
import common.Response;
import database.ProductDAO;
import model.Product;

import java.sql.SQLException;
import java.util.List;

public class RemoveLastCommand implements Command {

    private final ProductCollection collection;
    private final ProductDAO productDAO;

    public RemoveLastCommand(ProductCollection collection, ProductDAO productDAO) {
        this.collection = collection;
        this.productDAO = productDAO;
    }

    @Override
    public Response execute(Request request) {
        if (collection.isEmpty()) return Response.error("Collection is empty.");

        List<Product> products = collection.getProducts();
        Product last = products.get(products.size() - 1);

        if (!request.getLogin().equals(collection.getOwnerLogin(last.getId()))) {
            return Response.error("Access denied: last element belongs to another user.");
        }

        try {
            boolean ok = productDAO.deleteById(last.getId(), request.getLogin());
            if (ok) {
                collection.removeLast();
                return Response.ok("Last product (id=" + last.getId() + ") removed.");
            }
            return Response.error("Failed to remove last product.");
        } catch (SQLException e) {
            return Response.error("DB error: " + e.getMessage());
        }
    }

    @Override public String getName() { return "remove_last"; }
    @Override public String getDescription() { return "remove_last - remove the last product in the collection"; }
}
