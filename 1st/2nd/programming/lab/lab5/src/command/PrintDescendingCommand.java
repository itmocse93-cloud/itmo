package command;

import collectionManager.ProductCollection;
import java.io.InputStreamReader;

/**
 * Command to print elements in descending order
 */
public class PrintDescendingCommand extends Command {
    public PrintDescendingCommand(ProductCollection collection, InputStreamReader inputReader) {
        super(collection, inputReader);
    }

    @Override
    public void execute(String[] args) {
        if (collection.getProducts().isEmpty()) {
            System.out.println("Collection is empty");
            return;
        }

        System.out.println("Products in descending order:");
        collection.printDescending();
    }
}