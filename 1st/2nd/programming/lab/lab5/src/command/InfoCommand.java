package command;

import collectionManager.ProductCollection;
import java.io.InputStreamReader;

/**
 * Command to display collection information
 */
public class InfoCommand extends Command {
    public InfoCommand(ProductCollection collection, InputStreamReader inputReader) {
        super(collection, inputReader);
    }

    @Override
    public void execute(String[] args) {
        System.out.println(collection.getInfo());
    }
}