package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Product implements Serializable, Comparable<Product> {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private Coordinates coordinates;
    private LocalDateTime creationDate;
    private Integer price;
    private String partNumber;
    private UnitOfMeasure unitOfMeasure;
    private Organization manufacturer;
    private String ownerLogin; // Lab 7: owner

    public Product(int id, String name, Coordinates coordinates, LocalDateTime creationDate,
                   Integer price, String partNumber, UnitOfMeasure unitOfMeasure,
                   Organization manufacturer) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.price = price;
        this.partNumber = partNumber;
        this.unitOfMeasure = unitOfMeasure;
        this.manufacturer = manufacturer;
    }

    // Constructor used by client (no id, no date yet)
    public Product(String name, Coordinates coordinates, Integer price, String partNumber,
                   UnitOfMeasure unitOfMeasure, Organization manufacturer) {
        this.name = name;
        this.coordinates = coordinates;
        this.price = price;
        this.partNumber = partNumber;
        this.unitOfMeasure = unitOfMeasure;
        this.manufacturer = manufacturer;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Coordinates getCoordinates() { return coordinates; }
    public void setCoordinates(Coordinates coordinates) { this.coordinates = coordinates; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }
    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }
    public String getPartNumber() { return partNumber; }
    public void setPartNumber(String partNumber) { this.partNumber = partNumber; }
    public UnitOfMeasure getUnitOfMeasure() { return unitOfMeasure; }
    public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) { this.unitOfMeasure = unitOfMeasure; }
    public Organization getManufacturer() { return manufacturer; }
    public void setManufacturer(Organization manufacturer) { this.manufacturer = manufacturer; }
    public String getOwnerLogin() { return ownerLogin; }
    public void setOwnerLogin(String ownerLogin) { this.ownerLogin = ownerLogin; }

    @Override
    public int compareTo(Product other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return String.format(
            "Product{id=%d, name='%s', coordinates=%s, creationDate=%s, price=%s, " +
            "partNumber='%s', unitOfMeasure=%s, manufacturer=%s, owner='%s'}",
            id, name, coordinates, creationDate, price,
            partNumber, unitOfMeasure, manufacturer, ownerLogin);
    }
}
