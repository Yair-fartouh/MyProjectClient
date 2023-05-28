package login_SignUp_GUI;

import DTO.VolunteerDTO;
import clientServer.SendToServer;
import clientServer.SocketClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class HomeGUI implements ActionListener {

    private final SocketClient socketClient;
    private SendToServer toServer;
    private JFrame frame;
    private JLabel title;
    private JPanel panel;
    private JPanel informationPanel;
    private JPanel historyPanel;
    private JButton button;

    public HomeGUI(SocketClient socketClient) {
        this.frame = new JFrame();
        this.panel = new JPanel();
        this.informationPanel = new JPanel();
        this.historyPanel = new JPanel();
        this.button = new JButton();
        this.socketClient = socketClient;
        createHomePage();
    }

    public SocketClient getSocketClient() {
        return socketClient;
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

    private void createHomePage() {
        frame.setTitle("HOME");
        frame.setLocation(new Point(570, 150));
        frame.setSize(new Dimension(350, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        home();
        frame.setVisible(true);
        // add window listener to the frame
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                //TODO: שמור שעת יציאה מהאפליקציה
                System.out.println("Exiting...");
                // add code to insert into DB here
            }
        });
    }

    //TODO: לעשות DTO
    public void home() {
        panel.setLayout(null);

        panel.add(getTitle("Volunteer Center"));
        //TODO: לשנות את השם למה שקיבלנו מהשרת
        panel.add(getName("Test"));
        panel.add(getButton(panel));

        panel.setVisible(true);
        informationPanel.setVisible(false);
        historyPanel.setVisible(false);
        frame.add(panel);
    }

    public void personalInformation() {
        try {
            informationPanel.setLayout(null);

            informationPanel.add(getTitle("Information"));
            informationPanel.add(getButton(informationPanel));
            initializeCustomer("Information");
            getSocketClient().outToServerObject(toServer);

            VolunteerDTO volunteerDTO = getSocketClient().inFromServerDTO();
            System.out.println(volunteerDTO.getEmail());

            informationPanel.setVisible(true);
            panel.setVisible(false);
            historyPanel.setVisible(false);
            frame.add(informationPanel);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
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


    public void initializeCustomer(String requestType) {
        toServer = new SendToServer();
        toServer.setTypeClient("VOLUNTEER");
        toServer.setLoginOrRegister("HOME");
        toServer.setRequestType(requestType);
    }

    //TODO: למחוק את הפונקציה הראשית
    public static void main(String[] args) {
        new HomeGUI(new SocketClient());
    }
}
