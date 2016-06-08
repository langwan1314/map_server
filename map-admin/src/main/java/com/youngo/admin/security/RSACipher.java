/**
 * @Title: RSACipher.java
 * @Package com.yjh.admin.security
 * @Description: RSA 加密解密
 * @author yiyan
 * @date 2015-1-26 下午5:19:17
 * @version
 */
package com.youngo.admin.security;

import com.youngo.core.common.Base64;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * 类名称：RSACipher
 * 类描述：
 * 创建人：yiyan
 * 创建时间：2015-1-26 下午5:19:17
 * 修改人：yiyan
 * 修改时间：2015-1-26 下午5:19:17
 * 修改备注：
 * 注意事项，如果密文以Http Get 的方式传递时，要做 URLEncoder
 * 示例：
 * String newUrl = urlStr + "&key=" + URLEncoder.encode(
 * "ABfJ1fCah/RhIEbibJ5MQex0DeYH2u3MvGT37VrcngwOZjK+5M2gHaFQX0kYwI2/6zG0JumZ/cGC1yLaKelxUZYuqw78RnJy5zRZxLMF/92z61NUjNuefFQ1lG0a3p7G1mCbQkmid0ANTSP504+7TqiuznFVMri80POhtzHlgB4="
 * , "UTF-8");
 * 
 * @version
 */
public class RSACipher
{

    /**
     * 公钥加密
     * 
     * @param rawText 原文
     * @param public_key 公钥
     * @param encoding 编码格式
     * @return 加密后的密文
     * @throws Exception
     */
    public static String encrypt(String rawText, String public_key, String encoding) throws Exception
    {

        PublicKey pubkey = getPublicKey(public_key);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubkey);

        return Base64.encode(cipher.doFinal(rawText.getBytes(encoding)));
    }

    /**
     * 私钥解密
     * 
     * @param cipherText 密文
     * @param private_key 信任的客户端私钥
     * @param encoding 编码格式
     * @return 解密后的字符串
     */
    public static String decrypt(String cipherText, String private_key, String encoding) throws Exception
    {
        PrivateKey prikey = getPrivateKey(private_key);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, prikey);

        return new String(cipher.doFinal(Base64.decode(cipherText)), encoding);
    }

    /**
     * 获得公钥
     * 
     * @param public_key
     * @return
     * @throws Exception
     */
    private static PublicKey getPublicKey(String public_key) throws Exception
    {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] encodedKey = Base64.decode(public_key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);

        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        return publicKey;
    }

    /**
     * 获得私钥
     * 
     * @param key 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String key) throws Exception
    {

        byte[] keyBytes;

        keyBytes = Base64.decode(key);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        return privateKey;
    }

}
