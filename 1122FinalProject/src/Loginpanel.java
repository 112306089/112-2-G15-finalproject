import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

class Loginpanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton enrollButton;
    private User user; 
    private HelmetSharingSystem frame; 

    private String username = "111306069";
    private String password = "111306069";

    public Loginpanel() {
        setLayout(new GridLayout(3, 2));
        frame = new HelmetSharingSystem();
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
            		// 需要檢查User是否存在和密碼是否正確
    				user.checkUserExist(usernameField.getText());
    				user.checkPassword(usernameField.getText(), passwordField.getText());
    				
    				//隱藏login的視窗
    				setVisible(false);
    				//創home page視窗
    				frame = new HelmetSharingSystem(); 
    				frame.setUsername(usernameField.getText());
    				frame.setVisible(true);
    				
    				
    			}catch (UserError error){
    				JOptionPane.showMessageDialog(null, "UserError: Can't find the user", "Error", JOptionPane.ERROR_MESSAGE);
    			}catch (PasswordError error){
    				JOptionPane.showMessageDialog(null, "PasswordError: Password is wrong", "Error", JOptionPane.ERROR_MESSAGE);
    			}
            }
        });
        add(loginButton);
    }
}
