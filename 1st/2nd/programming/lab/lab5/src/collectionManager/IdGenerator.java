package collectionManager;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Generates unique IDs for products and organizations
 */
public class IdGenerator {
    private static AtomicInteger productIdGenerator = new AtomicInteger(1);
    private static AtomicLong organizationIdGenerator = new AtomicLong(1);

    /**
     * Generates a new unique product ID
     * @return new product ID
     */
    public static int generateProductId() {
        return productIdGenerator.getAndIncrement();
    }

    /**
     * Generates a new unique organization ID
     * @return new organization ID
     */
    public static long generateOrganizationId() {
        return organizationIdGenerator.getAndIncrement();
    }

    /**
     * Updates the ID generators based on existing collection
     * @param maxProductId maximum product ID in collection
     * @param maxOrganizationId maximum organization ID in collection
     */
    public static void updateGenerators(int maxProductId, long maxOrganizationId) {
        productIdGenerator = new AtomicInteger(maxProductId + 1);
        organizationIdGenerator = new AtomicLong(maxOrganizationId + 1);
    }
}