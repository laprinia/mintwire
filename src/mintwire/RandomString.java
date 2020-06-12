/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mintwire;

import java.util.Random;

public class RandomString {

    public static final String SOURCES
            = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    public static String generatePassphrase() {
        RandomString rs = new RandomString();

        return rs.generateString(new Random(), SOURCES, 6);
    }

    private String generateString(Random random, String characters, int length) {
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(random.nextInt(characters.length()));
        }
        return new String(text);
    }
}
