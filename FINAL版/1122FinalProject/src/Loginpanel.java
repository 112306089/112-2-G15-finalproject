import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

class Loginpanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JFrame frame;
    private HelmetDataBase helmetDataBase;

    private String defaultUsername = "111306069";
    private String defaultPassword = "111306069";

    public Loginpanel(JFrame frame) {
        this.frame = frame;
        setLayout(new GridLayout(3, 2));
        helmetDataBase = new HelmetDataBase();

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        add(usernameLabel);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        add(passwordLabel);
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String inputUsername = usernameField.getText();
                String inputPassword = new String(passwordField.getPassword());

                try (ResultSet rs = helmetDataBase.getUserInfo(inputUsername, inputPassword)) {
                    if (rs != null && rs.next()) {
                        System.out.println("rs correct");
                        String username = rs.getString("Username");
                        frame.getContentPane().removeAll();
                        frame.add(new HelmetSharingSystem(username));
                        frame.revalidate();
                        frame.repaint();
                    } else {
                        System.out.println("ResultSet is empty or closed.");
                        JOptionPane.showMessageDialog(Loginpanel.this, "Invalid username or password.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(Loginpanel.this, "Error logging in.");
                }
            }
        });



        add(loginButton);

        registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String inputUsername = usernameField.getText();
                String inputPassword = new String(passwordField.getPassword());
                if (inputUsername.equals("") || inputPassword.equals("")) {
                    JOptionPane.showMessageDialog(Loginpanel.this, "Username or password cannot be empty.");
                } else {
                    try {
                        helmetDataBase.registerUser(inputUsername, inputPassword);
                        defaultUsername = inputUsername;
                        defaultPassword = inputPassword;
                        JOptionPane.showMessageDialog(Loginpanel.this, "Registration successful.");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(Loginpanel.this, "Error registering user.");
                    }
                }
            }
        });
        add(registerButton);
    }
}
