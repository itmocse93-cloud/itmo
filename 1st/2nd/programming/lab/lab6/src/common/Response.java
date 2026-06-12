package common;

import model.Product;

import java.io.Serializable;
import java.util.Vector;

/**
 * Serializable response sent from server to client.
 */
public class Response implements Serializable {

    private static final long serialVersionUID = 1L;

    private final boolean        success;
    private final String         message;
    private final Vector<Product> sortedCollection;

    public Response(boolean success, String message) {
        this(success, message, new Vector<>());
    }

    public Response(boolean success, String message, Vector<Product> sortedCollection) {
        this.success          = success;
        this.message          = message;
        this.sortedCollection = sortedCollection != null ? sortedCollection : new Vector<>();
    }

    public boolean        isSuccess()          { return success; }
    public String         getMessage()         { return message; }
    public Vector<Product> getSortedCollection() { return sortedCollection; }

    @Override
    public String toString() {
        return "Response{" + (success ? "OK" : "ERROR") + ", msg='" + message + "'}";
    }
}
