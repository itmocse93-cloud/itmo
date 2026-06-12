package command;

import common.Response;

public interface Command {

    /**
     * Executes the command with the given payload.
     *
     * @param payload command-specific data (Product, Integer, Object[], etc.)
     * @return response to send back to the client
     */
    Response execute(Object payload);
    String getName();

    String getDescription();
    default boolean isVisibleInHelp() { return true; }
}
