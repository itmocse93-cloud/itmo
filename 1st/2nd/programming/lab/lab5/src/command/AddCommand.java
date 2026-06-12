package command;

import collectionManager.ProductCollection;
import collectionManager.IdGenerator;
import model.*;
import exception.InvalidDataException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Command to add a new element
 */
public class AddCommand extends Command {
    public AddCommand(ProductCollection collection, InputStreamReader inputReader) {
        super(collection, inputReader);
    }

    @Override
    public void execute(String[] args) {
        try {
            System.out.println("\n=== Adding New Product ===");
            Product product = readProduct();
            collection.add(product);
            System.out.println("✓ Product added successfully with ID: " + product.getId());
        } catch (Exception e) {
            System.out.println("Error adding product: " + e.getMessage());
        }
    }

    /**
     * Reads a product from input with validation
     * @return new product
     * @throws IOException if input error occurs
     */
    protected Product readProduct() throws IOException {
        int id = IdGenerator.generateProductId();
        System.out.println("Generated ID: " + id);

        String name = readString("Enter name", true);

        System.out.println("\n-- Coordinates --");
        Coordinates coordinates = readCoordinates();

        System.out.println("\n-- Price Information --");
        Integer price = readPrice();

        String partNumber = readString("Enter part number", true);

        System.out.println("\n-- Unit of Measure --");
        UnitOfMeasure unitOfMeasure = readUnitOfMeasure();

        System.out.println("\n-- Manufacturer Information --");
        System.out.print("Add manufacturer? (y/n): ");
        String addManufacturer = reader.readLine();
        Organization manufacturer = null;
        if (addManufacturer.equalsIgnoreCase("y")) {
            manufacturer = readOrganization();
        }

        return new Product(id, name, coordinates, price, partNumber, unitOfMeasure, manufacturer);
    }

    /**
     * Reads a string with validation
     */
    private String readString(String prompt, boolean required) throws IOException {
        while (true) {
            System.out.print(prompt + ": ");
            String input = reader.readLine();
            if (input == null) {
                throw new IOException("Input cancelled");
            }
            if (required && input.trim().isEmpty()) {
                System.out.println("Error: This field cannot be empty. Please try again.");
                continue;
            }
            return input;
        }
    }

    /**
     * Reads coordinates with validation
     */
    private Coordinates readCoordinates() throws IOException {
        while (true) {
            try {
                System.out.print("Enter x coordinate (float): ");
                String xStr = reader.readLine();
                float x = Float.parseFloat(xStr);

                System.out.print("Enter y coordinate (int, max 20): ");
                String yStr = reader.readLine();
                int y = Integer.parseInt(yStr);

                return new Coordinates(x, y);

            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid number format. Please enter valid numbers.");
                System.out.println("Example: x = 5.5, y = 10");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    /**
     * Reads price with validation
     */
    private Integer readPrice() throws IOException {
        while (true) {
            System.out.print("Enter price (positive number, or empty for null): ");
            String priceStr = reader.readLine();

            if (priceStr.trim().isEmpty()) {
                return null;
            }

            try {
                int price = Integer.parseInt(priceStr);
                if (price <= 0) {
                    System.out.println("Error: Price must be greater than 0");
                    continue;
                }
                return price;
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid price format. Please enter a positive integer.");
            }
        }
    }

    /**
     * Reads unit of measure with validation
     */
    protected UnitOfMeasure readUnitOfMeasure() throws IOException {
        while (true) {
            System.out.println("Available units: KILOGRAMS, PCS, LITERS, MILLILITERS, GRAMS");
            System.out.print("Enter unit of measure (or empty for null): ");
            String unitStr = reader.readLine();

            if (unitStr.trim().isEmpty()) {
                return null;
            }

            try {
                return UnitOfMeasure.valueOf(unitStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Error: Invalid unit. Please choose from the list.");
            }
        }
    }

    /**
     * Reads organization with validation
     */
    protected Organization readOrganization() throws IOException {
        while (true) {
            try {
                long id = IdGenerator.generateOrganizationId();
                System.out.println("Generated Organization ID: " + id);

                System.out.print("Enter organization name: ");
                String name = reader.readLine();
                if (name.trim().isEmpty()) {
                    System.out.println("Error: Organization name cannot be empty");
                    continue;
                }

                System.out.print("Enter full name: ");
                String fullName = reader.readLine();
                if (fullName.trim().isEmpty()) {
                    System.out.println("Error: Full name cannot be empty");
                    continue;
                }

                System.out.print("Enter annual turnover (positive number): ");
                String turnoverStr = reader.readLine();
                long annualTurnover = Long.parseLong(turnoverStr);
                if (annualTurnover <= 0) {
                    System.out.println("Error: Annual turnover must be greater than 0");
                    continue;
                }

                System.out.print("Enter employees count (positive number): ");
                String employeesStr = reader.readLine();
                int employeesCount = Integer.parseInt(employeesStr);
                if (employeesCount <= 0) {
                    System.out.println("Error: Employees count must be greater than 0");
                    continue;
                }

                return new Organization(id, name, fullName, annualTurnover, employeesCount);

            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid number format. Please enter valid numbers.");
            }
        }
    }
}