package com.xiaoke1256.thirdpay.sdk.encryption.rsa;

import javax.crypto.Cipher;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

public class RSAUtils {
    public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = encryptCipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes); // 将加密后的字节转换为Base64编码的字符串
    }

    public static String decrypt(String encryptedText, PrivateKey privateKey) throws Exception {
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText); // 将Base64编码的字符串解码为字节数组
        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = decryptCipher.doFinal(decodedBytes);
        return new String(decryptedBytes); // 将解密后的字节转换为字符串
    }

    public static byte[] signData(byte[] data, PrivateKey privateKey) throws Exception {
        Signature signer = Signature.getInstance("SHA256withRSA");
        signer.initSign(privateKey);
        signer.update(data);
        return signer.sign();
    }

    public static boolean verifySignature(byte[] data, byte[] signature, PublicKey publicKey) throws Exception {
        Signature signer = Signature.getInstance("SHA256withRSA");
        signer.initVerify(publicKey);
        signer.update(data);
        return signer.verify(signature);
    }
}
