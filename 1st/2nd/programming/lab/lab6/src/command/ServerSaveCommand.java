package command;

import collectionManager.ProductCollection;
import common.Response;
import model.Product;
import util.LoggerConfig;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Saves the collection to disk.
 * Only callable from the server console - clients cannot send SERVER_SAVE.
 */
public class ServerSaveCommand implements Command {

    private final ProductCollection collection;
    private final String            filename;

    public ServerSaveCommand(ProductCollection collection, String filename) {
        this.collection = collection;
        this.filename   = filename;
    }

    @Override
    public Response execute(Object payload) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            collection.getProducts().forEach(p -> writer.println(p.toCsv()));
            int count = collection.getProducts().size();
            LoggerConfig.logSaved(filename, count);
            return new Response(true, "Saved " + count + " products to " + filename,
                    collection.getSortedByName());
        } catch (IOException e) {
            LoggerConfig.logError("ServerSaveCommand", "Save failed", e);
            return new Response(false, "Save failed: " + e.getMessage(), collection.getSortedByName());
        }
    }
    @Override
    public boolean isVisibleInHelp() { return false; }
    @Override public String getName()        { return "server_save"; }
    @Override public String getDescription() { return "save collection to file "; }
}
