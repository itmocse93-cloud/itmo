package client.commands;

import client.ServerConnection;
import common.CommandType;
import common.Request;
import common.Response;
import model.*;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Reads a script file on the CLIENT and sends each command to the server.
 * Usage: execute_script {file_name}
 */
public class ExecuteScriptCommand implements ClientCommand {

    private static final int MAX_DEPTH = 10;
    private static final Set<String> runningScripts = new HashSet<>();
    private static int depth = 0;

    private final ServerConnection connection;
    private final ClientCommandRegistry registry;

    public ExecuteScriptCommand(ServerConnection connection, ClientCommandRegistry registry) {
        this.connection = connection;
        this.registry = registry;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: execute_script {file_name}");
            return;
        }
        runScript(args[1]);
    }
    private void runScript(String filename) {
        if (runningScripts.contains(filename)) {
            System.out.println("Script '" + filename + "' is already being executed - skipped.");
            return;
        }
        if (depth >= MAX_DEPTH) {
            System.out.println("Maximum script nesting depth (" + MAX_DEPTH + ") reached.");
            return;
        }

        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("Script file not found: " + filename);
            return;
        }

        runningScripts.add(filename);
        depth++;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            processLines(reader, filename);
        } catch (IOException e) {
            System.out.println("Error reading script: " + e.getMessage());
        } finally {
            runningScripts.remove(filename);
            depth--;
        }
    }

    /** Reads and processes each line of the script file. */
    private void processLines(BufferedReader reader, String filename) throws IOException {
        String line;
        int lineNum = 0;
        while ((line = reader.readLine()) != null) {
            lineNum++;
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;

            System.out.println("[script:" + filename + ":" + lineNum + "] " + line);
            String[] parts = line.split("\\s+");
            String cmdName = parts[0].toLowerCase();

            try {
                dispatchScriptLine(cmdName, parts, reader);
            } catch (Exception e) {
                System.out.println("  Error at line " + lineNum + ": " + e.getMessage());
            }
        }
    }

    private void dispatchScriptLine(String cmdName, String[] parts, BufferedReader reader)
            throws IOException {
        switch (cmdName) {
            case "add": sendProduct(CommandType.ADD, readProductFromScript(reader));break;
            case "add_if_min": sendProduct(CommandType.ADD_IF_MIN, readProductFromScript(reader));break;
            case "update_by_id":
                if (parts.length >= 2) {
                    int id = Integer.parseInt(parts[1]);
                    Product updated = readProductFromScript(reader);
                    sendAndPrint(new Request(CommandType.UPDATE, new Object[]{id, updated}));
                }
                break;
            case "remove_by_id":
                if (parts.length >= 2)
                    sendAndPrint(new Request(CommandType.REMOVE_BY_ID, Integer.parseInt(parts[1])));
                break;
            case "count_by_price":
                if (parts.length >= 2) {
                    Integer price = parts[1].equalsIgnoreCase("null") ? null : Integer.parseInt(parts[1]);
                    sendAndPrint(new Request(CommandType.COUNT_BY_PRICE, price));
                }
                break;
            case "execute_script": if (parts.length >= 2) runScript(parts[1]);break;
            default:
                ClientCommand cmd = registry.resolve(cmdName);
                if (cmd != null) cmd.execute(parts);
                else System.out.println("  Unknown command: " + cmdName);
        }
    }

    private void sendProduct(CommandType type, Product product) throws IOException {
        sendAndPrint(new Request(type, product));
    }

    private void sendAndPrint(Request request) throws IOException {
        Response response = connection.sendAndReceive(request);
        if (response != null) System.out.println("  " + response.getMessage());
    }

    private Product readProductFromScript(BufferedReader reader) throws IOException {
        String name = nextLine(reader);
        float x = Float.parseFloat(nextLine(reader));
        int y = Integer.parseInt(nextLine(reader));
        String priceStr = nextLine(reader);
        Integer price = priceStr.isEmpty() ? null : Integer.parseInt(priceStr);
        String partNumber = nextLine(reader);
        String unitStr = nextLine(reader);
        UnitOfMeasure unit = unitStr.isEmpty() ? null : UnitOfMeasure.valueOf(unitStr.toUpperCase());
        String hasMfr = nextLine(reader);

        Organization manufacturer = null;
        if ("y".equalsIgnoreCase(hasMfr)) {
            String orgName = nextLine(reader);
            String fullName = nextLine(reader);
            long turnover = Long.parseLong(nextLine(reader));
            int employees = Integer.parseInt(nextLine(reader));
            manufacturer = new Organization(-1L, orgName, fullName, turnover, employees);
        }

        return new Product(-1, name, new Coordinates(x, y), price, partNumber, unit, manufacturer);
    }

    private String nextLine(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if (line == null) throw new IOException("Unexpected end of script file.");
        return line.trim();
    }

    @Override public String getName() { return "execute_script"; }

    @Override public String getDescription() { return "run commands from a script file"; }
}