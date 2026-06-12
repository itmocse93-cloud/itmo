package command;

import collectionManager.ProductCollection;
import java.io.InputStreamReader;

/**
 * Command to clear the collection
 */
public class ClearCommand extends Command {
    public ClearCommand(ProductCollection collection, InputStreamReader inputReader) {
        super(collection, inputReader);
    }

    @Override
    public void execute(String[] args) {
        collection.clear();
        System.out.println("Collection cleared");
    }
}