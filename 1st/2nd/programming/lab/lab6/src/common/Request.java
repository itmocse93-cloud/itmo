package common;

import java.io.Serializable;

/**
 * Serializable request sent from client to server.
 * Carries a CommandType and an optional payload object.
 */
public class Request implements Serializable {

    private static final long serialVersionUID = 1L;

    private final CommandType commandType;
    private final Object      payload;

    public Request(CommandType commandType) {
        this(commandType, null);
    }

    public Request(CommandType commandType, Object payload) {
        this.commandType = commandType;
        this.payload     = payload;
    }

    public CommandType getCommandType() { return commandType; }
    public Object      getPayload()     { return payload; }

    @Override
    public String toString() {
        return "Request{" + commandType + "}";
    }
}
