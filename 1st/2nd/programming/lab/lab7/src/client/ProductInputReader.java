package client;

import model.*;

import java.util.Scanner;

public class ProductInputReader {

    private final Scanner scanner;

    public ProductInputReader(Scanner scanner) {
        this.scanner = scanner;
    }

    public Product readProduct() {
        System.out.println("--- Enter product details ---");
        String name = readNonEmpty("Name");
        float x = readFloat("Coordinate X (float)");
        int y = readBoundedInt("Coordinate Y (int, max 20)", Integer.MIN_VALUE, 20);
        Integer price = readPositiveIntOrNull("Price (positive int, Enter = null)");
        String partNumber = readNonEmpty("Part number");
        UnitOfMeasure unit = readUnitOfMeasure();
        Organization manufacturer = readManufacturerOrNull();

        return new Product(name, new Coordinates(x, y), price, partNumber, unit, manufacturer);
    }

    public Organization readManufacturerOrNull() {
        String choice = readChoice("Add manufacturer? (y/n)", new String[]{"y", "n"});
        if (choice.equals("n")) return null;
        System.out.println("--- Manufacturer ---");
        String fullName = readNonEmpty("Full name");
        String type = readNonEmpty("Type");
        long annualTurnover = readPositiveLong("Annual turnover");
        long employeesCount = readPositiveInt("Employees count");
        return new Organization(annualTurnover, fullName, type, employeesCount);
    }

    public String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            String val = scanner.nextLine().trim();
            if (!val.isEmpty()) return val;
            System.out.println("Cannot be empty.");
        }
    }

    public float readFloat(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            try {
                return Float.parseFloat(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid float.");
            }
        }
    }

    public int readBoundedInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt + ": ");
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                if (val > max) { System.out.printf("Must be less than %d.%n", max + 1); continue; }
                if (val < min) { System.out.printf("Must be >= %d.%n", min); continue; }
                return val;
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid integer.");
            }
        }
    }

    public Integer readPositiveIntOrNull(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) return null;
            try {
                int val = Integer.parseInt(input);
                if (val <= 0) { System.out.println("Must be > 0."); continue; }
                return val;
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid integer or leave empty.");
            }
        }
    }

    public int readPositiveInt(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                if (val <= 0) { System.out.println("Must be > 0."); continue; }
                return val;
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid integer.");
            }
        }
    }

    public long readPositiveLong(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            try {
                long val = Long.parseLong(scanner.nextLine().trim());
                if (val <= 0) { System.out.println("Must be > 0."); continue; }
                return val;
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
            }
        }
    }

    public UnitOfMeasure readUnitOfMeasure() {
        while (true) {
            System.out.print("Unit (KILOGRAMS/PCS/LITERS/MILLILITERS/GRAMS, Enter = null): ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) return null;
            try {
                return UnitOfMeasure.valueOf(input.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid unit. Try again.");
            }
        }
    }

    public String readChoice(String prompt, String[] options) {
        while (true) {
            System.out.print(prompt + ": ");
            String input = scanner.nextLine().trim().toLowerCase();
            for (String opt : options) {
                if (opt.equals(input)) return input;
            }
            System.out.println("Enter one of: " + String.join(", ", options));
        }
    }
}
