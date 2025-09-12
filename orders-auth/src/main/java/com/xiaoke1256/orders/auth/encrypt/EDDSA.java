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
        PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) pemParser.readObject();
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
        this.privateKey = converter.getPrivateKey(privateKeyInfo);

        // 从私钥派生公钥（ED25519特定方式）
        byte[] privateKeyBytes = privateKey.getEncoded();
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("Ed25519", "BC");
        this.privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 使用BouncyCastle的Ed25519实现来派生公钥
        this.privateKeyParams = new Ed25519PrivateKeyParameters(privateKey.getEncoded(), 0);
        this.publicKeyParams = privateKeyParams.generatePublicKey();

        // 将Ed25519PublicKeyParameters转换为Java的PublicKey对象
        byte[] publicKeyBytes = publicKeyParams.getEncoded();
        SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(publicKeyBytes);
        this.publicKey = converter.getPublicKey(subjectPublicKeyInfo);

        pemParser.close();
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
}