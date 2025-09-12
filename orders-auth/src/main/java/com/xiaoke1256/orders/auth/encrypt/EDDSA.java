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
    private long expireDate;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private Ed25519PrivateKeyParameters privateKeyParams;
    private Ed25519PublicKeyParameters publicKeyParams;

    static {
        // 添加BouncyCastle作为安全提供者
        Security.addProvider(new BouncyCastleProvider());
    }

    {
        expireDate = 30 * 60 * 1000;
    }

    public EDDSA() {
        try {
            // 默认使用提供的私钥
            String privateKeyPEM = "-----BEGIN PRIVATE KEY-----\nMC4CAQAwBQYDK2VwBCIEICvppB2/P0eL4VX+3nuhjJyCYJxo2M1/jqKOopkJgJse\n-----END PRIVATE KEY-----";
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

    public String token(String content) {
        try {
            //过期时间
            Date date = new Date(System.currentTimeMillis() + expireDate);
            //设置头部信息
            Map<String, Object> header = new HashMap<>();
            header.put("typ", "JWT");
            header.put("alg", "EdDSA");
            
            // 创建JWT构建器
            JWTCreator.Builder builder = JWT.create()
                    .withHeader(header)
                    .withClaim("content", content)
                    .withClaim("random", Math.abs(new Random().nextInt()))
                    .withIssuedAt(new Date())
                    .withExpiresAt(date);
            
            // 使用EDDSA签名生成token
            String token = signWithEdDSA(builder);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String token(Map<String, String> params) {
        try {
            //过期时间
            Date date = new Date(System.currentTimeMillis() + expireDate);
            //设置头部信息
            Map<String, Object> header = new HashMap<>();
            header.put("typ", "JWT");
            header.put("alg", "EdDSA");
            
            // 创建JWT构建器
            JWTCreator.Builder builder = JWT.create()
                    .withHeader(header);
            
            // 添加自定义参数
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.withClaim(entry.getKey(), entry.getValue());
            }
            
            // 添加标准参数
            builder.withClaim("random", Math.abs(new Random().nextInt()))
                   .withIssuedAt(new Date())
                   .withExpiresAt(date);
            
            // 使用EDDSA签名生成token
            String token = signWithEdDSA(builder);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String signWithEdDSA(JWTCreator.Builder builder) throws Exception {
        try {
            // 注意：由于java-jwt 3.10.3版本可能不直接支持EDDSA算法，
            // 这里使用自定义的方式来创建和签名JWT
            
            // 构建未签名的JWT
            String unsignedToken = builder.toString();
            
            // 提取header和payload部分
            String[] parts = unsignedToken.split("\\.", 3);
            if (parts.length < 2) {
                throw new Exception("Invalid JWT format");
            }
            
            // 组合header和payload用于签名
            String signingInput = parts[0] + "." + parts[1];
            byte[] messageBytes = signingInput.getBytes(StandardCharsets.UTF_8);
            
            // 使用Ed25519进行签名
            Ed25519Signer signer = new Ed25519Signer();
            signer.init(true, privateKeyParams);
            signer.update(messageBytes, 0, messageBytes.length);
            byte[] signatureBytes = signer.generateSignature();
            
            // 对签名进行Base64URL编码
            String signature = Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);
            
            // 组合完整的JWT
            return signingInput + "." + signature;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Failed to sign JWT with EdDSA: " + e.getMessage());
        }
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
     * 主方法，用于简单测试EDDSA密钥解析和JWT功能
     */
    public static void main(String[] args) {
        try {
            System.out.println("开始测试EDDSA密钥解析和JWT功能...");
            
            // 创建EDDSA实例，这将触发密钥解析
            EDDSA eddsa = new EDDSA();
            
            System.out.println("EDDSA实例创建成功");
            System.out.println("私钥：" + eddsa.getPrivateKey());
            System.out.println("公钥：" + eddsa.getPublicKey());
            
            // 测试生成和验证token
            String testContent = "测试内容123";
            System.out.println("\n生成JWT Token，内容：" + testContent);
            String token = eddsa.token(testContent);
            
            if (token != null) {
                System.out.println("Token生成成功：" + token);
                
                // 验证token
                boolean isValid = eddsa.verify(token);
                System.out.println("Token验证结果：" + (isValid ? "有效" : "无效"));
                
                // 提取内容
                String retrievedContent = eddsa.getContent(token);
                System.out.println("从Token中提取的内容：" + retrievedContent);
                
                // 验证提取的内容是否正确
                System.out.println("内容验证结果：" + (testContent.equals(retrievedContent) ? "正确" : "错误"));
            } else {
                System.out.println("Token生成失败！");
            }
            
        } catch (Exception e) {
            System.out.println("测试过程中发生错误：");
            e.printStackTrace();
        }
    }
}