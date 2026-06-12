package client;

import model.*;

import java.util.Scanner;

/**
 * Reads and validates a complete {@link Product} from the console.
 */
public class ProductInputReader {

    private final Scanner scanner;

    public ProductInputReader(Scanner scanner) {
        this.scanner = scanner;
    }

    public Product readProduct() {
        System.out.println("--- Enter product details ---");
        String        name         = readNonEmpty("Name");
        float         x            = readFloat("Coordinate X (float)");
        int           y            = readBoundedInt("Coordinate Y (int, max 20)", Integer.MIN_VALUE, 20);
        Integer       price        = readPositiveIntOrNull("Price (positive int, Enter = null)");
        String        partNumber   = readNonEmpty("Part number");
        UnitOfMeasure unit         = readUnitOfMeasure();
        Organization  manufacturer = readManufacturerOrNull();

        return new Product(-1, name, new Coordinates(x, y), price, partNumber, unit, manufacturer);
    }

    private Organization readManufacturerOrNull() {
        if (!readChoice("Add manufacturer? (y/n)", "y", "n").equals("y")) return null;
        System.out.println("--- Manufacturer ---");
        String orgName   = readNonEmpty("Name");
        String fullName  = readNonEmpty("Full name");
        long   turnover  = readPositiveLong("Annual turnover");
        int    employees = readPositiveInt("Employees count");
        return new Organization(-1L, orgName, fullName, turnover, employees);
    }

    // --- Reusable input helpers (package-visible for script reader) ---

    public String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            String v = scanner.nextLine().trim();
            if (!v.isEmpty()) return v;
            System.out.println("Cannot be empty.");
        }
    }

    public float readFloat(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            try { return Float.parseFloat(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Enter a valid float."); }
        }
    }

    public int readBoundedInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt + ": ");
            try {
                int v = Integer.parseInt(scanner.nextLine().trim());
                if (v >= min && v <= max) return v;
                if (min == Integer.MIN_VALUE) {
                    System.out.printf("Must be less than %d.%n", max);
                } else {
                    System.out.printf("Must be between %d and %d.%n", min, max);
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid integer.");
            }
        }
    }
    public Integer readPositiveIntOrNull(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            String s = scanner.nextLine().trim();
            if (s.isEmpty()) return null;
            try {
                int v = Integer.parseInt(s);
                if (v > 0) return v;
                System.out.println("Must be > 0.");
            } catch (NumberFormatException e) { System.out.println("Enter a valid integer or leave empty."); }
        }
    }

    public int readPositiveInt(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            try {
                int v = Integer.parseInt(scanner.nextLine().trim());
                if (v > 0) return v;
                System.out.println("Must be > 0.");
            } catch (NumberFormatException e) { System.out.println("Enter a valid integer."); }
        }
    }

    public long readPositiveLong(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            try {
                long v = Long.parseLong(scanner.nextLine().trim());
                if (v > 0) return v;
                System.out.println("Must be > 0.");
            } catch (NumberFormatException e) { System.out.println("Enter a valid number."); }
        }
    }

    public UnitOfMeasure readUnitOfMeasure() {
        System.out.print("Unit (KILOGRAMS/PCS/LITERS/MILLILITERS/GRAMS, Enter = null): ");
        while (true) {
            String s = scanner.nextLine().trim();
            if (s.isEmpty()) return null;
            try { return UnitOfMeasure.valueOf(s.toUpperCase()); }
            catch (IllegalArgumentException e) { System.out.print("Invalid unit. Try again: "); }
        }
    }

    public String readChoice(String prompt, String... options) {
        while (true) {
            System.out.print(prompt + ": ");
            String s = scanner.nextLine().trim().toLowerCase();
            for (String o : options) if (o.equals(s)) return s;
            System.out.println("Enter one of: " + String.join(", ", options));
        }
    }
}
