import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class HelmetDatabase {
    private Connection conn;

    public HelmetDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:helmets.db");
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS helmets (id INTEGER PRIMARY KEY AUTOINCREMENT, model TEXT, size TEXT, available INTEGER DEFAULT 1)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addHelmet(String model, String size) {
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO helmets (model, size) VALUES (?, ?)")) {
            stmt.setString(1, model);
            stmt.setString(2, size);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteHelmet(int id) {
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM helmets WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getAllHelmets() {
        try {
            Statement stmt = conn.createStatement();
            return stmt.executeQuery("SELECT * FROM helmets WHERE available = 1");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}