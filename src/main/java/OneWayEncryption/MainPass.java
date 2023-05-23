package OneWayEncryption;

import java.security.NoSuchAlgorithmException;

public class MainPass {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        try {
            PasswordHashingExample hashingExample = new PasswordHashingExample();
            String password = "password123";
            String salt = hashingExample.generateSalt();
            String hashedPassword = hashingExample.hashPassword(password, salt);
            System.out.println("Hashed password: " + hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
    }
}