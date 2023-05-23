/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package login_to_verify_Email;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public abstract class SendingACodeByEmail {

    private final String UserName = "yourbusiness222@outlook.com";
    private final String Password = "ieteYe4EUUfYDP";
    private static final int EXPIRATION_TIME = 15 * 60 * 1000; // 15 minutes in milliseconds
    private String To;
    private int Code;

    public SendingACodeByEmail(String To) {
        this.To = To;
    }

    public int getCode() {
        return Code;
    }

    private void setCode(int Code) {
        this.Code = Code;
    }

    public final VerificationProperties sendEmail() {
        VerificationProperties properties = new VerificationProperties();
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "outlook.office365.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new javax.mail.PasswordAuthentication(UserName, Password);
                    }
                });

        try {
            randomCode();
            long expirationTime = System.currentTimeMillis() + EXPIRATION_TIME;
            //insert data into an object.
            properties.setVerificationCode(getCode());
            properties.setEXPIRATION_TIME(expirationTime);
            //Convert the expiration time to a readable date and time format.
            Date expirationDate = new Date(expirationTime);
            System.out.println(expirationDate);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String expirationTimeString = formatter.format(expirationDate);

            String text = "<html><body>" +
                    "<p>Thank you for registering with us!</p>" +
                    "<p>Your verification code is: <strong>" + this.Code + "</strong></p>" +
                    "<p>Please enter this code to verify your email address.</p>" +
                    "<ul>" +
                    "<li>The verification code is valid for 15 minutes.</li>" +
                    "<li>The verification code will expire at: <strong>" + expirationTimeString + "</strong></li>" +
                    "</ul>" +
                    "<p>Thank you for using our service!</p>" +
                    "</body></html>";

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(UserName));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.To));
            message.setSubject("EC - Emergency center");
            message.setContent(text, "text/html");

            //BodyPart messageBodyPart = new MimeBodyPart();
            //Multipart multipart = new MimeMultipart();
            //multipart.addBodyPart(messageBodyPart);
            //String file = "src/img/Netflix.png";
            //DataSource source = new FileDataSource(file);
            //messageBodyPart.setDataHandler(new DataHandler(source));
            //messageBodyPart.setFileName(file);
            //multipart.addBodyPart(messageBodyPart);
            // Send the complete message parts
            //message.setContent(multipart);
            Transport.send(message);
            System.out.println("Sent...");
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        }
        return properties;
    }

    /**
     * Generates a 6-digit random number
     */
    private void randomCode() {
        Random r = new Random();
        int code = r.nextInt(900000) + 100000;
        setCode(code);
    }

}
