package org.library;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;


public class Encryption {

    public Encryption() throws Exception {
    }

    private final SecretKey key = generateKey(128);

    private final String algorithm = "AES/ECB/PKCS5Padding";


    private SecretKey generateKey (int n) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(n);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }



    private String encrypt(String input) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }

    private String decrypt(String cipherText) throws Exception{
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));

        return new String(plainText);
    }

    public String doDecrypt(String data) throws Exception{
        return decrypt(data);
    }
    public String doEncrypt(String data) throws Exception {
        return encrypt(data);
    }
}
