package command;

import collectionManager.ProductCollection;
import model.Product;
import java.io.InputStreamReader;

/**
 * Command to display all elements
 */
public class ShowCommand extends Command {
    public ShowCommand(ProductCollection collection, InputStreamReader inputReader) {
        super(collection, inputReader);
    }

    @Override
    public void execute(String[] args) {
        if (collection.getProducts().isEmpty()) {
            System.out.println("Collection is empty");
            return;
        }

        for (Product product : collection.getProducts()) {
            System.out.println(product);
        }
    }
}