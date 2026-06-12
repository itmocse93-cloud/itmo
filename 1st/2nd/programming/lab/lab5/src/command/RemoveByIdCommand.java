package command;

import collectionManager.ProductCollection;
import java.io.InputStreamReader;

/**
 * Command to remove an element by ID
 */
public class RemoveByIdCommand extends Command {
    public RemoveByIdCommand(ProductCollection collection, InputStreamReader inputReader) {
        super(collection, inputReader);
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: remove_by_id id");
            return;
        }

        try {
            int id = Integer.parseInt(args[1]);
            if (collection.removeById(id)) {
                System.out.println("Product with ID " + id + " removed");
            } else {
                System.out.println("Product with ID " + id + " not found");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format");
        }
    }
}