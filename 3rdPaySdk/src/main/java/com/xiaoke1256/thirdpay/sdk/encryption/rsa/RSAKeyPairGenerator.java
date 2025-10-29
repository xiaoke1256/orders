package com.xiaoke1256.thirdpay.sdk.encryption.rsa;

import java.io.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAKeyPairGenerator {
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(3072);
        return keyGen.generateKeyPair();
    }

    public static void writePrivateKeyToFile(PrivateKey privateKey, String filename) throws IOException {
        String privateKeyPEM = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        String pkcs8Header = "-----BEGIN PRIVATE KEY-----\n";
        String pkcs8Footer = "\n-----END PRIVATE KEY-----";
        String privateKeyString = pkcs8Header + privateKeyPEM + pkcs8Footer;
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(privateKeyString.getBytes());
        }
    }

    public static void writePublicKeyToFile(PublicKey publicKey, String filename) throws IOException {
        String publicKeyPEM = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String x509Header = "-----BEGIN PUBLIC KEY-----\n";
        String x509Footer = "\n-----END PUBLIC KEY-----";
        String publicKeyString = x509Header + publicKeyPEM + x509Footer;
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(publicKeyString.getBytes());
        }
    }

    /**
     * 从 PEM 格式的私钥字符串还原 PrivateKey 对象
     * @param privateKeyPem PEM 格式的私钥字符串
     * @return PrivateKey 私钥对象
     * @throws Exception 如果私钥格式不正确或处理过程中出现错误
     */
    public static PrivateKey loadPrivateKey(String privateKeyPem) throws Exception {
        // 清理 PEM 格式的页眉页脚和空白字符
        String cleanedKey = privateKeyPem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        // Base64 解码
        byte[] keyBytes = Base64.getDecoder().decode(cleanedKey);

        // 创建 PKCS8EncodedKeySpec
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

        // 生成私钥对象
        // 首先尝试 RSA 算法
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            // 如果 RSA 失败，尝试其他常见算法
            try {
                KeyFactory keyFactory = KeyFactory.getInstance("EC");
                return keyFactory.generatePrivate(keySpec);
            } catch (Exception e2) {
                // 最后尝试 DSA
                KeyFactory keyFactory = KeyFactory.getInstance("DSA");
                return keyFactory.generatePrivate(keySpec);
            }
        }
    }

    /**
     * 从PEM格式字符串还原公钥
     * @param publicKeyPem PEM格式的公钥字符串
     * @return PublicKey 公钥对象
     * @throws Exception 如果公钥格式不正确或处理过程中出现错误
     */
    public static PublicKey loadPublicKey(String publicKeyPem) throws Exception {
        // 清理PEM格式的页眉页脚和空白字符
        String cleanedKey = publicKeyPem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", ""); // 移除所有空白字符

        // Base64解码
        byte[] keyBytes = Base64.getDecoder().decode(cleanedKey);

        // 创建X509EncodedKeySpec
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

        // 生成公钥对象，首先尝试RSA算法
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            // 如果RSA失败，尝试其他常见算法
            try {
                KeyFactory keyFactory = KeyFactory.getInstance("EC");
                return keyFactory.generatePublic(keySpec);
            } catch (Exception e2) {
                // 最后尝试DSA
                KeyFactory keyFactory = KeyFactory.getInstance("DSA");
                return keyFactory.generatePublic(keySpec);
            }
        }
    }

    public static PrivateKey loadPrivateKeyFromFile(String privateKeyFileName) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(privateKeyFileName);
            InputStreamReader isr = new InputStreamReader(fis ); // 指定字符编码
            BufferedReader br = new BufferedReader(isr)) {

            String line;
            while ((line = br.readLine()) != null) {
                if(sb.length()>0){
                    sb.append("\n");
                }
                sb.append(line);
            }
        }
        return loadPrivateKey(sb.toString());
    }

    public static PublicKey loadPublicKeyFromFile(String publicKeyFileName) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(publicKeyFileName);
             InputStreamReader isr = new InputStreamReader(fis ); // 指定字符编码
             BufferedReader br = new BufferedReader(isr)) {

            String line;
            while ((line = br.readLine()) != null) {
                if(sb.length()>0){
                    sb.append("\n");
                }
                sb.append(line);
            }
        }
        return loadPublicKey(sb.toString());
    }

    public static PublicKey loadPublicKeyFromSt(InputStream publicKeyFileStream) throws Exception {
        StringBuilder sb = new StringBuilder();
        InputStreamReader isr = new InputStreamReader(publicKeyFileStream ); // 指定字符编码
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            if(sb.length()>0){
                sb.append("\n");
            }
            sb.append(line);
        }
        return loadPublicKey(sb.toString());
    }

}
