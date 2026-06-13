package client.commands;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class ExecuteScriptCommandPublic implements ClientCommand {
    private final ClientCommandRegistryPublic registry;
    private final Set<String> runningScripts = new HashSet<>();

    public ExecuteScriptCommandPublic(ClientCommandRegistryPublic registry) {
        this.registry = registry;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) { System.out.println("Usage: execute_script <filename>"); return; }
        String filename = args[0];
        if (runningScripts.contains(filename)) { System.out.println("Recursive call: " + filename); return; }
        File file = new File(filename);
        if (!file.exists()) { System.out.println("File not found: " + filename); return; }
        runningScripts.add(filename);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\s+", 2);
                String cmdName = parts[0];
                String[] cmdArgs = parts.length > 1 ? parts[1].split("\\s+") : new String[0];
                ClientCommand cmd = registry.getByName(cmdName);
                if (cmd != null) cmd.execute(cmdArgs);
                else System.out.println("Unknown command: " + cmdName);
            }
        } catch (IOException e) {
            System.out.println("Script error: " + e.getMessage());
        } finally {
            runningScripts.remove(filename);
        }
    }

    @Override public String getName() { return "execute_script"; }
    @Override public String getDescription() { return "Execute commands from a script file"; }
}
