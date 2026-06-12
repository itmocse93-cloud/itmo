package collectionManager;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Thread-safe auto-increment ID generator for products and organizations.
 */
public final class IdGenerator {

    private static AtomicInteger productIdCounter      = new AtomicInteger(1);
    private static AtomicLong    organizationIdCounter = new AtomicLong(1);

    private IdGenerator() {}

    public static int  nextProductId()      { return productIdCounter.getAndIncrement(); }
    public static long nextOrganizationId() { return organizationIdCounter.getAndIncrement(); }

    /**
     * Synchronizes counters after loading the collection from file.
     *
     * @param maxProductId      highest product ID found in the file
     * @param maxOrganizationId highest organization ID found in the file
     */
    public static void syncAfterLoad(int maxProductId, long maxOrganizationId) {
        productIdCounter      = new AtomicInteger(maxProductId + 1);
        organizationIdCounter = new AtomicLong(maxOrganizationId + 1);
    }
}
