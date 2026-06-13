package command;

import collectionManager.ProductCollection;
import common.Request;
import common.Response;
import database.ProductDAO;
import model.Product;

import java.sql.SQLException;
import java.util.Optional;

public class AddIfMinCommand implements Command {

    private final ProductCollection collection;
    private final ProductDAO productDAO;

    public AddIfMinCommand(ProductCollection collection, ProductDAO productDAO) {
        this.collection = collection;
        this.productDAO = productDAO;
    }

    @Override
    public Response execute(Request request) {
        Product product = (Product) request.getPayload();
        if (product.getPrice() == null) return Response.error("Price is required for add_if_min.");

        Optional<Product> currentMin = collection.getMinByPrice();
        if (currentMin.isPresent() && product.getPrice() >= currentMin.get().getPrice()) {
            return Response.ok("Product NOT added: price is not less than current minimum ("
                    + currentMin.get().getPrice() + ").");
        }

        try {
            Product saved = productDAO.insert(product, request.getLogin());
            collection.add(saved);
            return Response.ok("Product added as new minimum, id=" + saved.getId());
        } catch (SQLException e) {
            return Response.error("DB error: " + e.getMessage());
        }
    }

    @Override public String getName() { return "add_if_min"; }
    @Override public String getDescription() { return "add_if_min -add product only if its price is the minimum"; }
}
