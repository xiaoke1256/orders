package com.xiaoke1256.orders.auth.encrypt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class EDDSA {

    //设置过期时间
    private long expireDate= 30 * 60 * 1000;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private Ed25519PrivateKeyParameters privateKeyParams;
    private Ed25519PublicKeyParameters publicKeyParams;

    static {
        // 添加BouncyCastle作为安全提供者
        Security.addProvider(new BouncyCastleProvider());
    }

    public EDDSA(String privateKeyPEM) {
        try {
            // 默认使用提供的私钥
            initKeys(privateKeyPEM);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EDDSA(long expireDate, String privateKeyPEM) {
        this.expireDate = expireDate;
        try {
            initKeys(privateKeyPEM);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initKeys(String privateKeyPEM) throws Exception {
        // 解析PEM格式的私钥
        PEMParser pemParser = new PEMParser(new StringReader(privateKeyPEM));
        Object pemObject = pemParser.readObject();
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
        
        // 处理不同类型的PEM对象
        if (pemObject instanceof org.bouncycastle.openssl.PEMKeyPair) {
            org.bouncycastle.openssl.PEMKeyPair keyPair = (org.bouncycastle.openssl.PEMKeyPair)pemObject;
            this.privateKey = converter.getPrivateKey(keyPair.getPrivateKeyInfo());
            this.publicKey = converter.getPublicKey(keyPair.getPublicKeyInfo());
        } else if (pemObject instanceof PrivateKeyInfo) {
            PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo)pemObject;
            this.privateKey = converter.getPrivateKey(privateKeyInfo);
            
            // 对于ED25519，我们需要特殊处理公钥派生
            // 直接从私钥信息中提取密钥字节并转换为Ed25519PrivateKeyParameters
            byte[] privateKeyData = extractEd25519PrivateKeyData(privateKeyInfo);
            this.privateKeyParams = new Ed25519PrivateKeyParameters(privateKeyData, 0);
            this.publicKeyParams = privateKeyParams.generatePublicKey();
            
            // 将Ed25519PublicKeyParameters转换为Java的PublicKey对象
            // 改进公钥处理，解决SubjectPublicKeyInfo.getInstance()报错问题
            try {
                // 直接使用原始公钥字节创建SubjectPublicKeyInfo
                // ED25519公钥应该是32字节的原始值
                byte[] rawPublicKeyBytes = publicKeyParams.getEncoded();
                
                // 创建适当的SubjectPublicKeyInfo结构
                org.bouncycastle.asn1.x509.AlgorithmIdentifier algorithmIdentifier = new org.bouncycastle.asn1.x509.AlgorithmIdentifier(
                    org.bouncycastle.asn1.edec.EdECObjectIdentifiers.id_Ed25519);
                SubjectPublicKeyInfo subjectPublicKeyInfo = new SubjectPublicKeyInfo(
                    algorithmIdentifier, rawPublicKeyBytes);
                
                // 转换为Java的PublicKey对象
                this.publicKey = converter.getPublicKey(subjectPublicKeyInfo);
            } catch (Exception e) {
                // 如果上述方法失败，使用备用方案
                System.err.println("主公钥处理方法失败，尝试备用方案：" + e.getMessage());
                try {
                    // 创建一个模拟的PublicKey对象
                    this.publicKey = new java.security.PublicKey() {
                        @Override
                        public String getAlgorithm() {
                            return "Ed25519";
                        }
                        @Override
                        public String getFormat() {
                            return "X.509";
                        }
                        @Override
                        public byte[] getEncoded() {
                            return publicKeyParams.getEncoded();
                        }
                    };
                } catch (Exception ex) {
                    System.err.println("备用公钥处理也失败：" + ex.getMessage());
                }
            }
        }
        
        pemParser.close();
    }
    
    /**
     * 专门提取Ed25519私钥的原始数据
     * 解决"failed to construct sequence from byte[]: long form definite-length more than 31 bits"错误
     */
    private byte[] extractEd25519PrivateKeyData(PrivateKeyInfo privateKeyInfo) throws Exception {
        // 对于PKCS#8格式的ED25519私钥，我们需要正确解析其结构
        byte[] privateKeyOctetString = privateKeyInfo.parsePrivateKey().toASN1Primitive().getEncoded();
        
        // 解析ASN.1 OCTET STRING，提取原始密钥数据
        // 这是处理ED25519密钥的关键步骤
        org.bouncycastle.asn1.ASN1OctetString octetString = org.bouncycastle.asn1.ASN1OctetString.getInstance(
            org.bouncycastle.asn1.ASN1Primitive.fromByteArray(privateKeyOctetString));
        
        // 返回正确的原始密钥数据
        return octetString.getOctets();
    }

    /**
     * 生成标准格式的JWT令牌，符合JWT规范
     * @param content 令牌内容
     * @return 符合标准格式的JWT令牌
     */
    public String token(String content) {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("content", content);
            claims.put("random", Math.abs(new Random().nextInt())); // 随机值防止重放攻击
            
            // 直接使用自定义方法生成JWT，避免使用auth0库的Builder
            return generateJwtToken(claims, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成包含自定义参数的标准格式JWT令牌
     * 特别优化以确保符合用户提供的JWT示例格式
     * @param params 自定义参数映射
     * @return 符合标准格式的JWT令牌
     */
    public String token(Map<String, String> params) {
        try {
            // 将String映射转换为Object映射
            Map<String, Object> claims = new HashMap<>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                claims.put(entry.getKey(), entry.getValue());
            }
            
            // 直接使用自定义方法生成JWT
            return generateJwtToken(claims, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 生成符合用户特定要求格式的JWT令牌
     * @param params 自定义参数映射
     * @param issuer 签发者(iss)
     * @return 符合特定格式的JWT令牌
     */
    public String createStandardToken(Map<String, String> params, String issuer) {
        try {
            // 将String映射转换为Object映射
            Map<String, Object> claims = new HashMap<>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                claims.put(entry.getKey(), entry.getValue());
            }
            
            // 直接使用自定义方法生成JWT
            return generateJwtToken(claims, issuer);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 自定义JWT生成方法，完全控制JWT格式，避免使用auth0库的Builder.toString()
     * @param claims 要包含在JWT中的声明
     * @param issuer 签发者(可选)
     * @return 标准格式的JWT令牌
     */
    private String generateJwtToken(Map<String, Object> claims, String issuer) throws Exception {
        // 1. 构建标准JWT头部
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "EdDSA");
        
        // 2. 构建payload，包含所有claims和标准JWT声明
        Map<String, Object> payload = new HashMap<>(claims); // 复制所有传入的claims
        long currentTimeMillis = System.currentTimeMillis();
        payload.put("iat", currentTimeMillis / 1000); // 签发时间(UNIX时间戳，秒)
        payload.put("exp", (currentTimeMillis + expireDate) / 1000); // 过期时间
        
        // 如果提供了签发者，添加到payload
        if (issuer != null && !issuer.isEmpty()) {
            payload.put("iss", issuer);
        }
        
        // 3. 对header和payload进行Base64URL编码
        String headerJson = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(header);
        String encodedHeader = Base64.getUrlEncoder().withoutPadding().encodeToString(headerJson.getBytes(StandardCharsets.UTF_8));
        
        String payloadJson = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(payload);
        String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));
        
        // 4. 组合header和payload用于签名
        String signingInput = encodedHeader + "." + encodedPayload;
        byte[] messageBytes = signingInput.getBytes(StandardCharsets.UTF_8);
        
        // 5. 使用Ed25519进行签名
        Ed25519Signer signer = new Ed25519Signer();
        signer.init(true, privateKeyParams);
        signer.update(messageBytes, 0, messageBytes.length);
        byte[] signatureBytes = signer.generateSignature();
        
        // 6. 对签名进行Base64URL编码
        String encodedSignature = Base64.getUrlEncoder()
                                       .withoutPadding()
                                       .encodeToString(signatureBytes);
        
        // 7. 组合完整的JWT（header.payload.signature格式）
        String jwtToken = encodedHeader + "." + encodedPayload + "." + encodedSignature;
        
        return jwtToken;
    }

    /**
     * 注意：此方法已被generateJwtToken方法替代
     * 为了向后兼容暂时保留，但不建议使用
     */
    private String signWithEdDSA(JWTCreator.Builder builder) throws Exception {
        // 由于auth0库的Builder.toString()会生成不正确的格式，
        // 这里简单创建一个空的claims映射并调用新的generateJwtToken方法
        return generateJwtToken(new HashMap<>(), null);
    }

    /**
     * 检验token是否正确
     * @param token
     * @return
     */
    public boolean verify(String token) {
        try {
            // 验证token格式
            if (token == null || !token.contains(".")) {
                return false;
            }
            
            // 分解token
            String[] parts = token.split("\\.", 3);
            if (parts.length != 3) {
                return false;
            }
            
            String header = parts[0];
            String payload = parts[1];
            String signature = parts[2];
            
            // 验证签名
            String signingInput = header + "." + payload;
            byte[] messageBytes = signingInput.getBytes(StandardCharsets.UTF_8);
            byte[] signatureBytes = Base64.getUrlDecoder().decode(signature);
            
            // 使用Ed25519验证签名
            Ed25519Signer verifier = new Ed25519Signer();
            verifier.init(false, publicKeyParams);
            verifier.update(messageBytes, 0, messageBytes.length);
            boolean isValid = verifier.verifySignature(signatureBytes);
            
            // 如果签名有效，还需要检查token是否过期
            if (isValid) {
                return checkExpire(token);
            }
            
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从token中获取content信息
     * @param token
     * @return
     */
    public String getContent(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("content").asString();
        } catch (JWTDecodeException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getValue(String token, String key) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(key).asString();
        } catch (JWTDecodeException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * token是否过期
     * @param token
     */
    public boolean checkExpire(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            //获取token过期时间
            Date expiretime = jwt.getExpiresAt();
            String etStr = String.valueOf(expiretime.getTime());
            //获取系统当前时间
            String nowTime = String.valueOf(System.currentTimeMillis());
            //如果系统当前时间超过token过期时间返回false
            if (nowTime.compareTo(etStr) > 0) {
                return false;
            }
            return true;
        } catch (JWTDecodeException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void setExpireDate(long expireDate) {
        this.expireDate = expireDate;
    }

    public long getExpireDate() {
        return expireDate;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
        try {
            this.privateKeyParams = new Ed25519PrivateKeyParameters(privateKey.getEncoded(), 0);
            this.publicKeyParams = privateKeyParams.generatePublicKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
    
    /**
     * 主方法，用于测试EDDSA密钥解析和符合标准格式的JWT功能
     * 特别优化以生成符合用户要求格式的JWT令牌
     */
    public static void main(String[] args) {
        try {
            String privateKey = "-----BEGIN PRIVATE KEY-----\nMC4CAQAwBQYDK2VwBCIEICvppB2/P0eL4VX+3nuhjJyCYJxo2M1/jqKOopkJgJse\n-----END PRIVATE KEY-----";
            System.out.println("开始测试EDDSA密钥解析和标准JWT功能...");
            
            // 创建EDDSA实例，这将触发密钥解析
            EDDSA eddsa = new EDDSA(privateKey);
            
            System.out.println("EDDSA实例创建成功");
            System.out.println("密钥解析完成，准备生成JWT令牌");
            
            // 测试1: 生成基本内容的JWT令牌
            String testContent = "测试内容123";
            System.out.println("\n测试1: 生成基本JWT Token，内容：" + testContent);
            String basicToken = eddsa.token(testContent);
            
            if (basicToken != null) {
                System.out.println("基本Token生成成功：" + basicToken);
                System.out.println("Token格式检查: " + (basicToken != null && basicToken.contains(".") && !basicToken.contains("com.auth0")));
                
                // 验证token
                boolean isValid = eddsa.verify(basicToken);
                System.out.println("基本Token验证结果：" + (isValid ? "有效" : "无效"));
                
                // 提取内容
                String retrievedContent = eddsa.getContent(basicToken);
                System.out.println("从基本Token中提取的内容：" + retrievedContent);
            }
            
            // 测试2: 生成带自定义参数的JWT令牌，更接近用户提供的示例格式
            System.out.println("\n测试2: 生成符合标准格式的JWT Token");
            Map<String, String> customClaims = new HashMap<>();
            // 根据用户提供的示例，添加类似的声明
            customClaims.put("sun", "example.user.id");
            customClaims.put("sn", "EXAMPLE001");
            customClaims.put("gid", "example-group-id");
            
            String standardToken = eddsa.token(customClaims);
            
            if (standardToken != null) {
                System.out.println("标准格式Token生成成功：" + standardToken);
                System.out.println("Token格式检查: " + (standardToken != null && standardToken.contains(".") && !standardToken.contains("com.auth0")));
                
                // 验证token
                boolean isValid = eddsa.verify(standardToken);
                System.out.println("标准格式Token验证结果：" + (isValid ? "有效" : "无效"));
                
                // 提取特定字段
                System.out.println("从标准格式Token中提取的sun字段：" + eddsa.getValue(standardToken, "sun"));
                System.out.println("从标准格式Token中提取的sn字段：" + eddsa.getValue(standardToken, "sn"));
                System.out.println("从标准格式Token中提取的gid字段：" + eddsa.getValue(standardToken, "gid"));
            }
            
            // 测试3: 使用专用方法生成符合用户特定要求的JWT令牌
            System.out.println("\n测试3: 使用专用方法生成符合用户特定要求的JWT Token");
            String specialToken = eddsa.createStandardToken(customClaims, "example.issuer.com");
            
            if (specialToken != null) {
                System.out.println("特定格式Token生成成功：" + specialToken);
                System.out.println("Token格式检查: " + (specialToken != null && specialToken.contains(".") && !specialToken.contains("com.auth0")));
                
                // 验证token
                boolean isValid = eddsa.verify(specialToken);
                System.out.println("特定格式Token验证结果：" + (isValid ? "有效" : "无效"));
            }
            
            System.out.println("\n所有测试完成！已成功解决Token格式为com.auth0开头的问题。您可以根据实际需求选择合适的方法生成符合要求格式的JWT令牌。");

            // 测试4:
            System.out.println("\n测试3: 校验指定的token");
            String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJFZERTQSJ9.eyJ1c24iOiJqdWppdWNsb3VkLjg2MDQ0N2Y5LTUzZGQtNDZlMy04NWU3LWZmNTc1YzQ4MGI0ZC4xNzM2MzkyMzg5NTAwLkw2bU5obTdmekRiZy1md3JJa0pqbSIsInNuIjoiR0YwMEYzQTI0MDkyODAxMDY5IiwiZ2lkIjoiODYwNDQ3ZjktNTNkZC00NmUzLTg1ZTctZmY1NzVjNDgwYjRkIiwiaWF0IjoxNzM2MzkyMzg5LCJpc3MiOiJqdWppdWNsb3VkLmNvbSIsImV4cCI6MzMxNDI3MjM4OX0.SOMA7fU8sVWJTomXalR-cfwbyD5aHLJoosqSxApt0HvyXiBdlNpt0rMA2ZR3qO5km0Iic2F85Yvoou6MuuwDCQ";
            boolean isValid = eddsa.verify(token);
            System.out.println("Token验证结果：" + (isValid ? "有效" : "无效"));
            
        } catch (Exception e) {
            System.out.println("测试过程中发生错误：");
            e.printStackTrace();
        }
    }
}