package command;

import collectionManager.ProductCollection;
import collectionManager.IdGenerator;
import model.*;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Command to update an element by ID
 */
public class UpdateCommand extends AddCommand {
    public UpdateCommand(ProductCollection collection, InputStreamReader inputReader) {
        super(collection, inputReader);
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: update id");
            return;
        }

        try {
            int id = Integer.parseInt(args[1]);
            Product existing = collection.getById(id);

            if (existing == null) {
                System.out.println("Product with ID " + id + " not found");
                return;
            }

            System.out.println("Updating product with ID: " + id);

            // Read new values
            System.out.print("Enter name (or press Enter to keep '" + existing.getName() + "'): ");
            String name = reader.readLine();
            if (name.trim().isEmpty()) name = existing.getName();

            System.out.print("Enter x coordinate (or press Enter to keep " + existing.getCoordinates().getX() + "): ");
            String xStr = reader.readLine();
            float x = xStr.trim().isEmpty() ? existing.getCoordinates().getX() : Float.parseFloat(xStr);

            System.out.print("Enter y coordinate (or press Enter to keep " + existing.getCoordinates().getY() + "): ");
            String yStr = reader.readLine();
            int y = yStr.trim().isEmpty() ? existing.getCoordinates().getY() : Integer.parseInt(yStr);

            System.out.print("Enter price (or press Enter to keep " + existing.getPrice() + "): ");
            String priceStr = reader.readLine();
            Integer price = priceStr.trim().isEmpty() ? existing.getPrice() :
                    (priceStr.isEmpty() ? null : Integer.parseInt(priceStr));

            System.out.print("Enter part number (or press Enter to keep '" + existing.getPartNumber() + "'): ");
            String partNumber = reader.readLine();
            if (partNumber.trim().isEmpty()) partNumber = existing.getPartNumber();

            // Create updated product with same ID
            Coordinates coordinates = new Coordinates(x, y);
            Product updated = new Product(id, name, coordinates, price, partNumber,
                    existing.getUnitOfMeasure(), existing.getManufacturer());
            updated.setCreationDate(existing.getCreationDate());

            // Update collection
            collection.removeById(id);
            collection.add(updated);

            System.out.println("Product with ID " + id + " updated successfully");

        } catch (NumberFormatException e) {
            System.out.println("Invalid number format");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}