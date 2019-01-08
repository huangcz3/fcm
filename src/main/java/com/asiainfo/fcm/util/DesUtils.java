package com.asiainfo.fcm.util;

/*
 * @(#)DESConsole2.java 0.1 2012-2-8
 *
 * Copyright (c) 2011-2015 Bingosoft, Inc.
 * All rights reserved.
 *
 */

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.Key;

/**
 * Class description goes here.
 * 
 * @version 0.1 2012-2-8
 * @author chenxulong
 */
public class DesUtils {
	
	public static String key="scyd-llh";
	
	public static String EncryptString(String strInput, String txtKey) throws Exception {
		Cipher enCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");// 得到加密对象Cipher
		enCipher.init(Cipher.ENCRYPT_MODE, getKey(txtKey));// 设置工作模式为加密模式，给出密钥和向量
		byte[] pasByte = enCipher.doFinal(strInput.getBytes("utf-8"));
		BASE64Encoder base64Encoder = new BASE64Encoder();
		return base64Encoder.encode(pasByte);
	}
	
	/**
	 * 通过keyId解密对应的字符串
	 * @param strInput
	 * @param keyId
	 * @return
	 * @throws Exception
	 */
    public static String DecryptString(String strInput,String keyId) throws Exception {
		Cipher deCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		deCipher.init(Cipher.DECRYPT_MODE, getKey(keyId));
		BASE64Decoder base64Decoder = new BASE64Decoder();
		byte[] pasByte = deCipher.doFinal(base64Decoder.decodeBuffer(strInput));
		return new String(pasByte, "UTF-8");
    }

	public static Key getKey(String txtKey) throws Exception {
		DESKeySpec keySpec = new DESKeySpec(txtKey.getBytes());// 设置密钥参数
		//iv = new IvParameterSpec(DESIV);// 设置向量
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");// 获得密钥工厂
		return keyFactory.generateSecret(keySpec);// 得到密钥对象
	}
	// 测试
	public static void main(String[] args) throws Exception {
		System.out.println("加密:" + DesUtils.EncryptString("ngai01", "scyd-llh"));
		System.out.println("解密2:" + DesUtils.DecryptString("xOuoKGPVAVbxFenPZ/AtCg==", "boncbonc"));

	}
}
