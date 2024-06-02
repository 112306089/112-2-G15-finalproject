import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.awt.GridLayout;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

class HelmetSharingSystem extends JFrame {
    protected static final HelmetSharingSystem HelmetSharingSystem = null;
	private JList<Helmet> helmetList; // list 所有的helmet
    private DefaultListModel<Helmet> listModel; 
    private JButton reserveButton;
    private String username;
    private static HelmetSharingSystem instance;
    Connection conn;
    Statement stat;

    private HelmetDatabase database;

    public HelmetSharingSystem(Connection conn) {
        super("Helmet Sharing System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        username = ""; // 初始化 username 為空字符串
        database = new HelmetDatabase(conn); // 將 conn 傳遞給 HelmetDatabase

        listModel = new DefaultListModel<>(); // 主要管理helmet的list
        helmetList = new JList<>(listModel); // 顯示給user看的helmet list
        JScrollPane scrollPane = new JScrollPane(helmetList);
        add(scrollPane, BorderLayout.CENTER);

        createButton();
        add(reserveButton, BorderLayout.SOUTH);

        loadHelmets();
    }

    private void createButton(){
    	reserveButton = new JButton("Reserve"); 
        reserveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Helmet selectedHelmet = helmetList.getSelectedValue();
                if (selectedHelmet != null) {
                    database.reserveHelmet(selectedHelmet.getId()); 
                    listModel.removeElement(selectedHelmet);
                    
                } else {
                    JOptionPane.showMessageDialog(HelmetSharingSystem.this, "Please select a helmet to reserve.");
                }
            }
        });
    }
    
    private void loadHelmets() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    ResultSet rs = database.getAllHelmets();
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String model = rs.getString("model");
                        String size = rs.getString("size");
                        listModel.addElement(new SafetyHelmet(id, model, size));
                    }
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public void setUsername(String name) { 
        username = name;
        database.setUsername(name); 
    }
    
    public static HelmetSharingSystem getInstance(Connection conn)  { // 接受Connection作為參數
        if (instance == null) {
            instance = new HelmetSharingSystem(conn); // 創建時傳遞Connection
        }
        return instance;
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Login");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(300, 150);
                frame.setLocationRelativeTo(null);
                try {
					frame.add(new Loginpanel(HelmetSharingSystem));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                frame.setVisible(true);
            }
        });
        
        String server = "jdbc:mysql://140.119.19.73:3315/";
        String database = "112306089";
        String username = "112306089";
        String password = "mmwab";
        String url = server + database + "?useSSL=false";
        try {
        	 Connection conn = DriverManager.getConnection(url, username, password);
        	 System.out.println("DB connect");
        	 HelmetSharingSystem homeF = new HelmetSharingSystem(conn);
        	 
        	 
        
    } catch (SQLException e) {
        e.printStackTrace();
        
    }
  
    }
    
    
}