package client.commands;

public interface ClientCommand {
    void execute(String[] args);
    String getName();
    String getDescription();
}
