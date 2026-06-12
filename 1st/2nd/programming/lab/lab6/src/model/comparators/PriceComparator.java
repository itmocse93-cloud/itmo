package model.comparators;

import model.Product;
import java.util.Comparator;

/** Sorts products by price . */
public class PriceComparator implements Comparator<Product> {
    @Override
    public int compare(Product a, Product b) {
        if (a.getPrice() == null && b.getPrice() == null) return 0;
        if (a.getPrice() == null) return -1;
        if (b.getPrice() == null) return 1;
        return Integer.compare(a.getPrice(), b.getPrice());
    }
}
