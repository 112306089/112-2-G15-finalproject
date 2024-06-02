import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.*;

// login 的class
class Loginpanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton enrollButton;
    private User user; 
    private HelmetSharingSystem frame;

    private String username = "111306069";
    private String password = "111306069";

    public Loginpanel(HelmetSharingSystem frame) throws SQLException { 
        setLayout(new GridLayout(3, 2));
        this.frame = frame; // 使用傳遞進來的HelmetSharingSystem對象
        user = new User();

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        add(usernameLabel);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        add(passwordLabel);
        add(passwordField);

        enrollButton = new JButton("Enroll");
        enrollButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (usernameField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "UserError: Username can't be empty", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else if (passwordField.getPassword().length != 8) {
                    JOptionPane.showMessageDialog(null, "PasswordError: Password should be 8 characters", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        user.add(usernameField.getText(), new String(passwordField.getPassword()));
                    } catch (PasswordError | UserError ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        add(enrollButton);

        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    user.checkUserExist(usernameField.getText());
                    user.checkPassword(usernameField.getText(), passwordField.getText());

                    // 隱藏登錄窗口並顯示主頁面
                    JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(Loginpanel.this);
                    loginFrame.setVisible(false);
                    
                    // 確保 frame 對象不為空
                    if (frame != null) {
                        frame.setVisible(true); // 顯示主頁面
                        frame.setUsername(usernameField.getText()); // 設置用戶名
                    } else {
                        System.err.println("Frame object is null");
                    }
                    
                } catch (UserError error) {
                    JOptionPane.showMessageDialog(null, "UserError: Can't find the user", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (PasswordError error) {
                    JOptionPane.showMessageDialog(null, "PasswordError: Password is wrong", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        add(loginButton);
    }

}
