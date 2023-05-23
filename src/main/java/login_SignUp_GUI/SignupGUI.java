package login_SignUp_GUI;

import clientServer.SendToServer;
import clientServer.Server;
import com.toedter.calendar.JDateChooser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;

public final class SignupGUI extends AuthenticationChecker implements Server, ActionListener {

    private final JFrame frame;
    private final JPanel panel;
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private JLabel passwordLabel;
    private JLabel confirmPasswordLabel;
    private JLabel emailLabel;
    private JLabel titleLabel;
    private JLabel fNameLabel;
    private JLabel lNameLabel;
    private JLabel bDateLabel;
    private JLabel phoneLabel;
    private JLabel addressLabel;
    private JTextField firstName;
    private JTextField lastName;
    private JTextField phoneNumber;
    private JTextField email;
    private JTextField address;
    private JButton LoginButton;
    private JButton Signup;
    private JPasswordField passwordJP;
    private JPasswordField confirmPasswordJP;
    private JDateChooser dateChooser;
    private SendToServer toServer;

    public SignupGUI(JFrame frame) {
        this.frame = frame;
        this.panel = new JPanel();
        createSignup();
    }

    public JTextField getPhoneNumber() {
        return phoneNumber;
    }

    public JDateChooser getDateChooser() {
        return dateChooser;
    }

    public JTextField getFirstName() {
        return firstName;
    }

    public JTextField getLastName() {
        return lastName;
    }

    public JTextField getEmail() {
        return email;
    }

    public JTextField getAddress() {
        return address;
    }

    public JPasswordField getPasswordJP() {
        return passwordJP;
    }

    public JPasswordField getConfirmPasswordJP() {
        return confirmPasswordJP;
    }

    public void createSignup() {
        ImageIcon icon = new ImageIcon(System.getProperty("user.dir") + "\\src\\main\\java\\images\\lock.jpg");
        panel.setLayout(null);

        frame.setIconImage(icon.getImage());
        frame.setTitle("SIGNUP");
        frame.setLocation(new Point(500, 200));
        frame.add(panel);
        frame.setSize(new Dimension(400, 560));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        titleLabel = new JLabel("SIGNUP");
        titleLabel.setBounds(105, 10, 200, 50);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 48));
        panel.add(titleLabel);

        fNameLabel = new JLabel("First name");
        fNameLabel.setBounds(100, 65, 70, 20);
        panel.add(fNameLabel);

        firstName = new JTextField();
        firstName.setBounds(100, 87, 193, 28);
        firstName.addActionListener(this);
        panel.add(firstName);

        lNameLabel = new JLabel("Last name");
        lNameLabel.setBounds(100, 115, 70, 20);
        panel.add(lNameLabel);

        lastName = new JTextField();
        lastName.setBounds(100, 135, 193, 28);
        lastName.addActionListener(this);
        panel.add(lastName);

        bDateLabel = new JLabel("Date of birth");
        bDateLabel.setBounds(100, 165, 70, 20);
        panel.add(bDateLabel);

        dateChooser = new JDateChooser();
        dateChooser.setBounds(100, 183, 193, 28);
        panel.add(dateChooser);

        phoneLabel = new JLabel("Phone number");
        phoneLabel.setBounds(100, 215, 90, 20);
        panel.add(phoneLabel);

        phoneNumber = new JTextField(10);
        phoneNumber.setBounds(100, 233, 193, 28);
        phoneNumber.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || phoneNumber.getText().length() >= 10) {
                    e.consume(); // לא שומר במשתנה את האותיות
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        panel.add(phoneNumber);
        addressLabel = new JLabel("Address");
        addressLabel.setBounds(100, 265, 80, 20);
        panel.add(addressLabel);

        address = new JTextField();
        address.setBounds(100, 283, 193, 28);
        address.addActionListener(this);
        panel.add(address);

        emailLabel = new JLabel("Mail");
        emailLabel.setBounds(100, 315, 70, 20);
        panel.add(emailLabel);

        email = new JTextField();
        email.setBounds(100, 331, 193, 28);
        email.addActionListener(this);
        panel.add(email);

        passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(100, 365, 70, 20);
        panel.add(passwordLabel);

        passwordJP = new JPasswordField();
        passwordJP.setBounds(100, 383, 193, 28);
        passwordJP.addActionListener(this);
        panel.add(passwordJP);

        confirmPasswordLabel = new JLabel("Confirm password");
        confirmPasswordLabel.setBounds(100, 415, 110, 20);
        panel.add(confirmPasswordLabel);

        confirmPasswordJP = new JPasswordField();
        confirmPasswordJP.setBounds(100, 435, 193, 28);
        confirmPasswordJP.addActionListener(this);
        panel.add(confirmPasswordJP);

        Signup = new JButton("Signup");
        Signup.setBounds(150, 470, 90, 25);
        Signup.setForeground(Color.WHITE);
        Signup.setBackground(Color.BLACK);
        Signup.addActionListener(this);
        panel.add(Signup);

        LoginButton = new JButton("Login");
        LoginButton.setBounds(20, 490, 90, 25);
        LoginButton.setForeground(Color.BLUE);
        LoginButton.setBackground(Color.white);
        LoginButton.addActionListener(this);
        panel.add(LoginButton);

        frame.show();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == email) {
            passwordJP.requestFocus();
        }
        if (e.getSource() == passwordJP) {
            confirmPasswordJP.requestFocus();
        }
        if (e.getSource() == confirmPasswordJP) {
            Signup.requestFocus();
        }
        if (e.getSource() == Signup) {
            actionSignupButton();
        }
        if (e.getSource() == LoginButton) {
            panel.setVisible(false);
            new LoginGUI(frame);
        }

    }

    private void actionSignupButton() {
        try {
            connectToServer();
            String inputServer;
            String firstN = getFirstName().getText();
            String lastN = getLastName().getText();
            Date date = new Date(getDateChooser().getDate().getTime());
            String residentialAddress = getAddress().getText();
            String number = getPhoneNumber().getText();
            String email = getEmail().getText();

            String salt = generateSalt();
            String password = getPasswordJP().getText();
            String confirmPass = getConfirmPasswordJP().getText();

            ChecksAndSendsEmailGUI checksAndSendsEmailGUI = new ChecksAndSendsEmailGUI(email, frame, panel);

            //TODO: לבדוק אם יש אימייל כזה קיים במערכת

            /**
             * First of all, if the passwords are equal and not empty.
             */
            if (password.equals(confirmPass) && !password.equals("")) {
                if (isValidEmail(email) && isValidPassword(password)) {
                    password = hashPassword(password, salt);

                    initializeCustomer("checkEmail", email, password, salt, date,
                            firstN, lastN, residentialAddress, number);
                    out.writeObject(toServer);
                    out.flush();

                    inputServer = (String) in.readObject();

                    /**
                     * If there is no password, it means that such an email does not exist and the user can register.
                     */
                    if (inputServer.equals("Does not exist")) {
                        //TODO: להתחבר למסד נתונים ולהכניס את salt and hashedPassword
                        //TODO: עכשיו עליך לשמור את כתובת המייל והסיסמה המוצפנת במסד הנתונים של המערכת
                        // וליצור משתמש חדש במסד הנתונים עם הפרטים הללו.
                        checksAndSendsEmailGUI.sendingAnEmail();
                        //status("successfully...");
                        //TODO: Home
                    } else {
                        if (inputServer.equals("Exist")) {
                            checksAndSendsEmailGUI.showLoginErrorNotification("קיים אימייל כזה");
                            this.email.setText("");
                        }
                    }
                } else {
                    checksAndSendsEmailGUI.showLoginErrorNotification("EMAIL OR PASSWORD IS INCORRECT");
                }
            } else {
                checksAndSendsEmailGUI.showLoginErrorNotification("Password verification is incorrect");
            }
        } catch (NoSuchAlgorithmException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void status(String message) {
        this.panel.setVisible(false);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);
        JLabel label = new JLabel(message);
        label.setBounds(100, 65, 300, 200);
        panel.add(label);

        Timer timer = new Timer(3000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                frame.remove(panel);
                frame.revalidate();
                frame.repaint();
                //TODO: להפעיל פונקציה של בית האפליקציה ולהעביר לה את הפריים
            }
        });
        // start the timer
        timer.setRepeats(false); // מפעיל אותו פעם אחת בלבד
        timer.start(); //מפעיל טיימר
    }

    public void initializeCustomer(String requestType, String email, String password, String salt, Date date,
                                   String firstN, String lastN, String residentialAddress, String number) {

        toServer = new SendToServer();
        toServer.setTypeClient("VOLUNTEER");
        toServer.setLoginOrRegister("SIGNUP");
        toServer.setFirstName(firstN);
        toServer.setLastName(lastN);
        toServer.setAddress(residentialAddress);
        toServer.setPhone(number);
        toServer.setDateOfBirth(date);
        toServer.setEmail(email);
        toServer.setPassword(password);
        toServer.setSalt(salt);
        toServer.setRequestType(requestType);
    }

    @Override
    public void connectToServer() {
        // connect to the server
        try {
            clientSocket = new Socket("localhost", PORT); // replace with your server address and port number
            OutputStream outputStream = clientSocket.getOutputStream();
            out = new ObjectOutputStream(outputStream);
            in = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeConnection() {
        try {
            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
