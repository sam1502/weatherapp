package com.ps.weatherapp.configs;

import com.ps.weatherapp.exceptions.WeatherServiceException;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

@Log4j2
public class CipherUtils {
    static Cipher cipher;
    private static SecretKeySpec secretKey;

    static {
        try {
            secretKey = createSecretKey("testKey".toCharArray(), "12345678".getBytes(StandardCharsets.UTF_8));
            cipher = Cipher.getInstance("AES/GCM/NoPadding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException e) {
            log.error("Exception caught while creating key");
        }
    }

    public static SecretKeySpec createSecretKey(char[] password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        PBEKeySpec keySpec = new PBEKeySpec(password, salt, 1000, 128);
        SecretKey keyTmp = keyFactory.generateSecret(keySpec);
        return new SecretKeySpec(keyTmp.getEncoded(), "AES");
    }

    public static String encrypt(String plainText)
            throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        AlgorithmParameters parameters = cipher.getParameters();
        GCMParameterSpec gcmParameterSpec = parameters.getParameterSpec(GCMParameterSpec.class);
        byte[] encryptedByte = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        byte[] iv = gcmParameterSpec.getIV();
        return base64Encode(iv) + ":" + base64Encode(encryptedByte);
    }

    private static String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String decrypt(String encryptedText) {
        try {
            String iv = encryptedText.split(":")[0];
            String property = encryptedText.split(":")[1];
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(128, base64Decode(iv)));
            return new String(cipher.doFinal(base64Decode(property)), StandardCharsets.UTF_8);
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException
                | InvalidAlgorithmParameterException e) {
            log.error("Exception caught while decrypting key : ", e);
            throw new WeatherServiceException("Exception caught while decrypting key");
        }
    }

    private static byte[]  base64Decode(String property) {
        return Base64.getDecoder().decode(property);
    }
}
