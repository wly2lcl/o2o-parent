package com.parent.o2o.utils.rsa;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;

import com.parent.o2o.utils.base64.Base64Utils;

public class RSAUtilsSHA256 {

	/**
	 * 密钥算法RSA
	 */
	public static final String KEY_ALGORITHM = "RSA";

	/**
	 * 加密算法RSA
	 */
	public static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1PADDING";

	/**
	 * 签名算法
	 */
	public static final String SIGNATURE_ALGORITHM = "SHA256WithRSA";

	/**
	 * 获取公钥的key
	 */
	private static final String PUBLIC_KEY = "RSAPublicKey";

	/**
	 * 获取私钥的key
	 */
	private static final String PRIVATE_KEY = "RSAPrivateKey";

	/**
	 * RSA最大加密明文大小
	 */
	private static final int MAX_ENCRYPT_BLOCK = 117;

	/**
	 * RSA最大解密密文大小
	 */
	private static final int MAX_DECRYPT_BLOCK = 128;

	/**
	 * 生成密钥对(公钥和私钥)
	 */
	public static Map<String, Key> genKeyPair() {
		KeyPairGenerator keyPairGen;
		try {
			keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		Map<String, Key> keyMap = new HashMap<String, Key>(2);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}
	
  

	/**
	 * 用私钥对信息生成数字签名
	 * 
	 * @param data
	 *            已加密数据
	 * @param privateKey
	 *            私钥(BASE64编码)
	 */
	public static String sign(byte[] data, String privateKey) throws Exception {
		byte[] keyBytes = Base64Utils.decode(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(privateK);
		signature.update(data);
		return Base64Utils.encode(signature.sign());
	}

	/**
	 * 校验数字签名
	 * 
	 * @param data
	 *            已加密数据
	 * @param publicKey
	 *            公钥(BASE64编码)
	 * @param sign
	 *            数字签名
	 */
	public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
		byte[] keyBytes = Base64Utils.decode(publicKey);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey publicK = keyFactory.generatePublic(keySpec);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(publicK);
		signature.update(data);
		return signature.verify(Base64Utils.decode(sign));
	}

	/**
	 * 私钥解密
	 * 
	 * @param encryptedData
	 *            已加密数据
	 * @param privateKey
	 *            私钥(BASE64编码)
	 */
	public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
		ByteArrayOutputStream out = null;
		try {
			byte[] keyBytes = Base64Utils.decode(privateKey);
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, privateK);
			int inputLen = encryptedData.length;
			out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段解密
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
					cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_DECRYPT_BLOCK;
			}
			byte[] decryptedData = out.toByteArray();
			return decryptedData;
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 公钥解密
	 * 
	 * @param encryptedData
	 *            已加密数据
	 * @param publicKey
	 *            公钥(BASE64编码)
	 */
	public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {
		ByteArrayOutputStream out = null;
		try {
			byte[] keyBytes = Base64Utils.decode(publicKey);
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			Key publicK = keyFactory.generatePublic(x509KeySpec);
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, publicK);
			int inputLen = encryptedData.length;
			out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段解密
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
					cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_DECRYPT_BLOCK;
			}
			byte[] decryptedData = out.toByteArray();
			return decryptedData;
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 公钥解密
	 * 
	 * @param srcData
	 *            已加密数据
	 * @param publicKey
	 *            公钥(BASE64编码)
	 *            
	 *  入参与返回值均为String类型          
	 */
	public static String decryptByPublicKey(String srcData, String publicKey) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		ByteArrayOutputStream out = null;
		try {
			byte[] keyBytes = Base64.decodeBase64(publicKey.getBytes());
			byte[] encryptedData=Base64.decodeBase64(srcData.getBytes());
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			Key publicK = keyFactory.generatePublic(x509KeySpec);
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, publicK);
			int inputLen = encryptedData.length;
			out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段解密
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
					cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_DECRYPT_BLOCK;
			}
			byte[] decryptedData = out.toByteArray();
			return new String(decryptedData,"utf-8");
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 公钥加密
	 * 
	 * @param data
	 *            源数据
	 * @param publicKey
	 *            公钥(BASE64编码)
	 */
	public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
		ByteArrayOutputStream out = null;
		try {
			byte[] keyBytes = Base64Utils.decode(publicKey);
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			Key publicK = keyFactory.generatePublic(x509KeySpec);
			// 对数据加密
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, publicK);
			int inputLen = data.length;
			out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段加密
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
					cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(data, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_ENCRYPT_BLOCK;
			}
			byte[] encryptedData = out.toByteArray();
			return encryptedData;
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 公钥加密
	 * 
	 * @param data
	 *            源数据
	 * @param publicKey
	 *            公钥(BASE64编码)
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchPaddingException 
	 * @throws InvalidKeyException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws UnsupportedEncodingException 
	 */
	public static String encryptByPublicKey(String srcData, String publicKey) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		ByteArrayOutputStream out = null;
		try {
			byte[] keyBytes =Base64.decodeBase64(publicKey.getBytes());
			byte[] data=srcData.getBytes("utf-8");
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			Key publicK = keyFactory.generatePublic(x509KeySpec);
			// 对数据加密
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, publicK);
			int inputLen = data.length;
			out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段加密
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
					cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(data, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_ENCRYPT_BLOCK;
			}
			byte[] encryptedData = out.toByteArray();
			return  Base64.encodeBase64String(encryptedData);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 私钥加密
	 * 
	 * @param data
	 *            源数据
	 * @param privateKey
	 *            私钥(BASE64编码)
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
		ByteArrayOutputStream out = null;
		try {
			byte[] keyBytes = Base64Utils.decode(privateKey);
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, privateK);
			int inputLen = data.length;
			out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段加密
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
					cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(data, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_ENCRYPT_BLOCK;
			}
			byte[] encryptedData = out.toByteArray();
			return encryptedData;
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**********************************************************
	 * 方法说明： 私钥加密<br>
	 * 参数： @param srcData
	 * 参数： @param privateKey
	 * 参数： @param input_charset
	 * 参数： @return
	 * 参数： @throws NoSuchAlgorithmException
	 * 参数： @throws InvalidKeySpecException
	 * 参数： @throws NoSuchPaddingException
	 * 参数： @throws InvalidKeyException
	 * 参数： @throws IllegalBlockSizeException
	 * 参数： @throws BadPaddingException
	 * 参数： @throws UnsupportedEncodingException <br> 
	 * 创建人：liuwei  <br>
	 * 创建时间：2017年11月29日 上午9:51:25<br>   
	 * 修改备注： <br>
	 *********************************************************
	 */
	public static String encryptByPrivateKey(String srcData, String privateKey,String input_charset) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException  {
		ByteArrayOutputStream out = null;
		try {
			byte[] keyBytes = Base64.decodeBase64(privateKey.getBytes());
			byte[] data=srcData.getBytes(input_charset);
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, privateK);
			int inputLen = data.length;
			out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段加密
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
					cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(data, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_ENCRYPT_BLOCK;
			}
			byte[] encryptedData = out.toByteArray();
			return Base64.encodeBase64String(encryptedData);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static byte[] encryptByPublicKey(byte[] data, BigInteger modulus, BigInteger publicExponent) throws Exception {
		ByteArrayOutputStream out = null;
		try {
			RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, publicExponent);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			Key publicK = keyFactory.generatePublic(publicKeySpec);
			// 对数据加密
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, publicK);
			int inputLen = data.length;
			out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段加密
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
					cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(data, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_ENCRYPT_BLOCK;
			}
			byte[] encryptedData = out.toByteArray();
			return encryptedData;
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取私钥
	 * 
	 * @param keyMap
	 *            密钥对
	 */
	public static String getPrivateKey(Map<String, Key> keyMap) {
		Key key = keyMap.get(PRIVATE_KEY);
		return Base64Utils.encode(key.getEncoded());
	}

	/**
	 * 获取公钥
	 * 
	 * @param keyMap
	 *            密钥对
	 */
	public static String getPublicKey(Map<String, Key> keyMap) {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return Base64Utils.encode(key.getEncoded());
	}
	
	
	public static String bytesToHexString(byte[] src){  
	    StringBuilder stringBuilder = new StringBuilder("");  
	    if (src == null || src.length <= 0) {  
	        return null;  
	    }  
	    for (int i = 0; i < src.length; i++) {  
	        int v = src[i] & 0xFF;  
	        String hv = Integer.toHexString(v);  
	        if (hv.length() < 2) {  
	            //stringBuilder.append(0);  
	        	stringBuilder.append("$0" + hv + ",");  
	        }  else {
	        	stringBuilder.append("$" + hv + ",");  
	        }
	        
	    }  
	    return stringBuilder.toString();  
	} 
}