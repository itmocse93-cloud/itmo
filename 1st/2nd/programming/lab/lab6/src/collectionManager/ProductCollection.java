package collectionManager;

import model.Organization;
import model.Product;

import java.util.*;
import java.util.stream.Collectors;

/**In-memory collection of products.
 */
public class ProductCollection {

    private final Vector<Product> products;
    private final Date            initializationDate;

    public ProductCollection() {
        this.products           = new Vector<>();
        this.initializationDate = new Date();
    }

    // --- Mutators- change the collection--

    public void add(Product product) {
        products.add(product);
    }

    public boolean removeById(int id) {
        return products.removeIf(p -> p.getId().equals(id));
    }

    public void removeLast() {
        if (!products.isEmpty()) products.remove(products.size() - 1);
    }

    public void clear() {
        products.clear();
    }

    public void shuffle() {
        Collections.shuffle(products);
    }

    /**
     * Adds a product only if its price is less than the current minimum.
     *
     * @return true if the product was added
     */
    public boolean addIfMin(Product product) {
        if (products.isEmpty()) {
            products.add(product);
            return true;
        }
        Product min = products.stream().min(Product::compareTo).orElseThrow();
        if (product.compareTo(min) < 0) {
            products.add(product);
            return true;
        }
        return false;
    }

    // --- Queries ---

    public Product getById(int id) {
        return products.stream()
                .filter(p -> p.getId() != null && p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public long countByPrice(Integer price) {
        return products.stream()
                .filter(p -> Objects.equals(p.getPrice(), price))
                .count();
    }

    /** Returns products sorted by name ascending (requirement: client always sees this order). */
    public Vector<Product> getSortedByName() {
        return products.stream()
                .sorted(Comparator.comparing(Product::getName,
                        Comparator.nullsLast(String::compareTo)))
                .collect(Collectors.toCollection(Vector::new));
    }

    /** Returns products sorted by price descending. */
    public List<Product> getDescending() {
        return products.stream()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());
    }

    /** Returns distinct manufacturer names sorted descending. */
    public List<String> getManufacturerNamesDescending() {
        return products.stream()
                .map(Product::getManufacturer)
                .filter(Objects::nonNull)
                .map(Organization::getName)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    public int getMaxProductId() {
        return products.stream().mapToInt(Product::getId).max().orElse(0);
    }

    public long getMaxOrganizationId() {
        return products.stream()
                .map(Product::getManufacturer)
                .filter(Objects::nonNull)
                .mapToLong(Organization::getId)
                .max().orElse(0);
    }

    public String getInfo() {
        return String.format(
                "Type       : %s%nInit date  : %s%nSize       : %d%nElement    : Product",
                products.getClass().getName(), initializationDate, products.size());
    }

    public Vector<Product> getProducts() { return products; }
}
