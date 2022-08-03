/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uredjaj.korisnickiuredjaj;

import java.util.Base64;

/**
 *
 * @author user2
 */
public class AuthConverterHelper {
    public static String encode (String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }
    public static String getAuthString(String username, String password) {
        return "Basic " + encode(username + ":"  + password);
    }
}
