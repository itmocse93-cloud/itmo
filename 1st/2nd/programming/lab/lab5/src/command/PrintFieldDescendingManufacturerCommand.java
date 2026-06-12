package command;

import collectionManager.ProductCollection;
import java.io.InputStreamReader;

/**
 * Command to print manufacturer names in descending order
 */
public class PrintFieldDescendingManufacturerCommand extends Command {
    public PrintFieldDescendingManufacturerCommand(ProductCollection collection, InputStreamReader inputReader) {
        super(collection, inputReader);
    }

    @Override
    public void execute(String[] args) {
        if (collection.getProducts().isEmpty()) {
            System.out.println("Collection is empty");
            return;
        }

        System.out.println("Manufacturer names in descending order:");
        collection.printFieldDescendingManufacturer();
    }
}