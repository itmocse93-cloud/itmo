package common;

import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    private CommandType commandType;
    private Object payload;
    private String login;
    private String passwordHash; // MD2 hashed password

    public Request(CommandType commandType) {
        this.commandType = commandType;
    }

    public Request(CommandType commandType, Object payload) {
        this.commandType = commandType;
        this.payload = payload;
    }

    public Request(CommandType commandType, Object payload, String login, String passwordHash) {
        this.commandType = commandType;
        this.payload = payload;
        this.login = login;
        this.passwordHash = passwordHash;
    }

    public Request(CommandType commandType, String login, String passwordHash) {
        this.commandType = commandType;
        this.login = login;
        this.passwordHash = passwordHash;
    }

    public CommandType getCommandType() { return commandType; }
    public Object getPayload() { return payload; }
    public String getLogin() { return login; }
    public String getPasswordHash() { return passwordHash; }

    public void setLogin(String login) { this.login = login; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setPayload(Object payload) { this.payload = payload; }
}
