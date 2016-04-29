
package com.zoomkey.core;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.BASE64EncoderStream;

/**
 * DES+Base64安全编码组件，工具类，直接调用其中方法即可
 * DES_KEY是自定义密钥规则，但是长度必须为16位
 * 
 */
public class DESCoder {

	public static final String	ALGORITHM	= "DES";

	public static final String	DES_KEY		= "*!@#$%&<zoomkey>";

	/**
	 * 转换密钥<br>
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static Key toKey(byte[] key) throws Exception {
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		SecretKey secretKey = keyFactory.generateSecret(dks);
		return secretKey;
	}

	/**
	 * 解密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data, String key) throws Exception {
		Key k = toKey(decryptBASE64(key));
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, k);
		return cipher.doFinal(data);
	}

	/**
	 * 加密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data, String key) throws Exception {
		Key k = toKey(decryptBASE64(key));
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, k);
		return cipher.doFinal(data);
	}

	/**
	 * 生成密钥
	 * 
	 * @param seed
	 * @return
	 * @throws Exception
	 */
	public static String initKey(String seed) throws Exception {
		SecureRandom secureRandom = null;
		if (seed != null) {
			secureRandom = new SecureRandom(decryptBASE64(seed));
		} else {
			secureRandom = new SecureRandom();
		}
		KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM);
		kg.init(secureRandom);
		SecretKey secretKey = kg.generateKey();
		return encryptBASE64(secretKey.getEncoded());
	}

	/**
	* BASE64解密
	* 
	* @param key
	* @return
	* @throws Exception
	*/
	public static byte[] decryptBASE64(String key) throws Exception {
		return BASE64DecoderStream.decode(key.getBytes());
	}

	/**
	 * BASE64加密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptBASE64(byte[] key) throws Exception {
		byte[] encode = BASE64EncoderStream.encode(key);
		return new String(encode);
	}

	public static void main(String[] args) throws Exception {
		String inputStr = "sjkP@ssw0rd";
		String key = DESCoder.initKey(DES_KEY);
		System.err.println("原文:\t" + inputStr);
		System.err.println("密钥:\t" + key);
		byte[] inputData = inputStr.getBytes();
		inputData = DESCoder.encrypt(inputData, key);
		System.err.println("加密后:\t" + DESCoder.encryptBASE64(inputData));
		byte[] outputData = DESCoder.decrypt(inputData, key);
		String outputStr = new String(outputData);
		System.err.println("解密后:\t" + outputStr);
	}
}