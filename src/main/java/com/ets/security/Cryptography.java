package com.ets.security;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Yusuf
 * http://stackoverflow.com/questions/28622438/aes-256-password-based-encryption-decryption-in-java
 */
public class Cryptography {

    private static final String salt = "102010212";
    private static final char[] password = {'e', 'T', 's', '2', '!', '£', 'E', 't', 's', '2', '1', '5'};
    private static final int iterations = 65536;
    private static final int keySize = 128;

    public static String encryptString(String plaintext) {
        char[] chars = plaintext.toCharArray();
        return encryptString(chars);
    }

    public static String encryptString(char[] plaintext) {

        byte[] encryptedTextBytes = null;
        String ivString = null;
        try {

            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            PBEKeySpec spec = new PBEKeySpec(password, salt.getBytes(), iterations, keySize);
            SecretKey secretKey = skf.generateSecret(spec);
            SecretKeySpec secretSpec = new SecretKeySpec(secretKey.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretSpec);
            AlgorithmParameters params = cipher.getParameters();
            byte[] ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();

            encryptedTextBytes = cipher.doFinal(String.valueOf(plaintext).getBytes("UTF-8"));
            ivString = Arrays.toString(ivBytes);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | InvalidParameterSpecException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException ex) {
            Logger.getLogger(Cryptography.class.getName()).log(Level.SEVERE, null, ex);
        }

        return DatatypeConverter.printBase64Binary(encryptedTextBytes).concat("partition").concat(ivString);
    }

    
    public static String decryptString(String encryptedString) {

        byte[] decryptedTextBytes = null;

        String[] vals = encryptedString.split("partition");
        if(vals.length<2){
         return "";
        }
        char[] encryptedText = vals[0].toCharArray();

        String[] byteValues = vals[1].substring(1, vals[1].length() - 1).split(",");
        byte[] ivBytes = new byte[byteValues.length];

        for (int i = 0, len = ivBytes.length; i < len; i++) {
            ivBytes[i] = Byte.parseByte(byteValues[i].trim());
        }

        try {
            byte[] encryptedTextBytes = DatatypeConverter.parseBase64Binary(new String(encryptedText));

            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            PBEKeySpec spec = new PBEKeySpec(password, salt.getBytes(), iterations, keySize);
            SecretKey secretKey = skf.generateSecret(spec);
            SecretKeySpec secretSpec = new SecretKeySpec(secretKey.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretSpec, new IvParameterSpec(ivBytes));

            try {
                decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
            } catch (IllegalBlockSizeException | BadPaddingException e) {
            }

        } catch (ArrayIndexOutOfBoundsException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException ex) {
            Logger.getLogger(Cryptography.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new String(decryptedTextBytes);
    }

    public static String getSalt() throws Exception {

        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] saltbyte = new byte[20];
        sr.nextBytes(saltbyte);
        return new String(saltbyte);
    }

    public static void main(String[] args) throws Exception {
        String message = "1two34";
        System.out.println("Message: " + String.valueOf(message));

        String encs = encryptString(message);
        System.out.println("Encrypted: " + encs);
        System.out.println("Decrypted: " + decryptString("mzNat4xEfK4CUrEHrrk0Wg==partition[-54, -119, -121, 101, -75, 122, 38, 56, -54, 9, -81, -54, -101, -9, 51, 2]"));
    }
}
