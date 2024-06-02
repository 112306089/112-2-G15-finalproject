import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class HelmetDatabase {
    private Connection conn;
    private String username;

    public HelmetDatabase(Connection conn) { // 接受從HelmetSharingSystem傳遞過來的數據庫連接
        this.conn = conn;
        try {
            // 加載SQLite JDBC驅動
            Class.forName("org.sqlite.JDBC");
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `HelmetList` (id INTEGER PRIMARY KEY AUTOINCREMENT, model TEXT, size TEXT, available INTEGER DEFAULT 1)");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public void addHelmet(String model, String size) {
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO `HelmetList` (model, size) VALUES (?, ?)")) {
            stmt.setString(1, model);
            stmt.setString(2, size);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteHelmet(int id) {
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM `HelmetList` WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void reserveHelmet(int id) { 
        try (PreparedStatement stmt = conn.prepareStatement("UPDATE `HelmetList` SET `Username`= ? WHERE id = ?")) {
            stmt.setString(1, username);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public ResultSet getAllHelmets() {
        try {
            Statement stmt = conn.createStatement();
            return stmt.executeQuery("SELECT * FROM `HelmetList` WHERE Username IS NULL"); 
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
