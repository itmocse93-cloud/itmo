package command;

import collectionManager.ProductCollection;
import collectionManager.IdGenerator;
import model.*;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.HashSet;

/**
 * Command to execute script from file
 */
public class ExecuteScriptCommand extends Command {
    private Map<String, Command> commands;
    private static HashSet<String> runningScripts = new HashSet<>();
    private static int recursionDepth = 0;
    private static final int MAX_DEPTH = 100;

    /**
     * Constructor for ExecuteScriptCommand
     * @param collection product collection
     * @param inputReader input reader
     * @param commands map of all available commands
     */
    public ExecuteScriptCommand(ProductCollection collection, InputStreamReader inputReader, Map<String, Command> commands) {
        super(collection, inputReader);
        this.commands = commands;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: execute_script file_name");
            return;
        }

        String filename = args[1];

        // Check 1: Circular dependency
        if (runningScripts.contains(filename)) {
            System.out.println("Error: Circular dependency detected! Script '" + filename + "' is already running.");
            System.out.println("Skipping to prevent infinite loop.");
            return;
        }

        // Check 2: Maximum recursion depth
        if (recursionDepth >= MAX_DEPTH) {
            System.out.println("Error: Maximum script nesting depth reached (" + MAX_DEPTH + ")");
            return;
        }

        // Mark script as running and increase depth
        runningScripts.add(filename);
        recursionDepth++;

        try (BufferedReader scriptReader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)))) {

            System.out.println("Executing script: " + filename + " (depth=" + recursionDepth + ")");
            String line;
            int lineNumber = 0;

            while ((line = scriptReader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                System.out.println("\n[" + filename + ":" + lineNumber + "] Executing: " + line);

                String[] commandArgs = line.split("\\s+");
                String commandName = commandArgs[0].toLowerCase();

                try {
                    if (commandName.equals("add")) {
                        // Read product fields
                        String name = scriptReader.readLine();
                        if (name == null) {
                            System.out.println("Error: Unexpected end of script while reading product name");
                            break;
                        }

                        float x = Float.parseFloat(scriptReader.readLine());
                        int y = Integer.parseInt(scriptReader.readLine());
                        int price = Integer.parseInt(scriptReader.readLine());
                        String partNumber = scriptReader.readLine();
                        String unitStr = scriptReader.readLine();
                        String addManufacturer = scriptReader.readLine();

                        int id = IdGenerator.generateProductId();
                        Coordinates coordinates = new Coordinates(x, y);
                        UnitOfMeasure unit = UnitOfMeasure.valueOf(unitStr);

                        Organization manufacturer = null;
                        if (addManufacturer.equalsIgnoreCase("y")) {
                            long orgId = IdGenerator.generateOrganizationId();
                            String orgName = scriptReader.readLine();
                            String orgFullName = scriptReader.readLine();
                            long turnover = Long.parseLong(scriptReader.readLine());
                            int employees = Integer.parseInt(scriptReader.readLine());

                            manufacturer = new Organization(orgId, orgName, orgFullName, turnover, employees);
                        }

                        Product product = new Product(id, name, coordinates, price, partNumber, unit, manufacturer);
                        collection.add(product);
                        System.out.println("Product added successfully with ID: " + id);

                    } else if (commandName.equals("add_if_min")) {
                        // Read product fields for add_if_min
                        String name = scriptReader.readLine();
                        if (name == null) {
                            System.out.println("Error: Unexpected end of script while reading product name");
                            break;
                        }

                        float x = Float.parseFloat(scriptReader.readLine());
                        int y = Integer.parseInt(scriptReader.readLine());
                        int price = Integer.parseInt(scriptReader.readLine());
                        String partNumber = scriptReader.readLine();
                        String unitStr = scriptReader.readLine();
                        String addManufacturer = scriptReader.readLine();

                        int id = IdGenerator.generateProductId();
                        Coordinates coordinates = new Coordinates(x, y);
                        UnitOfMeasure unit = UnitOfMeasure.valueOf(unitStr);

                        Organization manufacturer = null;
                        if (addManufacturer.equalsIgnoreCase("y")) {
                            long orgId = IdGenerator.generateOrganizationId();
                            String orgName = scriptReader.readLine();
                            String orgFullName = scriptReader.readLine();
                            long turnover = Long.parseLong(scriptReader.readLine());
                            int employees = Integer.parseInt(scriptReader.readLine());

                            manufacturer = new Organization(orgId, orgName, orgFullName, turnover, employees);
                        }

                        Product product = new Product(id, name, coordinates, price, partNumber, unit, manufacturer);

                        if (collection.addMin(product)) {
                            System.out.println("Product added successfully with ID: " + id);
                        } else {
                            System.out.println("Product not added - not minimum");
                        }

                    } else {
                        Command command = commands.get(commandName);
                        if (command != null) {
                            command.execute(commandArgs);
                        } else {
                            System.out.println("Unknown command: " + commandName);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error at line " + lineNumber + ": " + e.getMessage());
                }
            }

            System.out.println("\nScript execution completed: " + filename);

        } catch (IOException e) {
            System.out.println("Error reading script file: " + e.getMessage());
        } finally {
            // Remove script from running list and decrease depth
            runningScripts.remove(filename);
            recursionDepth--;
        }
    }
}