package command;

import collectionManager.ProductCollection;
import model.Product;
import java.io.InputStreamReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Command to save collection to file
 */
public class SaveCommand extends Command {
    private String filename;

    /**
     * Constructor for SaveCommand
     * @param collection product collection
     * @param inputReader input reader
     * @param filename file name to save
     */
    public SaveCommand(ProductCollection collection, InputStreamReader inputReader, String filename) {
        super(collection, inputReader);
        this.filename = filename;
    }

    @Override
    public void execute(String[] args) {
        if (collection.getProducts().isEmpty()) {
            System.out.println("Collection is empty. Nothing to save.");
            return;
        }

        try (FileWriter writer = new FileWriter(filename)) {
            int savedCount = 0;

            for (Product product : collection.getProducts()) {
                writer.write(product.toCsv() + "\n");
                savedCount++;
            }

            System.out.println("Successfully saved " + savedCount + " products to " + filename);

        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }
}