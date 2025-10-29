import com.xiaoke1256.thirdpay.sdk.encryption.rsa.RSAKeyPairGenerator;
import com.xiaoke1256.thirdpay.sdk.encryption.rsa.RSAUtils;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class TestRSAUtils {

    private PublicKey publicKey;
    private PrivateKey privateKey;

    @Before
    public void init() throws Exception {
        privateKey = RSAKeyPairGenerator.loadPrivateKeyFromFile("D:\\tmp\\private_key_base64.pem");
        System.out.println("私钥已获取");
        System.out.println("算法: " + privateKey.getAlgorithm());
        System.out.println("格式: " + privateKey.getFormat());
        System.out.println("密钥长度: " + privateKey.getEncoded().length);

        publicKey = RSAKeyPairGenerator.loadPublicKeyFromFile("D:\\tmp\\public_key_base64.pem");
        System.out.println("公钥已获取");
        System.out.println("算法: " + publicKey.getAlgorithm());
        System.out.println("格式: " + publicKey.getFormat());
        System.out.println("密钥长度: " + publicKey.getEncoded().length);
    }

    @Test
    public void testEncryptAndDecrypt() throws Exception {
        String message = "Hello, World! "+ System.currentTimeMillis();
        System.out.println("原始消息: " + message);
        String encrypted = RSAUtils.encrypt(message, publicKey);

        System.out.println("公钥加密后: " + encrypted);
        String decoded = RSAUtils.decrypt(encrypted, privateKey);
        System.out.println("私钥解密后: " + decoded);
        assert message.equals(decoded);
    }

    @Test
    public void testSignAndVerify() throws Exception {
        String message = "Hello, World! "+ System.currentTimeMillis();
        System.out.println("原始消息: " + message);
        byte[] signature = RSAUtils.signData(message.getBytes(), privateKey);
        System.out.println("签名: " + Base64.getEncoder().encodeToString(signature));
        boolean isValid = RSAUtils.verifySignature(message.getBytes(), signature, publicKey);
        System.out.println("签名验证结果: " + isValid);
        assert isValid;
    }
}
