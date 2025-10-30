import com.xiaoke1256.thirdpay.sdk.encryption.aes.AESUtils;

import java.util.Base64;

public class AESUtilsTest {
    public static void main(String[] args) throws Exception {
        String plainText = "hello world "+System.currentTimeMillis();
        String key = AESUtils.generateAESKey();
        System.out.println("key:"+key);
        System.out.println("plainText:"+plainText);
        String encryptedText = AESUtils.encrypt(plainText, AESUtils.loadAESKey(key));
        System.out.println("encryptedText:"+encryptedText);
        String decryptedText = AESUtils.decrypt(encryptedText, AESUtils.loadAESKey(key));
        System.out.println("decryptedText:"+decryptedText);
    }
}
