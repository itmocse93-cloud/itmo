package database;

import model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    private final DatabaseManager db;

    public ProductDAO(DatabaseManager db) {
        this.db = db;
    }

    public Product insert(Product product, String ownerLogin) throws SQLException {
        Integer manufacturerId = null;
        if (product.getManufacturer() != null) {
            manufacturerId = insertOrganization(product.getManufacturer());
        }

        String sql = "INSERT INTO products(name, coord_x, coord_y, creation_date, price, " +
                     "part_number, unit_of_measure, manufacturer_id, owner_login) " +
                     "VALUES(?,?,?,?,?,?,?,?,?) RETURNING id";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            LocalDateTime now = LocalDateTime.now();
            ps.setString(1, product.getName());
            ps.setFloat(2, product.getCoordinates().getX());
            ps.setInt(3, product.getCoordinates().getY());
            ps.setTimestamp(4, Timestamp.valueOf(now));
            if (product.getPrice() != null) ps.setInt(5, product.getPrice());
            else ps.setNull(5, Types.INTEGER);
            ps.setString(6, product.getPartNumber());
            ps.setString(7, product.getUnitOfMeasure() != null
                    ? product.getUnitOfMeasure().name() : null);
            if (manufacturerId != null) ps.setInt(8, manufacturerId);
            else ps.setNull(8, Types.INTEGER);
            ps.setString(9, ownerLogin);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                product.setId(rs.getInt("id"));
                product.setCreationDate(now);
                product.setOwnerLogin(ownerLogin);
                if (product.getManufacturer() != null && manufacturerId != null) {
                    product.getManufacturer().setId(manufacturerId);
                }
            }
        }
        return product;
    }

    /**
     * Updates an existing product.
     */
    public boolean update(Product product, String requestingUser) throws SQLException {
        if (!isOwner(product.getId(), requestingUser)) return false;

        if (product.getManufacturer() != null) {
            deleteOrganizationOfProduct(product.getId());
            int newOrgId = insertOrganization(product.getManufacturer());
            product.getManufacturer().setId(newOrgId);
        }

        String sql = "UPDATE products SET name=?, coord_x=?, coord_y=?, price=?, " +
                     "part_number=?, unit_of_measure=?, manufacturer_id=? WHERE id=?";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getName());
            ps.setFloat(2, product.getCoordinates().getX());
            ps.setInt(3, product.getCoordinates().getY());
            if (product.getPrice() != null) ps.setInt(4, product.getPrice());
            else ps.setNull(4, Types.INTEGER);
            ps.setString(5, product.getPartNumber());
            ps.setString(6, product.getUnitOfMeasure() != null
                    ? product.getUnitOfMeasure().name() : null);
            if (product.getManufacturer() != null)
                ps.setInt(7, product.getManufacturer().getId());
            else ps.setNull(7, Types.INTEGER);
            ps.setInt(8, product.getId());
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Deletes product by id.
     */
    public boolean deleteById(int id, String requestingUser) throws SQLException {
        if (!isOwner(id, requestingUser)) return false;
        deleteOrganizationOfProduct(id);
        String sql = "DELETE FROM products WHERE id=?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Deletes all products belonging to the requesting user.
     */
    public int deleteAllByOwner(String ownerLogin) throws SQLException {
        List<Integer> orgIds = getOrganizationIdsOfOwner(ownerLogin);

        String sql = "DELETE FROM products WHERE owner_login=?";
        int count;
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ownerLogin);
            count = ps.executeUpdate();
        }

        deleteOrphanedOrganizations(orgIds);
        return count;
    }

    /**
     * Loads all products from DB at server startup.
     */
    public List<Product> loadAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = """
            SELECT p.id, p.name, p.coord_x, p.coord_y, p.creation_date,
                   p.price, p.part_number, p.unit_of_measure, p.owner_login,
                   o.id as org_id, o.annual_turnover, o.full_name, o.type, o.employees_count
            FROM products p
            LEFT JOIN organizations o ON p.manufacturer_id = o.id
            ORDER BY p.id
        """;
        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                products.add(mapRow(rs));
            }
        }
        return products;
    }

    // ===== PRIVATE HELPERS =====

    private int insertOrganization(Organization org) throws SQLException {
        String sql = "INSERT INTO organizations(annual_turnover, full_name, type, employees_count) " +
                     "VALUES(?,?,?,?) RETURNING id";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, org.getAnnualTurnover());
            ps.setString(2, org.getFullName());
            ps.setString(3, org.getType());
            ps.setLong(4, org.getEmployeesCount());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");
        }
        throw new SQLException("Failed to insert organization");
    }

    private void deleteOrganizationOfProduct(int productId) throws SQLException {
        String getOrgSql = "SELECT manufacturer_id FROM products WHERE id=?";
        int orgId = -1;
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(getOrgSql)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getObject("manufacturer_id") != null) {
                orgId = rs.getInt("manufacturer_id");
            }
        }
        if (orgId > 0) {
            try (Connection conn = db.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                         "DELETE FROM organizations WHERE id=?")) {
                ps.setInt(1, orgId);
                ps.executeUpdate();
            }
        }
    }

    private boolean isOwner(int productId, String login) throws SQLException {
        String sql = "SELECT owner_login FROM products WHERE id=?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return login.equals(rs.getString("owner_login"));
            return false;
        }
    }

    private List<Integer> getOrganizationIdsOfOwner(String ownerLogin) throws SQLException {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT manufacturer_id FROM products WHERE owner_login=? AND manufacturer_id IS NOT NULL";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ownerLogin);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) ids.add(rs.getInt("manufacturer_id"));
        }
        return ids;
    }

    private void deleteOrphanedOrganizations(List<Integer> orgIds) throws SQLException {
        String sql = "DELETE FROM organizations WHERE id=? " +
                     "AND NOT EXISTS (SELECT 1 FROM products WHERE manufacturer_id=?)";
        for (int orgId : orgIds) {
            try (Connection conn = db.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, orgId);
                ps.setInt(2, orgId);
                ps.executeUpdate();
            }
        }
    }

    private Product mapRow(ResultSet rs) throws SQLException {
        Organization org = null;
        if (rs.getObject("org_id") != null) {
            org = new Organization(
                rs.getInt("org_id"),
                rs.getLong("annual_turnover"),
                rs.getString("full_name"),
                rs.getString("type"),
                rs.getLong("employees_count")
            );
        }
        String unitStr = rs.getString("unit_of_measure");
        UnitOfMeasure unit = (unitStr != null) ? UnitOfMeasure.valueOf(unitStr) : null;

        Product p = new Product(
            rs.getInt("id"),
            rs.getString("name"),
            new Coordinates(rs.getFloat("coord_x"), rs.getInt("coord_y")),
            rs.getTimestamp("creation_date").toLocalDateTime(),
            (Integer) rs.getObject("price"),
            rs.getString("part_number"),
            unit,
            org
        );
        p.setOwnerLogin(rs.getString("owner_login"));
        return p;
    }
}
