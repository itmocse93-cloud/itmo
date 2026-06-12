package command;

import collectionManager.ProductCollection;
import model.Product;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Command to add element if it's minimum
 */
public class AddMinCommand extends AddCommand {
    public AddMinCommand(ProductCollection collection, InputStreamReader inputReader) {
        super(collection, inputReader);
    }

    @Override
    public void execute(String[] args) {
        try {
            Product product = readProduct();
            if (collection.addMin(product)) {
                System.out.println("Product added successfully with ID: " + product.getId());
            } else {
                System.out.println("Product not added - not minimum");
                // Remove the generated ID since we didn't add it
            }
        } catch (IOException e) {
            System.out.println("Error adding product: " + e.getMessage());
        }
    }
}