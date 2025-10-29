import com.xiaoke1256.thirdpay.sdk.encryption.rsa.RSAKeyPairGenerator;

import java.security.KeyPair;

public class TestRSAKeyPairGenerator {

    public static void main(String[] args) throws Exception {
        KeyPair keyPair = RSAKeyPairGenerator.generateKeyPair();
        System.out.println("Public Key: " + keyPair.getPublic());
        System.out.println("Private Key: " + keyPair.getPrivate());
        RSAKeyPairGenerator.writePrivateKeyToFile(keyPair.getPrivate(), "D:\\tmp\\private_key.pem");
        RSAKeyPairGenerator.writePublicKeyToFile(keyPair.getPublic(), "D:\\tmp\\public_key.pem");
        System.out.println("Private Key 已保存到 D:\\tmp\\private_key.pem");
        System.out.println("Public Key 已保存到 D:\\tmp\\public_key.pem");
    }
}
