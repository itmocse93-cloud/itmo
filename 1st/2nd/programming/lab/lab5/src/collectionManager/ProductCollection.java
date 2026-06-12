package collectionManager;

import model.Product;
import model.Organization;
import java.util.Vector;
import java.util.Date;
import java.util.Collections;

/**
 * Manages the collection of products
 */
public class ProductCollection {
    private Vector<Product> products;
    private Date initializationDate;

    /**
     * Constructor for ProductCollection
     */
    public ProductCollection() {
        this.products = new Vector<>();
        this.initializationDate = new Date();
    }

    /**
     * Adds a product to the collection
     * @param product product to add
     */
    public void add(Product product) {
        products.add(product);
    }

    /**
     * Removes a product by ID
     * @param id product ID
     * @return true if removed successfully
     */
    public boolean removeById(int id) {
        return products.removeIf(p -> p.getId() == id);
    }

    /**
     * Gets a product by ID
     * @param id product ID
     * @return product or null if not found
     */
    public Product getById(int id) {
        for (Product p : products) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    /**
     * Adds product if it's minimum
     * @param product product to add
     * @return true if added
     */
    public boolean addMin(Product product) {
        if (products.isEmpty()) {
            products.add(product);
            return true;
        }

        Product min = Collections.min(products);
        if (product.compareTo(min) < 0) {
            products.add(product);
            return true;
        }
        return false;
    }

    /**
     * Shuffles the collection randomly
     */
    public void shuffle() {
        Collections.shuffle(products);
    }

    /**
     * Counts products with given price
     * @param price price to count
     * @return count of products with that price
     */
    public long countByPrice(Integer price) {
        long count = 0;
        for (Product p : products) {
            if (price == null && p.getPrice() == null) {
                count++;
            } else if (price != null && price.equals(p.getPrice())) {
                count++;
            }
        }
        return count;
    }

    /**
     * Prints products in descending order
     */
    public void printDescending() {
        Vector<Product> sorted = new Vector<>(products);
        sorted.sort(Collections.reverseOrder());
        for (Product p : sorted) {
            System.out.println(p);
        }
    }

    /**
     * Prints manufacturer fields in descending order
     */
    public void printFieldDescendingManufacturer() {
        Vector<Organization> manufacturers = new Vector<>();
        for (Product p : products) {
            Organization m = p.getManufacturer();
            if (m != null && !manufacturers.contains(m)) {
                manufacturers.add(m);
            }
        }

        manufacturers.sort((m1, m2) -> m2.getName().compareTo(m1.getName()));

        for (Organization m : manufacturers) {
            System.out.println(m.getName());
        }
    }

    /**
     * Gets information about the collection
     * @return info string
     */
    public String getInfo() {
        return String.format(
                "Collection type: %s\n" +
                        "Initialization date: %s\n" +
                        "Number of elements: %d\n" +
                        "Element type: Product",
                products.getClass().getName(),
                initializationDate,
                products.size()
        );
    }

    /**
     * Gets all products
     * @return vector of products
     */
    public Vector<Product> getProducts() {
        return products;
    }

    /**
     * Clears the collection
     */
    public void clear() {
        products.clear();
    }

    /**
     * Removes the last element
     */
    public void removeLast() {
        if (!products.isEmpty()) {
            products.remove(products.size() - 1);
        }
    }

    /**
     * Gets maximum product ID
     * @return max ID or 0 if empty
     */
    public int getMaxProductId() {
        int max = 0;
        for (Product p : products) {
            if (p.getId() > max) {
                max = p.getId();
            }
        }
        return max;
    }

    /**
     * Gets maximum organization ID
     * @return max organization ID or 0 if none
     */
    public long getMaxOrganizationId() {
        long max = 0;
        for (Product p : products) {
            Organization m = p.getManufacturer();
            if (m != null && m.getId() > max) {
                max = m.getId();
            }
        }
        return max;
    }
}