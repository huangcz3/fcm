package com.asiainfo.fcm.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * 加密解密工具.
 * Created by RUOK on 2017/6/16.
 */
public class PasswordUtil {
    private byte[] desKey;
    public static String key = "asia123?";

    // 解密数据
    public static String decrypt(String message) throws Exception {

        byte[] bytesrc = convertHexString(message);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));

        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

        byte[] retByte = cipher.doFinal(bytesrc);
        return java.net.URLDecoder.decode(new String(retByte), "utf-8");
    }

    //加密数据
    public static String encrypt(String message) throws Exception {
        message = java.net.URLEncoder.encode(message, "utf-8").toLowerCase();
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

        return toHexString(cipher.doFinal(message.getBytes("UTF-8"))).toUpperCase();
    }

    public static byte[] convertHexString(String ss) {
        byte digest[] = new byte[ss.length() / 2];
        for (int i = 0; i < digest.length; i++) {
            String byteString = ss.substring(2 * i, 2 * i + 2);
            int byteValue = Integer.parseInt(byteString, 16);
            digest[i] = (byte) byteValue;
        }

        return digest;
    }

    public static String toHexString(byte b[]) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String plainText = Integer.toHexString(0xff & b[i]);
            if (plainText.length() < 2)
                plainText = "0" + plainText;
            hexString.append(plainText);
        }

        return hexString.toString();
    }

    public static void main(String[] args) throws Exception {
        String str = "17F197DE3F52A96AA86A6F66F15B7F70719C0F453E3ED3E124ED52FA4D9217FC41BFC97B0ED01E8E133A219B919543ED4ED39A2C34906119415055BA186CEACFBC131BAE107007DDE58D93EB45853D5C1916395B98766BED44379B44721F138DB323A4DEBB5F4C67CE5D5E71DDCF60F7";
        //String tmp ="loginno=ai_liai&pwd=60BCF81879D9026BF72536E340ABD71D&op_time=2017-12-28 09:34:14.573089";
       // String tmp ="loginno=vgopbz_wangshengchuan&pwd=5AB34A6350BB7488&op_time=2018-03-22 12:34:24.577089";
        //String tmp ="loginno=tf_jiangshirong&pwd=E141CCB81639F67331E1ACF291102B60&op_time=2018-03-23 10:34:24.577089";
        String tmp ="loginno=ai_zhangyue&pwd=6c68663a39383f&op_time=2018-04-20 10:34:24.577089";
        System.out.println(encrypt(tmp));
        //"loginno=ai_zhangyue&pwd=6c68663a39383f&op_time

        //System.out.println(decrypt(str));
    }
}
