package login_SignUp_GUI;

import login_to_verify_Email.LoginToVerify;
import login_to_verify_Email.VerificationProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChecksAndSendsEmailGUI {
    private Thread thread;
    private JFrame frame;
    private JPanel panel;
    private String email;
    private boolean done = false;

    public JFrame getFrame() {
        return frame;
    }

    public JPanel getPanel() {
        return panel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public ChecksAndSendsEmailGUI(String email, JFrame frame, JPanel panel) {
        //frame.setSize(new Dimension(400, 300));
        this.frame = frame;
        this.panel = panel;
        setEmail(email);
    }

    public void sendingAnEmail() {
        JProgressBar progressBar;
        LoginToVerify l = new LoginToVerify(getEmail());
        getPanel().setVisible(false);
        JPanel pbar = new JPanel();
        pbar.setLayout(null);
        progressBar = new JProgressBar(0, 100);
        progressBar.setBounds(100, 160, 193, 20);
        progressBar.setStringPainted(true);
        pbar.add(progressBar);
        getFrame().add(pbar);

        // שרשור שיגור אירוע
        new SwingWorker<Void, Integer>() {
            // מפרסמת תוצאות ביינים אבל לא ללקוח
            @Override
            protected Void doInBackground() throws Exception {
                for (int i = 0; i <= 100; i += 10) {
                    Thread.sleep(800);
                    publish(i);
                    if (i == 0) {
                        Thread emailThread = new Thread(() -> {
                            VerificationProperties verifiedPassword;
                            verifiedPassword = l.sendCodeInEmail();
                            new EmailVerificationGUI(verifiedPassword, getFrame());
                            setDone(true);
                        });
                        setThread(emailThread);
                        getThread().start();
                    }
                    if (isDone()) {
                        break;
                    }
                }
                return null;
            }

            // מעדכן את ממשק המשתמש בתוצאות הביינים
            @Override
            protected void process(List<Integer> chunks) {
                int value = chunks.get(chunks.size() - 1);
                progressBar.setValue(value);
            }

            // מעדכן תוצאה סופית ללקוח
            @Override
            protected void done() {
                try {
                    getThread().join();
                    getThread().stop();
                    progressBar.setValue(100);
                    progressBar.setVisible(false);
                    getPanel().setVisible(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    public void showLoginErrorNotification(String message) {
        Container glassPane;
        JLabel errorMessage = new JLabel("<html>" + message.replace("\n", "<br>") + "</html>", SwingConstants.CENTER);
        getFrame().setGlassPane(new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(0, 0, 0, 150));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        });
        glassPane = (Container) getFrame().getGlassPane();
        glassPane.addMouseListener(new MouseAdapter() {
        });
        glassPane.setLayout(null);
        errorMessage.setBounds(90, 60, 220, 100);
        errorMessage.setForeground(Color.RED);
        glassPane.add(errorMessage);
        glassPane.setVisible(true);

        //Pause the error screen for 3 seconds and turn it off.
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                glassPane.setVisible(false);
            }
        }, 3000);
    }
}
