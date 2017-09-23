package de.phenx.mieletest;

import java.security.GeneralSecurityException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class PayloadEncryptor {
    private static final String ALGORYTHM = "AES";
    private static final String CIPHER_TYPE = "AES/CBC/NoPadding";
    
	public static byte[] decrypt(byte[] payload, byte[] groupKey, String signature) throws GeneralSecurityException {
		IvParameterSpec ivParameterSpec = getIvParameterFromSignature(signature);
		SecretKeySpec secretKeySpec = getSecretKeyFromGroupKey(groupKey);

		Cipher cipher = Cipher.getInstance(CIPHER_TYPE);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
		byte[] decryptedPayload = cipher.doFinal(payload);
		
		return decryptedPayload;
	}
	
	public static byte[] encrypt(byte[] payload, byte[] groupKey, String signature) throws GeneralSecurityException {
        IvParameterSpec ivParameterSpec = getIvParameterFromSignature(signature);
		SecretKeySpec secretKeySpec = getSecretKeyFromGroupKey(groupKey);

		Cipher cipher = Cipher.getInstance(CIPHER_TYPE);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
		byte[] encryptedPayload = cipher.doFinal(payload);
		
		return encryptedPayload;
	}
	
	/**
	 * Berechnet aus der Signatur den Initialisierungsvektor zum Ent- oder Verschlüsseln.
	 */
	private static IvParameterSpec getIvParameterFromSignature(String signature) {
		String initVectorString = signature.substring(0, (signature.length() / 2));
        byte[] initVector = javax.xml.bind.DatatypeConverter.parseHexBinary(initVectorString);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initVector);
		return ivParameterSpec;
	}
	/**
	 * Berechnet aus dem GroupKey den SecretKey zum Ent- oder Verschlüsseln.
	 */
	private static SecretKeySpec getSecretKeyFromGroupKey(byte[] groupKey) {
		byte[] secretKey = Arrays.copyOf(groupKey, (groupKey.length / 2));
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, ALGORYTHM);
		return secretKeySpec;
	}
	
}
