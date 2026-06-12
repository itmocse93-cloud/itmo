package command;

import collectionManager.ProductCollection;
import common.Response;

/** Count by price command - counts products with given price.
 *  Usage: count_by_price {price} */
public class CountByPriceCommand implements Command {

    private final ProductCollection collection;

    public CountByPriceCommand(ProductCollection collection) { this.collection = collection; }

    @Override
    public Response execute(Object payload) {
        Integer price = (Integer) payload;
        long    count = collection.countByPrice(price);
        return new Response(true, "Products with price " + price + ": " + count,
                collection.getSortedByName());
    }

    @Override public String getName()        { return "count_by_price"; }
    @Override public String getDescription() { return "count_by_price {price} - count products with a given price "; }
}
