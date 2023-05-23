package OneWayEncryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordHashingExample {

    //TODO: למחוק את המחלקה הזאת אחרי שהתצוגה עובדת
    /**
     * This function adds a layer of security before the password is hashed.
     * To make it difficult for the attackers who have an encryption solution for hashed passwords only.
     *
     * @return
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * The function takes a password and a salt as input,
     * and returns the hash of the password using the SHA-256 hash function with the salt.
     *
     * @param password - Customer password
     * @param salt     - Make the password hash more secure
     *                 by adding a random and unique value to the password before it is hashed.
     * @return - Password hash
     * @throws NoSuchAlgorithmException
     */
    public static String hashPassword(String password, String salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(Base64.getDecoder().decode(salt));
        byte[] hashedPassword = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedPassword);
    }
}
