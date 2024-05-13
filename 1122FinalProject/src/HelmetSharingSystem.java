import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

class HelmetSharingSystem extends JFrame {
    private JList<Helmet> helmetList;
    private DefaultListModel<Helmet> listModel;
    private JButton reserveButton;
    private String username;

    private HelmetDatabase database;

    public HelmetSharingSystem() {
        super("Helmet Sharing System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        username = new String("");
        database = new HelmetDatabase();

        listModel = new DefaultListModel<>();
        helmetList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(helmetList);
        add(scrollPane, BorderLayout.CENTER);

        reserveButton = new JButton("Reserve");
        reserveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Helmet selectedHelmet = helmetList.getSelectedValue();
                if (selectedHelmet != null) {
                    database.deleteHelmet(selectedHelmet.getId());
                    listModel.removeElement(selectedHelmet);
                } else {
                    JOptionPane.showMessageDialog(HelmetSharingSystem.this, "Please select a helmet to reserve.");
                }
            }
        });
        add(reserveButton, BorderLayout.SOUTH);

        loadHelmets();
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
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Login");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(300, 150);
                frame.setLocationRelativeTo(null);
                frame.add(new Loginpanel());
                frame.setVisible(true);
            }
        });
    }
}