package server;

import collectionManager.IdGenerator;
import collectionManager.ProductCollection;
import model.Product;
import util.LoggerConfig;

import java.io.*;

/**
 * Loads the product collection from a CSV file at startup.
 */
public class CollectionFileManager {

    private final String            filename;
    private final ProductCollection collection;

    public CollectionFileManager(String filename, ProductCollection collection) {
        this.filename   = filename;
        this.collection = collection;
    }

    public void load() {
        File file = new File(filename);
        if (!file.exists()) {
            LoggerConfig.logWarn("CollectionFileManager", "File not found: " + filename);
            return;
        }

        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                try {
                    collection.add(Product.fromCsv(line));
                    count++;
                } catch (Exception e) {
                    LoggerConfig.logError("CollectionFileManager", "Skipping bad line", e);
                }
            }
            if (count > 0)
                IdGenerator.syncAfterLoad(collection.getMaxProductId(),
                        collection.getMaxOrganizationId());
            LoggerConfig.logLoaded(filename, count);
        } catch (IOException e) {
            LoggerConfig.logError("CollectionFileManager", "Cannot read file", e);
        }
    }
}
