package com.parent.o2o.utils.aes;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import com.parent.o2o.utils.base64.Base64Utils;

public class AESUtils {

	public static final String KEY_ALGORITHM_AES = "AES";
	public static final String CIPHER_ALGORITHM_AES = "AES/ECB/PKCS5Padding"; 

	public static Key genKey() {
		KeyGenerator kgen;
		try {
			kgen = KeyGenerator.getInstance(KEY_ALGORITHM_AES);
			kgen.init(128, new SecureRandom());
			return kgen.generateKey();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] decrypt(byte[] encryptedData, byte[] key) throws Exception {
		SecretKeySpec sks = new SecretKeySpec(key, KEY_ALGORITHM_AES);
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_AES);// 创建密码器
		cipher.init(Cipher.DECRYPT_MODE, sks);// 初始化
		return cipher.doFinal(encryptedData);
	}

	public static byte[] decrypt(byte[] encryptedData, String key) throws Exception {
		return decrypt(encryptedData, Base64Utils.decode(key));
	}

	public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		SecretKeySpec sks = new SecretKeySpec(key, KEY_ALGORITHM_AES);
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_AES);// 创建密码器
		cipher.init(Cipher.ENCRYPT_MODE, sks);// 初始化
		return cipher.doFinal(data);
	}

	public static byte[] encrypt(byte[] data, String key) throws Exception {
		return encrypt(data, Base64Utils.decode(key));
	}
	
	public static String getAesStr(String str) throws Exception {	
		byte[] mw  = AESUtils.encrypt(Base64Utils.decode(Base64Utils.encode(str.getBytes())), "ezBGRjFDNDU3LUZCMUMtNA==");
		return Base64Utils.encode(mw);
	}
	
	public static String getStr(String str) throws Exception {
		byte[] mw  = Base64Utils.decode(str);
		return new String(AESUtils.decrypt(mw, "ezBGRjFDNDU3LUZCMUMtNA=="));
	}
	
	
	public static void main(String[] args) throws Exception {
		String plainText = "1001";
		String miwen = AESUtils.getAesStr(plainText);
		System.out.println("密文 : " + miwen);
		System.out.println("明文 : " + AESUtils.getStr(miwen));

	}
}
