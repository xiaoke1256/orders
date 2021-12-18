package test.com.xiaoke1256.orders.store.intra.common.utils;

import com.xiaoke1256.orders.store.intra.common.utils.RSAUtil;
import org.junit.Test;

import java.security.KeyPair;

public class RSAUtilTest {

    @Test
    public void testDecode() throws Exception {
        KeyPair keyPair = RSAUtil.genKeyPair();
        String message = "admin13a686";
        String encryptMsg = RSAUtil.encrypt(message,keyPair.getPublic());
        System.out.println("encryptMsg:"+encryptMsg);
        String mw = RSAUtil.decrypt(encryptMsg,keyPair.getPrivate());
        System.out.println("mw:"+mw);
    }
}
