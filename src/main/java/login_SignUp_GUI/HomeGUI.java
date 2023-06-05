package login_SignUp_GUI;

import DTO.VolunteerDTO;
import ExcelReader.ExcelReader;
import ExcelReader.Location;
import clientServer.SendToServer;
import clientServer.SocketClient;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.Date;
import java.util.Arrays;

public class HomeGUI implements ActionListener {

    private static boolean firstTime = true;
    private final SocketClient socketClient;
    private SendToServer toServer;
    private VolunteerDTO volunteerDTO;
    private JFrame frame;
    private JLabel title;
    private JPanel panel;
    private JPanel informationPanel;
    private JPanel historyPanel;
    private JButton button;
    private JLabel firstNameLabel;
    private JLabel lastNameLabel;
    private JLabel emailLabel;
    private JLabel phoneLabel;
    private JLabel addressLabel;
    private JLabel birthDateLabel;
    private final JButton EDIT_BUTTON = new JButton("Edit");

    public HomeGUI(SocketClient socketClient) {
        try {
            this.frame = new JFrame();
            this.panel = new JPanel();
            this.informationPanel = new JPanel();
            this.historyPanel = new JPanel();
            this.button = new JButton();
            this.socketClient = socketClient;
            initializeCustomer("Information", new ExcelReader().getRandomLocation());
            this.socketClient.outToServerObject(this.toServer);
            //Get data from the server
            this.volunteerDTO = this.socketClient.inFromServerDTO();

            startListeningForUpdates();
            //TODO: לעדכן בשרת שיעדכן את מסד הנתונים ואת הנתונים ברשימה שמכילה את כל הנתונים של המתנדבים
            updateLocation();
            // Code to execute every half minute
            startTimer(30000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateLocation();
                }
            });

            createHomePage();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public SendToServer getToServer() {
        return toServer;
    }

    public static boolean isFirstTime() {
        return firstTime;
    }

    public static void setFirstTime(boolean firstTime) {
        HomeGUI.firstTime = firstTime;
    }

    public SocketClient getSocketClient() {
        return socketClient;
    }

    public VolunteerDTO getVolunteerDTO() {
        return volunteerDTO;
    }

    public void setVolunteerDTO(VolunteerDTO volunteerDTO) {
        this.volunteerDTO = volunteerDTO;
    }

    public JLabel getTitle(String text) {
        title = new JLabel(text);
        title.setBounds(70, 10, 210, 50);
        title.setFont(new Font("Serif", Font.BOLD, 28));
        return title;
    }

    public JLabel getName(String name) {
        JLabel subtitle = new JLabel("Hello " + name + "!");
        subtitle.setBounds(50, 60, 210, 30);
        subtitle.setFont(new Font("Serif", Font.PLAIN, 16));
        return subtitle;
    }

    public JButton getButton(JPanel thisPanel) {
        button.setBounds(10, 10, 35, 30); // set button position and size
        ImageIcon icon = new ImageIcon(System.getProperty("user.dir") + "\\src\\main\\java\\images\\Navigation bars app.png");
        button.setIcon(icon);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPopupMenu popupMenu = new JPopupMenu();

                JMenuItem menuItem1 = new JMenuItem("Home");
                JMenuItem menuItem2 = new JMenuItem("Personal Information");
                JMenuItem menuItem3 = new JMenuItem("History");

                // Add action listeners to the menu items
                menuItem1.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        home();
                        System.out.println("Hello from Option 1");
                    }
                });
                menuItem2.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        personalInformation();
                        System.out.println("Hello from Option 2");
                    }
                });
                menuItem3.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        history();
                        System.out.println("Hello from Option 3");
                    }
                });

                popupMenu.add(menuItem1);
                popupMenu.add(menuItem2);
                popupMenu.add(menuItem3);

                popupMenu.show(button, button.getWidth(), button.getHeight());
            }
        });
        return button;
    }

    public void startTimer(int interval, ActionListener actionListener) {
        Timer timer = new Timer(interval, actionListener);
        timer.start();
    }

    private void createHomePage() {
        frame.setTitle("HOME");
        frame.setLocation(new Point(570, 150));
        frame.setSize(new Dimension(350, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        home();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                //TODO: שמור שעת יציאה מהאפליקציה
                getSocketClient().closeConnection();
                System.out.println("Exiting...");
                // add code to insert into DB here
            }
        });
    }

    public void home() {
        if (isFirstTime()) {
            panel.add(getTitle("Volunteer Center"));
            panel.add(getName(getVolunteerDTO().getFirstName() + " " + getVolunteerDTO().getLastName()));
        }
        panel.setLayout(null);
        panel.add(getButton(panel));
        panel.setVisible(true);
        informationPanel.setVisible(false);
        historyPanel.setVisible(false);
        frame.add(panel);
    }

    public void personalInformation() {

        informationPanel.setLayout(null);

        informationPanel.add(getTitle("Information"));
        informationPanel.add(getButton(informationPanel));


        if (isFirstTime()) {
            firstNameLabel = new JLabel("First Name: " + getVolunteerDTO().getFirstName());
            firstNameLabel.setBounds(50, 100, 210, 30);
            firstNameLabel.setFont(new Font("Serif", Font.PLAIN, 16));
            informationPanel.add(firstNameLabel);

            lastNameLabel = new JLabel("Last Name: " + getVolunteerDTO().getLastName());
            lastNameLabel.setBounds(50, 130, 210, 30);
            lastNameLabel.setFont(new Font("Serif", Font.PLAIN, 16));
            informationPanel.add(lastNameLabel);

            emailLabel = new JLabel("Email: " + getVolunteerDTO().getEmail());
            emailLabel.setBounds(50, 160, 210, 30);
            emailLabel.setFont(new Font("Serif", Font.PLAIN, 16));
            informationPanel.add(emailLabel);

            phoneLabel = new JLabel("Phone: " + getVolunteerDTO().getPhone());
            phoneLabel.setBounds(50, 190, 210, 30);
            phoneLabel.setFont(new Font("Serif", Font.PLAIN, 16));
            informationPanel.add(phoneLabel);

            addressLabel = new JLabel("Address: " + getVolunteerDTO().getAddress());
            addressLabel.setBounds(50, 220, 210, 30);
            addressLabel.setFont(new Font("Serif", Font.PLAIN, 16));
            informationPanel.add(addressLabel);

            birthDateLabel = new JLabel("Birth Date: " + getVolunteerDTO().getBirthDate());
            birthDateLabel.setBounds(50, 250, 210, 30);
            birthDateLabel.setFont(new Font("Serif", Font.PLAIN, 16));
            informationPanel.add(birthDateLabel);

            setFirstTime(false);
        }

        EDIT_BUTTON.setBounds(75, 300, 100, 30);
        informationPanel.add(EDIT_BUTTON);
        EDIT_BUTTON.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JLabel jLabel : Arrays.asList(
                        firstNameLabel,
                        lastNameLabel,
                        emailLabel,
                        phoneLabel,
                        addressLabel,
                        birthDateLabel)) {
                    jLabel.setVisible(false);
                }
                EDIT_BUTTON.setVisible(false);
                dataEditing();
            }
        });

        informationPanel.setVisible(true);
        panel.setVisible(false);
        historyPanel.setVisible(false);
        frame.add(informationPanel);

    }

    public void history() {
        historyPanel.setLayout(null);

        historyPanel.add(getTitle("History"));
        historyPanel.add(getButton(historyPanel));

        historyPanel.setVisible(true);
        panel.setVisible(false);
        informationPanel.setVisible(false);
        frame.add(historyPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public void initializeCustomer(String requestType, Location location) {
        this.toServer = new SendToServer();
        this.toServer.setTypeClient("VOLUNTEER");
        this.toServer.setLoginOrRegister("HOME");
        this.toServer.setRequestType(requestType);
        this.toServer.setLocation(location);
    }

    public void dataEditing() {
        JTextField firstNameField = new JTextField(getVolunteerDTO().getFirstName());
        firstNameField.setBounds(50, 100, 200, 30);
        informationPanel.add(firstNameField);

        JTextField lastNameField = new JTextField(getVolunteerDTO().getLastName());
        lastNameField.setBounds(50, 130, 200, 30);
        informationPanel.add(lastNameField);

        JTextField emailField = new JTextField(getVolunteerDTO().getEmail());
        emailField.setBounds(50, 160, 200, 30);
        informationPanel.add(emailField);

        JTextField phoneField = new JTextField(getVolunteerDTO().getPhone());
        phoneField.setBounds(50, 190, 200, 30);
        informationPanel.add(phoneField);

        JTextField addressField = new JTextField(getVolunteerDTO().getAddress());
        addressField.setBounds(50, 220, 200, 30);
        informationPanel.add(addressField);

        JDateChooser birthDateField = new JDateChooser();
        birthDateField.setBounds(50, 250, 193, 28);
        informationPanel.add(birthDateField);

        JButton saveButton = new JButton("Save");
        saveButton.setBounds(75, 300, 100, 30);
        informationPanel.add(saveButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isDataChanged = false;
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                String address = addressField.getText();
                Date birthDate = new Date(birthDateField.getDate().getTime());

                boolean firstNameChanged = !firstName.equals(volunteerDTO.getFirstName());
                boolean lastNameChanged = !lastName.equals(volunteerDTO.getLastName());
                boolean emailChanged = !email.equals(volunteerDTO.getEmail());
                boolean phoneChanged = !phone.equals(volunteerDTO.getPhone());
                boolean addressChanged = !address.equals(volunteerDTO.getAddress());
                boolean birthDateChanged = !birthDate.equals(volunteerDTO.getBirthDate());

                if (firstNameChanged) {
                    volunteerDTO.setFirstName(firstName);
                    firstNameLabel.setText("First Name: " + firstName);
                    isDataChanged = true;
                }
                if (lastNameChanged) {
                    volunteerDTO.setLastName(lastName);
                    lastNameLabel.setText("Last Name: " + lastName);
                    isDataChanged = true;
                }
                if (emailChanged) {
                    volunteerDTO.setEmail(email);
                    emailLabel.setText("Email: " + email);
                    isDataChanged = true;
                }
                if (phoneChanged) {
                    volunteerDTO.setPhone(phone);
                    phoneLabel.setText("Phone: " + phone);
                    isDataChanged = true;
                }
                if (addressChanged) {
                    volunteerDTO.setAddress(address);
                    addressLabel.setText("Address: " + address);
                    isDataChanged = true;
                }
                if (birthDateChanged) {
                    volunteerDTO.setBirthDate(birthDate);
                    birthDateLabel.setText("Birth Date: " + birthDate);
                    isDataChanged = true;
                }

                EDIT_BUTTON.setVisible(true);
                saveButton.setVisible(false);

                for (JLabel jLabel : Arrays.asList(
                        firstNameLabel,
                        lastNameLabel,
                        emailLabel,
                        phoneLabel,
                        addressLabel,
                        birthDateLabel)) {
                    jLabel.setVisible(true);
                }

                for (JTextField jTextField : Arrays.asList(
                        firstNameField,
                        lastNameField,
                        emailField,
                        phoneField,
                        addressField)) {
                    jTextField.setVisible(false);
                }
                birthDateField.setVisible(false);

                if (isDataChanged) {
                    //TODO: Call a method to save the updated volunteer data to the server
                    //TODO: אם אין שינוי - אל תשלח לשרת, אם יש שינוי - תעדכן את השרת בשינוי
                }
            }
        });
    }

    //TODO: לבדוק למה זה לא עובד.
    public void startListeningForUpdates() {
        new Thread(() -> {
            try {
                InputStream inputStream = getSocketClient().getSocket().getInputStream();
                byte[] buffer = new byte[1024];
                int bytesRead = inputStream.read(buffer);
                String htmlString = new String(buffer, 0, bytesRead);

                // Show the message in a modal dialog box with a clickable link
                JEditorPane editorPane = new JEditorPane("text/html", htmlString);
                editorPane.setEditable(false);
                editorPane.addHyperlinkListener(e -> {
                    if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                        try {
                            Desktop.getDesktop().browse(e.getURL().toURI());
                        } catch (IOException | URISyntaxException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                JOptionPane.showMessageDialog(this.frame, editorPane, "New Call", JOptionPane.PLAIN_MESSAGE);

                // Handle the volunteer's response
                int response = JOptionPane.showConfirmDialog(this.frame, "Do you want to accept this call?", "New Call", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    // The volunteer accepted the call
                    System.out.println("Volunteer accepted the call");
                } else {
                    // The volunteer declined the call
                    System.out.println("Volunteer declined the call");
                }

                // Convert the byte array back into an HTML string
                System.out.println(htmlString);
            } catch (IOException e) {
                //throw new RuntimeException(e);
            }
        }).start();
    }


    public void updateLocation() {
        Location location = (new ExcelReader().getRandomLocation());
        initializeCustomer("UpdateLocation", location);
        getSocketClient().outToServerObject(getToServer());
    }

    //TODO: למחוק את הפונקציה הראשית
    public static void main(String[] args) {
        new HomeGUI(new SocketClient());
    }
}
