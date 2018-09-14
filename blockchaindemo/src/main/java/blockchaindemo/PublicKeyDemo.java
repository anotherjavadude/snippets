package blockchaindemo;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.Cipher;

public class PublicKeyDemo {

	private KeyPairGenerator keyGen;
	private KeyPair pair;
	static PrivateKey privateKey;
	static PublicKey publicKey;
	private Cipher cipher;

	public static void main(String[] args) {

		PublicKeyDemo pubKeyDemo = new PublicKeyDemo();

		pubKeyDemo.generateKeys();

		System.out.println("---- ENCRYPT WITH PUBLIC KEY----");
		String plainText = "My 2018 Cryptography Sample !";
		String cipherText = pubKeyDemo.encryptText(plainText, publicKey);
		System.out.println("Plain Text: " + plainText);
		System.out.println("Cipher Text: " + cipherText);	
		System.out.println("----------------------------------------------------------");

		plainText = "MY 2018 Cryptography Sample !";
		cipherText = pubKeyDemo.encryptText(plainText, publicKey);
		System.out.println("Plain Text: " + plainText);
		System.out.println("Cipher Text: " + cipherText);
		
		System.out.println("----------------------------------------------------------");
		System.out.println("---- DECRYPT WITH PRIVATE KEY----");
		plainText = pubKeyDemo.decryptText(cipherText, privateKey);
		System.out.println("Cipher Text: " + cipherText);
		System.out.println("Plain Text: " + plainText);
		System.out.println("------------------------");
		
		System.out.println("---- DECRYPT WITH PUBLIC KEY----");
		plainText = pubKeyDemo.decryptText(cipherText, publicKey);
		System.out.println("Cipher Text: " + cipherText);
		System.out.println("Plain Text: " + plainText);
		System.out.println("----------------------------------------------------------");

		
		System.out.println("---- ENCRYPT WITH PRIVATE KEY----");
		plainText = "My 2018 Cryptography Sample !";
		cipherText = pubKeyDemo.encryptText(plainText, privateKey);
		System.out.println("Plain Text: " + plainText);
		System.out.println("Cipher Text: " + cipherText);		
		System.out.println("----------------------------------------------------------");

		System.out.println("---- DECRYPT WITH PUBLIC KEY----");
		plainText = pubKeyDemo.decryptText(cipherText, publicKey);
		System.out.println("Cipher Text: " + cipherText);
		System.out.println("Plain Text: " + plainText);		
		
	}

	private void generateKeys() {

		try {
			Base64.Encoder encoder = Base64.getEncoder();

			keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(512);

			pair = keyGen.generateKeyPair();
			privateKey = pair.getPrivate();
			publicKey = pair.getPublic();

			System.out.println("---- KEY GENERATION ----");
			System.out.println("Algorithm: " + privateKey.getAlgorithm());
			System.out.println("Format: " + privateKey.getFormat());
			System.out.println("Private Key:\n" + encoder.encodeToString(privateKey.getEncoded()));
			System.out.println("Public Key:\n" + encoder.encodeToString(publicKey.getEncoded()));
			System.out.println("------------------------");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String encryptText(String messageText, Key key) {

		Base64.Encoder encoder = Base64.getEncoder();
		try {
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return encoder.encodeToString(cipher.doFinal(messageText.getBytes("UTF-8")));

		} catch (Exception e) {
			e.printStackTrace();
			return "nil";
		}
	}
	
	private String decryptText(String cipherText, Key key) {

		Base64.Decoder decoder = Base64.getDecoder();
		try {
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, key);
			return new String(cipher.doFinal(decoder.decode(cipherText)), "UTF-8");

		} catch (Exception e) {
			System.out.println(">>>>>> " + e.getMessage());
			return "nil";
		}
	}

}

