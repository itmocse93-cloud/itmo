package command;

import collectionManager.ProductCollection;
import java.io.InputStreamReader;

/**
 * Command to exit the program
 */
public class ExitCommand extends Command {
    public ExitCommand(ProductCollection collection, InputStreamReader inputReader) {
        super(collection, inputReader);
    }

    @Override
    public void execute(String[] args) {
        System.out.println("Exiting program...");
        System.exit(0);
    }
}