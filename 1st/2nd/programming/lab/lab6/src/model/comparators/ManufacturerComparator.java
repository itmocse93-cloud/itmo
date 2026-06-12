package model.comparators;

import model.Organization;
import model.Product;
import java.util.Comparator;

/** Sorts products by manufacturer name . */
public class ManufacturerComparator implements Comparator<Product> {
    @Override
    public int compare(Product a, Product b) {
        Organization m1 = a.getManufacturer();
        Organization m2 = b.getManufacturer();
        if (m1 == null && m2 == null) return 0;
        if (m1 == null) return -1;
        if (m2 == null) return 1;
        return m1.getName().compareTo(m2.getName());
    }
}
