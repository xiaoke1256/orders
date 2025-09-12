package com.xiaoke1256.orders.auth.encrypt;

import org.junit.Test;
import static org.junit.Assert.*;

public class EDDSAKeyTest {

    @Test
    public void testKeyParsing() {
        try {
            // 创建EDDSA实例，这将触发密钥解析
            EDDSA eddsa = new EDDSA();
            
            // 如果没有抛出异常，说明密钥解析成功
            assertNotNull(eddsa.getPrivateKey());
            assertNotNull(eddsa.getPublicKey());
            
            System.out.println("密钥解析成功！");
            
            // 测试生成和验证token
            String content = "test content";
            String token = eddsa.token(content);
            assertNotNull("Token should not be null", token);
            
            boolean isValid = eddsa.verify(token);
            assertTrue("Token should be valid", isValid);
            
            String retrievedContent = eddsa.getContent(token);
            assertEquals("Content should match", content, retrievedContent);
            
            System.out.println("JWT生成和验证成功！");
        } catch (Exception e) {
            e.printStackTrace();
            fail("密钥解析失败: " + e.getMessage());
        }
    }
}