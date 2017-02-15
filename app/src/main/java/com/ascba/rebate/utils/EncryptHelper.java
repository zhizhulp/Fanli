package com.ascba.rebate.utils;

import android.util.Base64;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class EncryptHelper {

    public static final int DES_ENCRYPT = 1;
    public static final int DES_DECRYPT = 2;
    public static final int RSA_ENCRYPT = 1;
    public static final int RSA_DECRYPT = 2;

    //两个大素数的乘积
    private static final String MODULUS = "100631058000714094813874361191853577129731636346" +
            "6842182066057798249316268307506230708031001897812113438517632753293640566406197" +
            "5533777992898527248609143138412802721336537200964823317189470833821316882486106" +
            "1809490615593530405056055952622249066180336803996949444124622212096805545953751" +
            "253607916170340397933039";
    //公钥
    private static final String PUB_KEY = "65537";
    //私钥
    private static final String PRI_KEY = "2690015571531364308778651652837454899882155938107" +
            "5740707715132776187148793016466508650068087107695523642202737697714709374658856" +
            "7337926144909438742059567276066746345636651546167589395765476637152346432730556" +
            "5882948281350395945965370806287562521000896123964377566135765559931285724941861" +
            "0810177817213648575161";

    /**
     * 使用Base64对原字符串进行加密操作,并返回加密后的字符串信息
     *
     * @param originalString
     * @return
     */
    public static String baseEncode(String originalString) {
        String encodeToString = Base64.encodeToString(originalString.getBytes(), Base64.DEFAULT);
        return encodeToString;
    }

    /**
     * 使用Base64对原字符串进行加密操作,并返回加密后的字符串信息
     *
     * @param decodeString
     * @return
     */
    public static String baseDecode(String decodeString) {
        byte[] decode = Base64.decode(decodeString.getBytes(), Base64.DEFAULT);
        return new String(decode);
    }

    /**
     * 使用MD5对字符串进行加密操作，返回加密后的字符串信息
     *
     * @param orinalString
     * @return
     */
    public static String md5Encode(String orinalString) {
        //创建消息摘要实例，MD5表示生成消息摘要的算法名称
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("md5");
            byte[] digest = messageDigest.digest(orinalString.getBytes("UTF-8"));
            //用16进制显示生成的消息摘要
            StringBuffer result = new StringBuffer();
            for (byte b : digest) {
                result.append(String.format("%02x", b));
            }

            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String desOpenration(String src, String key, int mode) {
        String charsetName = "UTF-8";
        try {
            byte[] bytes = key.getBytes(charsetName);
            byte[] temp = new byte[8];  //des加密是8位
            System.arraycopy(bytes, 0, temp, 0, Math.min(bytes.length, temp.length));
            //1.密钥
            //2.加密算法的名称
            SecretKey secretKey = new SecretKeySpec(temp, "des");
            //创建一个密码生成器，参数表示生成密码的算法名称
            Cipher cipher = Cipher.getInstance("des");
            //加密
            if (mode == DES_ENCRYPT) {
                //初始化密码生成器
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                //生成密文
                byte[] bytes1 = cipher.doFinal(src.getBytes(charsetName));
                return Base64.encodeToString(bytes1, Base64.DEFAULT);
                //解密
            } else {
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
                byte[] bytes1 = cipher.doFinal(Base64.decode(src, Base64.DEFAULT));
                return new String(bytes1, 0, bytes1.length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String rsaOpration(String originaleString, int mode) {
        if (mode == RSA_ENCRYPT) {
            //获得生成一个公钥/私钥的工厂方法
            try {
                KeyFactory keyFactory = KeyFactory.getInstance("rsa");
                RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(new BigInteger(MODULUS), new BigInteger(PUB_KEY));
                //获得一个密码生成器，参数表示生成密码的算法
                Cipher cipher = Cipher.getInstance("rsa");
                //生成公钥
                PublicKey key = keyFactory.generatePublic(rsaPublicKeySpec);
                //用公钥加密文本
                cipher.init(Cipher.ENCRYPT_MODE, key);
                //获得密文
                byte[] bytes = cipher.doFinal(originaleString.getBytes("UTF-8"));
                String s1 = Base64.encodeToString(bytes, Base64.DEFAULT);

                return s1;
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            try {
                KeyFactory keyFactory = KeyFactory.getInstance("rsa");
                RSAPrivateKeySpec rsaPrivateKeySpec = new RSAPrivateKeySpec(new BigInteger(MODULUS), new BigInteger(PRI_KEY));
                Cipher cipher = Cipher.getInstance("rsa");
                //获取私钥
                PrivateKey privateKey = keyFactory.generatePrivate(rsaPrivateKeySpec);
                cipher.init(Cipher.DECRYPT_MODE, privateKey);
                byte[] bytes = cipher.doFinal(Base64.decode(originaleString, Base64.DEFAULT));

                return new String(bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }

}
