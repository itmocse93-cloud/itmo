package model;

import java.io.Serializable;
import java.util.Date;

/**
 * Main entity stored in the collection.
 */
public class Product implements Comparable<Product>, Serializable {

    private static final long serialVersionUID = 1L;

    private Integer      id;
    private String       name;
    private Coordinates  coordinates;
    private Date         creationDate;
    private Integer      price;
    private String       partNumber;
    private UnitOfMeasure unitOfMeasure;
    private Organization manufacturer;

    public Product(Integer id, String name, Coordinates coordinates,
                   Integer price, String partNumber,
                   UnitOfMeasure unitOfMeasure, Organization manufacturer) {
        setId(id);
        setName(name);
        setCoordinates(coordinates);
        setPrice(price);
        setPartNumber(partNumber);
        this.unitOfMeasure = unitOfMeasure;
        this.manufacturer  = manufacturer;
        this.creationDate  = new Date();
    }

    // --- Getters ---
    public Integer      getId()            { return id; }
    public String       getName()          { return name; }
    public Coordinates  getCoordinates()   { return coordinates; }
    public Date         getCreationDate()  { return creationDate; }
    public Integer      getPrice()         { return price; }
    public String       getPartNumber()    { return partNumber; }
    public UnitOfMeasure getUnitOfMeasure(){ return unitOfMeasure; }
    public Organization getManufacturer()  { return manufacturer; }

    // --- Setters with validation ---
    public void setId(Integer id) {
        if (id != -1 && id <= 0)
            throw new IllegalArgumentException("ID must be > 0");
        this.id = id;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Name cannot be empty");
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null)
            throw new IllegalArgumentException("Coordinates cannot be null");
        this.coordinates = coordinates;
    }

    public void setCreationDate(Date creationDate) {
        if (creationDate == null)
            throw new IllegalArgumentException("Creation date cannot be null");
        this.creationDate = creationDate;
    }

    public void setPrice(Integer price) {
        if (price != null && price <= 0)
            throw new IllegalArgumentException("Price must be > 0");
        this.price = price;
    }

    public void setPartNumber(String partNumber) {
        if (partNumber == null)
            throw new IllegalArgumentException("Part number cannot be null");
        this.partNumber = partNumber;
    }

    public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) { this.unitOfMeasure = unitOfMeasure; }
    public void setManufacturer(Organization manufacturer)     { this.manufacturer  = manufacturer; }

    @Override
    public int compareTo(Product other) {
        if (this.price == null && other.price == null) return 0;
        if (this.price == null) return 1;
        if (other.price == null) return -1;
        return Integer.compare(this.price, other.price);
    }

    /** Serializes to a single CSV line. */
    public String toCsv() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(",")
          .append(name).append(",")
          .append(coordinates.getX()).append(",")
          .append(coordinates.getY()).append(",")
          .append(creationDate.getTime()).append(",")
          .append(price == null ? "" : price).append(",")
          .append(partNumber).append(",")
          .append(unitOfMeasure == null ? "" : unitOfMeasure).append(",");

        if (manufacturer != null) {
            sb.append(manufacturer.getId()).append(",")
              .append(manufacturer.getName()).append(",")
              .append(manufacturer.getFullName()).append(",")
              .append(manufacturer.getAnnualTurnover()).append(",")
              .append(manufacturer.getEmployeesCount());
        } else {
            sb.append(",,,,");
        }
        return sb.toString();
    }

    /** Deserializes from a CSV line. */
    public static Product fromCsv(String csv) {
        String[] p = csv.split(",", -1);

        Integer       id           = Integer.parseInt(p[0].trim());
        String        name         = p[1].trim();
        Coordinates   coords       = new Coordinates(Float.parseFloat(p[2].trim()), Integer.parseInt(p[3].trim()));
        Date          creationDate = new Date(Long.parseLong(p[4].trim()));
        Integer       price        = p[5].trim().isEmpty() ? null : Integer.parseInt(p[5].trim());
        String        partNumber   = p[6].trim();
        UnitOfMeasure unit         = p[7].trim().isEmpty() ? null : UnitOfMeasure.valueOf(p[7].trim());

        Organization manufacturer = null;
        if (p.length >= 13 && !p[8].trim().isEmpty()) {
            manufacturer = new Organization(
                    Long.parseLong(p[8].trim()), p[9].trim(), p[10].trim(),
                    Long.parseLong(p[11].trim()), Integer.parseInt(p[12].trim())
            );
        }

        Product product = new Product(id, name, coords, price, partNumber, unit, manufacturer);
        product.setCreationDate(creationDate);
        return product;
    }

    @Override
    public String toString() {
        return "Product{id=" + id
                + ", name='" + name + "'"
                + ", coords=" + coordinates
                + ", creationDate=" + creationDate
                + ", price=" + price
                + ", partNumber='" + partNumber + "'"
                + ", unit=" + unitOfMeasure
                + ", manufacturer=" + manufacturer + "}";
    }
}
