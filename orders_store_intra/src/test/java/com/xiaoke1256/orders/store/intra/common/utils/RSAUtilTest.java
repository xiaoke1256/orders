package com.xiaoke1256.orders.store.intra.common.utils;

import org.junit.Test;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

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
