/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package main;

import locationAPI.DrivingDirections;
import locationAPI.Time;
import login_SignUp_GUI.LoginGUI;

import javax.swing.*;
import java.io.IOException;
import java.text.ParseException;

/**
 * @author YairF
 */
public class Main {

    public static void main(String[] args) throws IOException, InterruptedException, ParseException {
        DrivingDirections dd;
        dd = new DrivingDirections();
//        String originLAT = "32.054635";
//        String originLNG = "34.784554";
//        String destinationLAT = "31.254905";
//        String destinationLNG = "34.791210";
        String originLAT = "32.054062";
        String originLNG = "34.782215";
        String destinationLAT = "32.050222";
        String destinationLNG = "34.955168";

        //System.out.println(dd.GetDirections(originLAT, originLNG, destinationLAT, destinationLNG));
        //String s = "1 hr 10 min 66.2 miles";
        //String s = "1 hr 10 min";
        //Time t = dd.start(originLAT, originLNG, destinationLAT, destinationLNG);
        //System.out.println(t.getHour() + " ==> " + t.getMinute());

        //LoginToVerify l = new LoginToVerify("yairlimudim@gmail.com");
        //l.sendCodeInEmail();
        /**
         * java GUI
         */
        LoginGUI login = new LoginGUI(new JFrame());

    }
}
