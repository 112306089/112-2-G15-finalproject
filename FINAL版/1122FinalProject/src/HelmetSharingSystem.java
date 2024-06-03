import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HelmetSharingSystem extends JPanel {
    private JList<Helmet> helmetList;
    private DefaultListModel<Helmet> listModel;
    private JButton reserveButton;
    private JButton returnButton;
    private String username;

    private HelmetDataBase helmetDataBase;

    public HelmetSharingSystem(String username) {
        super();
        this.username = username;
        setLayout(new BorderLayout());
        helmetDataBase = new HelmetDataBase();

        listModel = new DefaultListModel<>();
        helmetList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(helmetList);
        add(scrollPane, BorderLayout.CENTER);

        reserveButton = new JButton("Reserve");
        reserveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Helmet selectedHelmet = helmetList.getSelectedValue();
                if (selectedHelmet != null && selectedHelmet.isAvailable() == 1) {
                    try {
                        helmetDataBase.rentHelmet(selectedHelmet.getId(), username);
                        selectedHelmet.setAvailable(0);
                        helmetList.repaint();
                        JOptionPane.showMessageDialog(HelmetSharingSystem.this, "Successfully reserved!");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(HelmetSharingSystem.this, ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(HelmetSharingSystem.this, "Please select an available helmet to reserve.");
                }
            }
        });

        returnButton = new JButton("Return");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Helmet selectedHelmet = helmetList.getSelectedValue();
                if (selectedHelmet != null && selectedHelmet.isAvailable() == 0) {
                    try {
                        helmetDataBase.returnHelmet(selectedHelmet.getId(), username);
                        selectedHelmet.setAvailable(1); // 更新頭盔的狀態
                        helmetList.repaint(); // 重新繪製列表
                        JOptionPane.showMessageDialog(HelmetSharingSystem.this, "Helmet returned successfully.");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(HelmetSharingSystem.this, ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(HelmetSharingSystem.this, "Please select a reserved helmet to return.");
                }
            }
        });


        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.add(returnButton);
        buttonPanel.add(reserveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadHelmets();
    }

    private void loadHelmets() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ResultSet rs = null; // 声明 ResultSet
                try {
                    rs = helmetDataBase.getAllHelmets(); // 赋值 ResultSet
                    listModel.clear();
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String model = rs.getString("model");
                        String size = rs.getString("size");
                        int available = rs.getInt("available");
                        String username = rs.getString("username");

                        Helmet helmet = new SafetyHelmet(id, model, size, available);
                        helmet.setAvailable(available);
                        helmet.setUsername(username);
                        listModel.addElement(helmet);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    if (rs != null) { 
                        try {
                            rs.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    public void setUsername(String name) {
        username = name;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Login");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(300, 150);
                frame.setLocationRelativeTo(null);
                Loginpanel loginPanel = new Loginpanel(frame);
                frame.add(loginPanel);
                frame.setVisible(true);
            }
        });
    }
}
