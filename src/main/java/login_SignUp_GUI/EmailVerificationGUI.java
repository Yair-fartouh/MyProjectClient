package login_SignUp_GUI;

import clientServer.SocketClient;
import login_to_verify_Email.VerificationProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EmailVerificationGUI implements ActionListener {
    //TODO: מסך בידקת סיסמה שנשלחה לאימייל
    private final SocketClient socketClient;
    private JFrame frame;
    private JPanel panel = new JPanel();
    private JLabel title;
    private JTextField verifyThePassword;
    private JButton button;
    private VerificationProperties properties;

    /**
     * @param properties   - An object that contains the properties of the authentication.
     * @param frame
     * @param socketClient
     */
    public EmailVerificationGUI(VerificationProperties properties, JFrame frame, SocketClient socketClient) {
        this.socketClient = socketClient;
        setFrame(frame);
        setProperties(properties);
        initScreen();
    }

    public SocketClient getSocketClient() {
        return socketClient;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public JLabel getTitle() {
        return title;
    }

    public void setTitle(JLabel title) {
        this.title = title;
    }

    public JTextField getVerifyThePassword() {
        return verifyThePassword;
    }

    public void setVerifyThePassword(JTextField verifyThePassword) {
        this.verifyThePassword = verifyThePassword;
    }

    public JButton getButton() {
        return button;
    }

    public void setButton(JButton button) {
        this.button = button;
    }

    public VerificationProperties getProperties() {
        return properties;
    }

    public void setProperties(VerificationProperties properties) {
        this.properties = properties;
    }

    public void initScreen() {
        panel.setLayout(null);

        getFrame().setTitle("Password Authentication");
        getFrame().setLocation(new Point(500, 300));
        getFrame().add(getPanel());

        setTitle(new JLabel("Verify the password:"));
        getTitle().setBounds(75, 10, 260, 100);
        getTitle().setFont(new Font("Serif", Font.BOLD, 28));
        getPanel().add(getTitle());

        setVerifyThePassword(new JTextField());
        getVerifyThePassword().setBounds(100, 87, 193, 28);
        getVerifyThePassword().addActionListener(this);
        getPanel().add(getVerifyThePassword());

        setButton(new JButton("verify"));
        getButton().setBounds(150, 180, 90, 25);
        getButton().setForeground(Color.WHITE);
        getButton().setBackground(Color.BLACK);
        getButton().addActionListener(this);
        getPanel().add(getButton());

        getFrame().setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            if ((this.properties.getVerificationCode() == Integer.parseInt(verifyThePassword.getText()))
                    && (System.currentTimeMillis() <= this.properties.getEXPIRATION_TIME())) {
                getFrame().dispose();
                new HomeGUI(getSocketClient());
            } else {
                JOptionPane.showMessageDialog(null, "Match failed!");
            }
        }
    }
}
