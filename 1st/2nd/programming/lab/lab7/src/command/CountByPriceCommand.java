package command;

import collectionManager.ProductCollection;
import common.Request;
import common.Response;

public class CountByPriceCommand implements Command {

    private final ProductCollection collection;

    public CountByPriceCommand(ProductCollection collection) {
        this.collection = collection;
    }

    @Override
    public Response execute(Request request) {
        int price = (int) request.getPayload();
        long count = collection.countByPrice(price);
        return Response.ok("Number of products with price=" + price + ": " + count);
    }

    @Override public String getName() { return "count_by_price"; }

    @Override public String getDescription() { return "count_by_price {price} - count products with a given price "; }
}
