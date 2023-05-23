/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AES_ENCRYPTION;

/**
 *
 * @author YairF
 */
import javax.crypto.KeyGenerator;
import javax.crypto.Cipher;
import java.security.Key;

public class TwoWayEncryption {
    public static void main(String[] args) throws Exception {
        // Generate a key
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128); // Use a key length of 128 bits (16 bytes)
        Key key = keyGenerator.generateKey();

        // Create a cipher
        Cipher cipher = Cipher.getInstance("AES");

        // Encrypt the data
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedData = cipher.doFinal("yair".getBytes());

        // Decrypt the data
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedData = cipher.doFinal(encryptedData);

        // Print the encrypted and decrypted data as strings
        System.out.println("Encrypted data: " + new String(encryptedData));
        System.out.println("Decrypted data: " + new String(decryptedData));
    }
}

