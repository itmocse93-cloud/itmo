package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages the database connection and table initialization.
 */
public class DatabaseManager {

    private final String url;
    private final String user;
    private final String password;

    public DatabaseManager(String host, String dbName, String user, String password) {
        this.url = "jdbc:postgresql://" + host + "/" + dbName;
        this.user = user;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public void initTables() throws SQLException {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    login VARCHAR(100) PRIMARY KEY,
                    password_hash VARCHAR(255) NOT NULL
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS organizations (
                    id SERIAL PRIMARY KEY,
                    annual_turnover BIGINT,
                    full_name VARCHAR(255),
                    type VARCHAR(100),
                    employees_count BIGINT
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS products (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    coord_x FLOAT NOT NULL,
                    coord_y INT NOT NULL,
                    creation_date TIMESTAMP NOT NULL,
                    price INT,
                    part_number VARCHAR(255),
                    unit_of_measure VARCHAR(50),
                    manufacturer_id INT REFERENCES organizations(id) ON DELETE SET NULL,
                    owner_login VARCHAR(100) REFERENCES users(login) ON DELETE CASCADE
                )
            """);
        }
    }
}
