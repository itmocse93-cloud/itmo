package model;

import java.io.Serializable;

/**
 * Manufacturer organization of a product.
 */
public class Organization implements Comparable<Organization>, Serializable {

    private static final long serialVersionUID = 1L;

    private long   id;
    private String name;
    private String fullName;
    private long   annualTurnover;
    private int    employeesCount;

    public Organization(long id, String name, String fullName,
                        long annualTurnover, int employeesCount) {
        setId(id);
        setName(name);
        setFullName(fullName);
        setAnnualTurnover(annualTurnover);
        setEmployeesCount(employeesCount);
    }

    // --- Getters ---
    public long   getId()             { return id; }
    public String getName()           { return name; }
    public String getFullName()       { return fullName; }
    public long   getAnnualTurnover() { return annualTurnover; }
    public int    getEmployeesCount() { return employeesCount; }

    // --- Setters with validation ---
    public void setId(long id) {
        if (id != -1 && id <= 0)
            throw new IllegalArgumentException("Organization ID must be > 0");
        this.id = id;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Organization name cannot be empty");
        this.name = name;
    }

    public void setFullName(String fullName) {
        if (fullName == null)
            throw new IllegalArgumentException("Full name cannot be null");
        this.fullName = fullName;
    }

    public void setAnnualTurnover(long annualTurnover) {
        if (annualTurnover <= 0)
            throw new IllegalArgumentException("Annual turnover must be > 0");
        this.annualTurnover = annualTurnover;
    }

    public void setEmployeesCount(int employeesCount) {
        if (employeesCount <= 0)
            throw new IllegalArgumentException("Employees count must be > 0");
        this.employeesCount = employeesCount;
    }

    public String toCsv() {
        return id + ";" + name + ";" + fullName + ";" + annualTurnover + ";" + employeesCount;
    }

    public static Organization fromCsv(String csv) {
        String[] p = csv.split(";");
        return new Organization(
                Long.parseLong(p[0]), p[1], p[2],
                Long.parseLong(p[3]), Integer.parseInt(p[4])
        );
    }

    @Override
    public int compareTo(Organization other) {
        return Long.compare(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Organization{id=" + id + ", name='" + name + "', fullName='" + fullName
                + "', turnover=" + annualTurnover + ", employees=" + employeesCount + "}";
    }
}
