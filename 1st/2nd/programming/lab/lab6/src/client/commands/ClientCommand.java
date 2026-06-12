package client.commands;

/**
 * Contract for every client-side command handler.
 */
public interface ClientCommand {

    /**
     * @param args tokens from the typed line; args[0] is the command name
     */
    void execute(String[] args);

    String getName();
    String getDescription();
}
