package command;

import collectionManager.ProductCollection;
import java.io.InputStreamReader;

/**
 * Command to remove the last element
 */
public class RemoveLastCommand extends Command {
    public RemoveLastCommand(ProductCollection collection, InputStreamReader inputReader) {
        super(collection, inputReader);
    }

    @Override
    public void execute(String[] args) {
        if (collection.getProducts().isEmpty()) {
            System.out.println("Collection is empty");
            return;
        }

        collection.removeLast();
        System.out.println("Last element removed");
    }
}