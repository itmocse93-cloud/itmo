package command;

import collectionManager.ProductCollection;
import java.io.InputStreamReader;

/**
 * Command to shuffle the collection
 */
public class ShuffleCommand extends Command {
    public ShuffleCommand(ProductCollection collection, InputStreamReader inputReader) {
        super(collection, inputReader);
    }

    @Override
    public void execute(String[] args) {
        collection.shuffle();
        System.out.println("Collection shuffled");
    }
}