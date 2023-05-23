package login_SignUp_GUI;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.regex.Pattern;

abstract class AuthenticationChecker {

    /**
     * - The email address must begin with one or more alphanumeric characters, underscores, plus signs, dashes or asterisks.
     * - The local part of the email address (before the @ symbol) can contain periods, underscores, plus signs, dashes, or asterisks, as long as they are followed by one or more alphanumeric characters.
     * - The domain part of the email address (after the @ sign) must be a valid domain name, consisting of one or more alphanumeric characters, dashes or dots.
     *
     * @param email
     * @return - true or false
     */
    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        /*
         * The Pattern class is part of the Java regular expression API and is used to compile regular expressions into
         *  patterns that can be used to match input strings. In this function, the Pattern.compile method is used
         *  to compile the regular expression stored in emailRegex into a Pattern object. The matcher method of the
         *  Pattern class is then used to create a Matcher object that can be used to match the input string against
         *  the pattern. Finally, the matches() method of the Matcher class is used to check whether the input string
         * matches the pattern or not, and the function returns a Boolean value indicating whether the input string
         *  is a valid email address or not.
         * */
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    /**
     * - The password must be at least 8 characters long.
     * - The password must contain at least one capital letter.
     * - The password must contain at least one lowercase letter.
     * - The password must contain at least one digit.
     * - The password must contain at least one special character (such as !@#$%^&*()-+=[]{}|\;:'",.<>/?).
     *
     * @param password
     * @return - true or false
     */
    public boolean isValidPassword(String password) {
        // Check if password is at least 8 characters long
        if (password.length() < 8) {
            return false;
        }

        // Check if password contains at least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }

        // Check if password contains at least one lowercase letter
        if (!password.matches(".*[a-z].*")) {
            return false;
        }

        // Check if password contains at least one digit
        if (!password.matches(".*\\d.*")) {
            return false;
        }

        // Check if password contains at least one special character
        if (!password.matches(".*[!@#$%^&*()-+=\\[\\]{}|\\\\;:'\",.<>/?].*")) {
            return false;
        }

        // If all checks pass, return true
        return true;
    }

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
