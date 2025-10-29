import com.xiaoke1256.thirdpay.sdk.encryption.rsa.RSAKeyPairGenerator;

import java.security.KeyPair;

public class TestRSAKeyPairGenerator {

    public static void main(String[] args) throws Exception {
        KeyPair keyPair = RSAKeyPairGenerator.generateKeyPair();
        System.out.println("Public Key: " + keyPair.getPublic());
        System.out.println("Private Key: " + keyPair.getPrivate());
    }
}
