import collectionManager.ProductCollection;
import collectionManager.IdGenerator;
import model.Product;
import command.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Main application class for Product Collection Manager
 */
public class Main {
    private static ProductCollection collection;
    private static Map<String, Command> commands;
    private static String filename;
    private static InputStreamReader inputReader;

    /**
     * Main entry point of the application
     * @param args command line arguments containing filename
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No filename provided. Using default: data.csv");
            args = new String[]{"data.csv"};
        }

        filename = args[0];
        collection = new ProductCollection();
        inputReader = new InputStreamReader(System.in);

        try {
            ensureFileExists();
            loadCollectionFromFile();
            initializeCommands();
            printWelcomeMessage();
            runInteractiveMode();
        } catch (Exception e) {
            System.out.println("Fatal error: " + e.getMessage());
        }
    }

    /**
     * Creates file if it doesn't exist
     */
    private static void ensureFileExists() {
        File file = new File(filename);
        if (!file.exists()) {
            try {
                System.out.println("Creating new file: " + filename);
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Could not create file: " + e.getMessage());
            }
        }
    }

    /**
     * Loads collection data from CSV file using InputStreamReader
     */
    private static void loadCollectionFromFile() {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("File not found. Starting with empty collection.");
            return;
        }

        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader reader = new BufferedReader(isr)) {

            String line;
            int lineNumber = 0;
            int loadedCount = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty()) continue;

                try {
                    Product product = Product.fromCsv(line);
                    collection.add(product);
                    loadedCount++;
                } catch (Exception e) {
                    System.out.println("Error loading line " + lineNumber + ": " + e.getMessage());
                }
            }

            if (loadedCount > 0) {
                int maxProductId = collection.getMaxProductId();
                long maxOrganizationId = collection.getMaxOrganizationId();
                IdGenerator.updateGenerators(maxProductId, maxOrganizationId);
            }

            System.out.println("Loaded " + loadedCount + " products from " + filename);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Initializes all command objects
     */
    private static void initializeCommands() {
        commands = new HashMap<>();
        commands.put("help", new HelpCommand(collection, inputReader));
        commands.put("info", new InfoCommand(collection, inputReader));
        commands.put("show", new ShowCommand(collection, inputReader));
        commands.put("add", new AddCommand(collection, inputReader));
        commands.put("update", new UpdateCommand(collection, inputReader));
        commands.put("remove_by_id", new RemoveByIdCommand(collection, inputReader));
        commands.put("clear", new ClearCommand(collection, inputReader));
        commands.put("save", new SaveCommand(collection, inputReader, filename));
        commands.put("execute_script", new ExecuteScriptCommand(collection, inputReader, commands));
        commands.put("exit", new ExitCommand(collection, inputReader));
        commands.put("remove_last", new RemoveLastCommand(collection, inputReader));
        commands.put("add_if_min", new AddMinCommand(collection, inputReader));
        commands.put("shuffle", new ShuffleCommand(collection, inputReader));
        commands.put("count_by_price", new CountByPriceCommand(collection, inputReader));
        commands.put("print_descending", new PrintDescendingCommand(collection, inputReader));
        commands.put("print_field_descending_manufacturer",
                new PrintFieldDescendingManufacturerCommand(collection, inputReader));
    }

    /**
     * Displays welcome message
     */
    private static void printWelcomeMessage() {
        System.out.println("  Product Collection Manager");
        System.out.println("Type 'help' for available commands");
        System.out.println("Type 'exit' to quit");
    }

    /**
     * Runs interactive command loop
     */
    private static void runInteractiveMode() {
        BufferedReader reader = new BufferedReader(inputReader);

        while (true) {
            System.out.print("> ");
            try {
                String input = reader.readLine();
                if (input == null) break;

                input = input.trim();
                if (input.isEmpty()) continue;

                String[] parts = input.split("\\s+");
                String commandName = parts[0].toLowerCase();

                Command command = commands.get(commandName);
                if (command != null) {
                    try {
                        command.execute(parts);
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                } else {
                    System.out.println("Unknown command. Type 'help' for available commands.");
                }
            } catch (IOException e) {
                System.out.println("Input error: " + e.getMessage());
                break;
            }
        }
    }
}