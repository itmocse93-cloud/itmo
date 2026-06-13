package model;

import java.io.Serializable;

public class Organization implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private long annualTurnover;
    private String fullName;
    private String type;
    private long employeesCount;

    public Organization(int id, long annualTurnover, String fullName, String type, long employeesCount) {
        this.id = id;
        this.annualTurnover = annualTurnover;
        this.fullName = fullName;
        this.type = type;
        this.employeesCount = employeesCount;
    }

    // Constructor without id (for client input)
    public Organization(long annualTurnover, String fullName, String type, long employeesCount) {
        this.annualTurnover = annualTurnover;
        this.fullName = fullName;
        this.type = type;
        this.employeesCount = employeesCount;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public long getAnnualTurnover() { return annualTurnover; }
    public void setAnnualTurnover(long annualTurnover) { this.annualTurnover = annualTurnover; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public long getEmployeesCount() { return employeesCount; }
    public void setEmployeesCount(long employeesCount) { this.employeesCount = employeesCount; }

    @Override
    public String toString() {
        return String.format("Organization{id=%d, fullName='%s', type='%s', " +
                "annualTurnover=%d, employeesCount=%d}",
                id, fullName, type, annualTurnover, employeesCount);
    }
}
