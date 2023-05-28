package login_SignUp_GUI;

import SQL_connection.AuthenticationService;
import clientServer.SendToServer;
import clientServer.SocketClient;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import javax.swing.*;

/**
 * @author YairF
 */
public final class LoginGUI extends AuthenticationChecker implements ActionListener {

    private final SocketClient socketClient;
    private SendToServer toServer;
    private JFrame frame;
    private JPanel panel;
    private JLabel passwordLabel;
    private JLabel name;
    private JLabel title;
    private JTextField email;
    private JButton loginButton, signup;
    private JPasswordField password;

    public LoginGUI(JFrame frame) {
        this.frame = frame;
        this.panel = new JPanel();
        this.socketClient = new SocketClient();
        createLogin();
    }

    public SocketClient getSocketClient() {
        return socketClient;
    }

    public JTextField getEmail() {
        return email;
    }

    public JPasswordField getPassword() {
        return password;
    }

    public void createLogin() {
        ImageIcon icon = new ImageIcon(System.getProperty("user.dir") + "\\src\\main\\java\\images\\lock.jpg");
        panel.setLayout(null);

        frame.setIconImage(icon.getImage());
        frame.setTitle("LOGIN");
        frame.setLocation(new Point(500, 300));
        frame.add(panel);
        frame.setSize(new Dimension(400, 300));
        //TODO: לשנות שאם המשתמש הגדיר את עצמו כזמין אז אם הוא סוגר את האפליקציה האפליקצייה עדיין תרוץ ולא תסגר
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        title = new JLabel("LOGIN");
        title.setBounds(120, 10, 170, 50);
        title.setFont(new Font("Serif", Font.BOLD, 48));
        panel.add(title);

        name = new JLabel("Mail");
        name.setBounds(100, 65, 70, 20);
        panel.add(name);

        email = new JTextField();
        email.setBounds(100, 87, 193, 28);
        email.addActionListener(this);
        panel.add(email);

        passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(100, 115, 70, 20);
        panel.add(passwordLabel);

        password = new JPasswordField();
        password.setBounds(100, 135, 193, 28);
        password.addActionListener(this);
        panel.add(password);

        loginButton = new JButton("Login");
        loginButton.setBounds(150, 180, 90, 25);
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(Color.BLACK);
        loginButton.addActionListener(this);
        panel.add(loginButton);

        signup = new JButton("Signup");
        signup.setBounds(20, 220, 90, 25);
        signup.setForeground(Color.BLUE);
        signup.setBackground(Color.white);
        signup.addActionListener(this);
        panel.add(signup);

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == email) {
            password.requestFocus();
        }
        if (e.getSource() == password) {
            loginButton.requestFocus();
        }
        if (e.getSource() == loginButton) {
            actionLoginButton();
        }
        if (e.getSource() == signup) {
            panel.setVisible(false);
            new SignupGUI(frame);
        }
    }

    private void actionLoginButton() {
        try {
            getSocketClient().connectToServer();
            String email = getEmail().getText();
            String password = getPassword().getText();


            //ChecksAndSendsEmailGUI object
            ChecksAndSendsEmailGUI checksAndSendsEmailGUI = new ChecksAndSendsEmailGUI(email, frame, panel, socketClient);

            /**
             * If the password and email are correct, go to the socket server and ask if such an email exists in the system.
             * If it exists - the server goes to the DB and takes out the password and the salt to coordinate the passwords.
             * If not - asks the user to enter the system.
             */
            if (isValidEmail(email) && isValidPassword(password)) {
                initializeCustomer("checkEmail", email);
                getSocketClient().outToServerObject(toServer);

                AuthenticationService inputPassword = getSocketClient().inFromServerObject();

                /**
                 * If isPasswordExists() == true, this means that there is such an email in the system.
                 */
                if (inputPassword.isPasswordExists()) {
                    if (inputPassword.getPassword().equals(hashPassword(password, inputPassword.getSalt()))) {
                        checksAndSendsEmailGUI.sendingAnEmail();
                    } else {
                        /**
                         * Otherwise, if the email exists in the system but the password does not match,
                         * we will save the login attempt of that user in the database.
                         */
                        initializeCustomer("insertToLoginAttempts", email);
                        getSocketClient().outToServerObject(toServer);
                        checksAndSendsEmailGUI.showLoginErrorNotification("The email or password does not match.");
                    }
                } else {
                    /**
                     * Otherwise, if such an email does not exist, because there is no password we found in the database,
                     * then the user is sent a message to register in the system.
                     */
                    checksAndSendsEmailGUI.showLoginErrorNotification("You are not registered with us!\nGo to signup to register in the system.");
                }
            } else {
                //Otherwise, if the password or email is incorrect, an error message will be displayed.
                checksAndSendsEmailGUI.showLoginErrorNotification("EMAIL OR PASSWORD IS INCORRECT");
            }
            /**
             * IOException - This is a checked exception that is thrown when an I/O operation fails or is interrupted.
             * ClassNotFoundException - This is a checked exception thrown when a class cannot be found during runtime. For example,
             *          if you try to deserialize an object from the server and the class for that object is not available on the classpath.
             * NoSuchAlgorithmException - This is a checked exception thrown when a cryptographic algorithm is requested but not available in the environment.
             *          For example, if you try to use an encryption algorithm that is not supported by your Java installation
             */
        } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void initializeCustomer(String requestType, String email) {
        toServer = new SendToServer();
        toServer.setTypeClient("VOLUNTEER");
        toServer.setLoginOrRegister("LOGIN");
        toServer.setEmail(email);
        toServer.setRequestType(requestType);
    }


    //TODO: ברגע שהמשתמש יוצא מהמערכת אז לנתק אותו מהשרת

}
