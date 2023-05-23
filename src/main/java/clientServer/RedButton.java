package clientServer;

import ExcelReader.*;

import java.awt.event.*;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

public class RedButton implements Server, ActionListener {
    private JLabel title;
    private JFrame frame;
    private JPanel panel;
    private JTextField phoneField;
    private JComboBox<String> helpComboBox;
    private JButton button;
    private ExcelReader reader = new ExcelReader();
    private Socket clientSocket;
    private ObjectOutputStream out;

    public RedButton() {
        this.frame = new JFrame();
        this.button = new JButton();
        this.panel = new JPanel();
        this.panel.setLayout(null);

        frame.setTitle("HELP!");
        frame.setSize(new Dimension(400, 300));
        frame.setLocation(new Point(500, 300));
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.getContentPane().add(panel, BorderLayout.CENTER);

        title = new JLabel("YOUR PHONE:");
        title.setBounds(100, 10, 200, 50);
        title.setFont(new Font("Serif", Font.BOLD, 28));
        panel.add(title);

        phoneField = new JTextField(10);
        phoneField.setBounds(100, 65, 200, 28);
        phoneField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || phoneField.getText().length() >= 10) {
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
        panel.add(phoneField);

        String helpOptions[] = new String[6];
        helpOptions[0] = "propulsion";
        helpOptions[1] = "puncture";
        helpOptions[2] = "oil/water/fuel";
        helpOptions[3] = "locked car";
        helpOptions[4] = "door";
        helpOptions[5] = "area extraction";
        helpComboBox = new JComboBox<>(helpOptions);
        helpComboBox.setBounds(100, 105, 200, 28);
        panel.add(helpComboBox);

        button.setBounds(145, 140, 100, 100);
        try {
            String path = System.getProperty("user.dir");
            String imgPath = path + "\\src\\main\\java\\images\\redButton.png";
            Image img = ImageIO.read(new File(imgPath)); // replace with your image path
            button.setIcon(new ImageIcon(img)); // set button icon
        } catch (IOException e) {
            e.printStackTrace();
        }
        button.addActionListener(this);
        panel.add(button);

        frame.add(panel);
        frame.setVisible(true);
        connectToServer();
    }

    public void actionPerformed(ActionEvent e) {
        try {
            String kindOfHelp = (String) helpComboBox.getSelectedItem();
            String phone = phoneField.getText();

            if (phone.equals("") || phone.length() < 10) {
                JOptionPane.showMessageDialog(
                        null,
                        "Enter your cell phone number",
                        "Error", JOptionPane.ERROR_MESSAGE);
                phoneField.setText("");
                helpComboBox.setSelectedIndex(0);
                phoneField.requestFocus();
            } else {
                Location location = (reader.getRandomLocation());
                SendToServer toServer = new SendToServer();
                toServer.setKindOfHelp(kindOfHelp);
                toServer.setPhone(phone);
                toServer.setLocation(location);
                toServer.setTypeClient("CLIENT");

                out.writeObject(toServer);
                out.flush();
                clientSocket.shutdownInput();
                clientSocket.shutdownOutput();
                panel.setVisible(false);
                sent();
            }
        } catch (IOException ex) {
            System.err.println("Error sending object to server: " + ex.getMessage());
        }
    }

    public void sent() {
        JPanel panelMessage = new JPanel();
        JLabel message;
        message = new JLabel("We find you the volunteer!");
        message.setFont(new Font("Sans-serif", Font.BOLD, 18));
        panelMessage.add(message);
        frame.add(panelMessage);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                frame.setVisible(false);
                closeConnection();
                System.exit(0);
            }
        }, 2000);
    }

    public static void main(String[] args) {
        new RedButton();
    }

    @Override
    public void connectToServer() {
        // connect to the server
        try {
            clientSocket = new Socket("localhost", PORT); // replace with your server address and port number
            OutputStream outputStream = clientSocket.getOutputStream();
            out = new ObjectOutputStream(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeConnection() {
        try {
            out.close();
            clientSocket.close();
            clientSocket.shutdownOutput();  //מאותת לשרת שסיימתי לשלוח לו נתונים
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}