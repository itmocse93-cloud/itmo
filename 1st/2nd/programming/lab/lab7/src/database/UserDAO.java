package database;

import util.PasswordHasher;

import java.sql.*;

/**
 * Data Access Object for user-related database operations.
 * Handles registration, authentication and existence checks.
 */
public class UserDAO {

    private final DatabaseManager db;

    public UserDAO(DatabaseManager db) {
        this.db = db;
    }

    /**
     * Registers a new user.
     */
    public boolean register(String login, String rawPassword) throws SQLException {
        String hash = PasswordHasher.hashMD2(rawPassword);
        String sql = "INSERT INTO users(login, password_hash) VALUES(?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setString(2, hash);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) return false; // duplicate key
            throw e;
        }
    }

    /**
     * Checks if the provided MD2 password hash matches the stored hash for this login.
     */
    public boolean authenticate(String login, String passwordHash) throws SQLException {
        String sql = "SELECT password_hash FROM users WHERE login = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("password_hash").equals(passwordHash);
            }
            return false;
        }
    }

    /**
     * Returns true if a user with this login already exists.
     */
    public boolean exists(String login) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE login = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }
}
