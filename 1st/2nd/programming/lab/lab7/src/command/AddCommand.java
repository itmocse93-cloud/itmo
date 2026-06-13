package command;

import collectionManager.ProductCollection;
import common.Request;
import common.Response;
import database.ProductDAO;
import model.Product;

import java.sql.SQLException;

public class AddCommand implements Command {

    private final ProductCollection collection;
    private final ProductDAO productDAO;

    public AddCommand(ProductCollection collection, ProductDAO productDAO) {
        this.collection = collection;
        this.productDAO = productDAO;
    }

    @Override
    public Response execute(Request request) {
        Product product = (Product) request.getPayload();
        try {
            Product saved = productDAO.insert(product, request.getLogin());
            collection.add(saved);
            return Response.ok("Product added with id=" + saved.getId());
        } catch (SQLException e) {
            return Response.error("DB error: " + e.getMessage());
        }
    }

    @Override public String getName() { return "add"; }
    @Override public String getDescription() { return "add -add a new product to the collection"; }
}
