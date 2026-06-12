package command;

import collectionManager.ProductCollection;
import java.io.InputStreamReader;

/**
 * Command to count elements by price
 */
public class CountByPriceCommand extends Command {
    public CountByPriceCommand(ProductCollection collection, InputStreamReader inputReader) {
        super(collection, inputReader);
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: count_by_price price");
            return;
        }

        try {
            String priceStr = args[1];
            Integer price = priceStr.equals("null") ? null : Integer.parseInt(priceStr);
            long count = collection.countByPrice(price);
            System.out.println("Number of products with price " + price + ": " + count);
        } catch (NumberFormatException e) {
            System.out.println("Invalid price format");
        }
    }
}