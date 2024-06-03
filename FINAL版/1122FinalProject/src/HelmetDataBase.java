import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HelmetDataBase {
    private Connection conn;

    public HelmetDataBase() {
        try {
            String server = "jdbc:mysql://140.119.19.73:3315/";
            String database = "112306089";
            String url = server + database + "?useSSL=false";
            String username = "112306089";
            String password = "mmwab";
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Database connected successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Helmet> getHelmetList() {
        List<Helmet> helmets = new ArrayList<>();
        String query = "SELECT * FROM HelmetList";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String model = rs.getString("model");
                String size = rs.getString("size");
                int available = rs.getInt("available");
                SafetyHelmet helmet = new SafetyHelmet(id, model, size, available);
                helmet.setAvailable(available);
                helmets.add(helmet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return helmets;
    }

    public ResultSet getAllHelmets() {
        String query = "SELECT * FROM HelmetList"; 
        try {
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    
    public int getUserHelmetCount(String username) throws SQLException {
        String query = "SELECT COUNT(*) AS helmet_count FROM UserInfo WHERE username = ? AND (helmet1 IS NOT NULL OR helmet2 IS NOT NULL)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("helmet_count");
            }
        }
        return 0;
    }
    
    public void registerUser(String username, String password) throws SQLException {
        String insertQuery = "INSERT INTO UserInfo (Username, Password, Helmet1, Helmet2) VALUES (?, ?, NULL, NULL)";
        try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
        }
    }
    
    public ResultSet getUserInfo(String username, String password) throws SQLException {
        String query = "SELECT * FROM UserInfo WHERE Username = ? AND Password = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            // 創建一個可滾動和可更新的 ResultSet
            stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, username);
            stmt.setString(2, password);
            System.out.println("Executing query: " + stmt.toString());
            rs = stmt.executeQuery();
            if (rs != null && rs.next()) {
                System.out.println("User found: " + username);
                // 重置游標到第一行之前以供外部使用
                rs.beforeFirst();
            } else {
                System.out.println("No user found with provided username and password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }




    public void rentHelmet(int helmetId, String username) throws SQLException {
        int userHelmetCount = getUserHelmetCount(username);

        if (userHelmetCount >= 2) {
            throw new SQLException("User already has two helmets.");
        } else if (userHelmetCount == 1) {
        	
            String checkQuery = "SELECT * FROM UserInfo WHERE username = ? AND (helmet1 = ? OR helmet2 = ?)";
            try (PreparedStatement stmt = conn.prepareStatement(checkQuery)) {
                stmt.setString(1, username);
                stmt.setInt(2, helmetId);
                stmt.setInt(3, helmetId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    throw new SQLException("This helmet is already rented by the user.");
                }
            }
        }

        String updateHelmetListQuery = "UPDATE HelmetList SET available = 0, username = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateHelmetListQuery)) {
            stmt.setString(1, username);
            stmt.setInt(2, helmetId);
            stmt.executeUpdate();
        }

        String updateUserInfoQuery = "UPDATE UserInfo SET helmet1 = CASE WHEN helmet1 IS NULL THEN ? ELSE helmet1 END, helmet2 = CASE WHEN helmet1 IS NOT NULL AND helmet2 IS NULL THEN ? ELSE helmet2 END WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateUserInfoQuery)) {
            stmt.setInt(1, helmetId);
            stmt.setInt(2, helmetId);
            stmt.setString(3, username);
            stmt.executeUpdate();
        }
    }



    public void returnHelmet(int helmetId, String username) throws SQLException {
        
        
        // 更新 HelmetList 中的 available 狀態和 username
        String updateHelmetListQuery = "UPDATE HelmetList SET available = 1, username = NULL WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateHelmetListQuery)) {
            stmt.setInt(1, helmetId);
            int rowsUpdated = stmt.executeUpdate();
            System.out.println("HelmetList updated rows: " + rowsUpdated);
        }

       

        // 更新 UserInfo 中的 helmet1 和 helmet2 欄位
        String updateUserInfoQuery = "UPDATE UserInfo SET helmet1 = CASE WHEN helmet1 = ? THEN NULL ELSE helmet1 END, helmet2 = CASE WHEN helmet2 = ? THEN NULL ELSE helmet2 END WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateUserInfoQuery)) {
            stmt.setInt(1, helmetId);
            stmt.setInt(2, helmetId);
            stmt.setString(3, username);
            int rowsUpdated = stmt.executeUpdate();
            System.out.println("UserInfo updated rows: " + rowsUpdated);
        }
        
       
    }






}