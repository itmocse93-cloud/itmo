package collectionManager;

import model.Product;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class ProductCollection {

    private final Vector<Product> products = new Vector<>();
    private final LocalDateTime initializationDate = LocalDateTime.now();
    private final ReentrantLock lock = new ReentrantLock();

    // ===== LOCK-PROTECTED METHODS =====

    public void add(Product p) {
        lock.lock();
        try { products.add(p); }
        finally { lock.unlock(); }
    }

    public boolean remove(Product p) {
        lock.lock();
        try { return products.remove(p); }
        finally { lock.unlock(); }
    }

    public boolean removeById(int id) {
        lock.lock();
        try { return products.removeIf(p -> p.getId() == id); }
        finally { lock.unlock(); }
    }

    public int removeByOwner(String owner) {
        lock.lock();
        try {
            int before = products.size();
            products.removeIf(p -> owner.equals(p.getOwnerLogin()));
            return before - products.size();
        } finally { lock.unlock(); }
    }

    public void clear() {
        lock.lock();
        try { products.clear(); }
        finally { lock.unlock(); }
    }

    public void shuffle() {
        lock.lock();
        try { Collections.shuffle(products); }
        finally { lock.unlock(); }
    }

    public void updateById(int id, Product updated) {
        lock.lock();
        try {
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getId() == id) {
                    updated.setId(id);
                    updated.setCreationDate(products.get(i).getCreationDate());
                    updated.setOwnerLogin(products.get(i).getOwnerLogin());
                    products.set(i, updated);
                    return;
                }
            }
        } finally { lock.unlock(); }
    }

    public void removeLast() {
        lock.lock();
        try {
            if (!products.isEmpty())
                products.remove(products.size() - 1);
        } finally { lock.unlock(); }
    }

    // ===== READ METHODS (also lock-protected) =====

    public Vector<Product> getProducts() {
        lock.lock();
        try { return new Vector<>(products); }
        finally { lock.unlock(); }
    }

    public boolean isEmpty() {
        lock.lock();
        try { return products.isEmpty(); }
        finally { lock.unlock(); }
    }

    public int size() {
        lock.lock();
        try { return products.size(); }
        finally { lock.unlock(); }
    }

    public Optional<Product> getById(int id) {
        lock.lock();
        try {
            return products.stream().filter(p -> p.getId() == id).findFirst();
        } finally { lock.unlock(); }
    }

    public Optional<Product> getMinByPrice() {
        lock.lock();
        try {
            return products.stream()
                .filter(p -> p.getPrice() != null)
                .min(Comparator.comparingInt(Product::getPrice));
        } finally { lock.unlock(); }
    }

    public long countByPrice(int price) {
        lock.lock();
        try {
            return products.stream()
                .filter(p -> p.getPrice() != null && p.getPrice() == price)
                .count();
        } finally { lock.unlock(); }
    }

    public List<Product> getSortedDescending() {
        lock.lock();
        try {
            return products.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        } finally { lock.unlock(); }
    }

    public List<String> getManufacturerNamesDescending() {
        lock.lock();
        try {
            return products.stream()
                .filter(p -> p.getManufacturer() != null)
                .map(p -> p.getManufacturer().getFullName())
                .filter(Objects::nonNull)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        } finally { lock.unlock(); }
    }

    public String getInfo() {
        lock.lock();
        try {
            return String.format("Type       : %s%nInit date  : %s%nSize       : %d%nElement    : Product",
                products.getClass().getName(), initializationDate, products.size());
        } finally { lock.unlock(); }
    }

    public String getOwnerLogin(int productId) {
        lock.lock();
        try {
            return products.stream()
                .filter(p -> p.getId() == productId)
                .map(Product::getOwnerLogin)
                .findFirst().orElse(null);
        } finally { lock.unlock(); }
    }

    // Load all products from DB at startup
    public void loadFromList(List<Product> list) {
        lock.lock();
        try {
            products.clear();
            products.addAll(list);
        } finally { lock.unlock(); }
    }
}
