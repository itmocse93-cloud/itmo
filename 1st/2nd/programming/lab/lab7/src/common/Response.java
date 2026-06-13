package common;

import java.io.Serializable;

public class Response implements Serializable {
    private static final long serialVersionUID = 1L;

    private String message;
    private boolean success;

    public Response(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public static Response ok(String message) {
        return new Response(message, true);
    }

    public static Response error(String message) {
        return new Response(message, false);
    }

    public String getMessage() { return message; }
    public boolean isSuccess() { return success; }

    @Override
    public String toString() {
        return message;
    }
}
