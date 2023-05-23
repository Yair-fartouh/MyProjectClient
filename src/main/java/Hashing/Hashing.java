/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Hashing;

import org.apache.commons.codec.digest.DigestUtils;

public class Hashing {
    public static void main(String[] args) {
        String sha256hex = DigestUtils.sha1Hex("yair");
        System.out.println(sha256hex);
    }
    
}
