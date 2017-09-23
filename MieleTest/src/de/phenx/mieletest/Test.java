package de.phenx.mieletest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Test {
	private static byte[] testEncryptedPayload = javax.xml.bind.DatatypeConverter.parseHexBinary(
			"bd01af27ac31029665ef24cf7c17048c9b0316c07e0d15b984d69861a7b658b00bd33c4f1b689af64643183ddc4e43462eb4e698a902a791084d9a5e3287eb449c75f6fb9a7aef2c47f9232e0367915a5aee10f92da3898446e22d75c80036ce72d4b30ba48007ed6d937523ba31febb213af8f24e5fb5485457e3c117f447eb13aa7bfca689801fd94805e8dc4f1fa324709e60b817043e518518a74d3e602911c7e21675ae650306344f6816b71cd0dbd9b591448c320ab84897d1278ce435bc3eeeca3b2b2be9e5106cd076001ecef1e2b981df1f1a9f7cb25b73350ffc6d0da4d00a8a559c80c9ac0bcafa0f8c204fa31ee350999c9e9bbb75b6d013cb7472f679491f7f5c1b03d7b24a75ff4c58a5baf183c37704272752dedfc88b95ef527e33e2d3ca50bb7ca152e4eb7338e696875c31a6d0246dfcaeb615fcafb55ef27bd3d296e19a9bcbb2875a113945ef876dbffc961e244917e2966d21e36b8b1015e67da064cd5f38b2bc996d37ac792799f01376588a2ffd1673c4a767b7b5ed72ff401579da38482b52aeea19f98980aaf211db98e47fcba0d185499bcb70e7890e256688a72a6576256b97c6e371589f0065ce9d410dc51876a9ceaa5d6f1097c41544b0fef067739b96423542c21c8ee0fbee5801028a5ef9633345401a4930b1e6f68723c2ba065db1247b2b57c5377b4c0370681780665148d0d46df0");
	private static byte[] testDecryptedPayload = javax.xml.bind.DatatypeConverter.parseHexBinary(
			"7B0A0922537461747573223A332C0A0922496E7465726E616C5374617465223A332C0A092250726F6772616D54797065223A302C0A092250726F6772616D4944223A312C0A092250726F6772616D5068617365223A3235362C0A092252656D61696E696E6754696D65223A5B322C35395D2C0A0922537461727454696D65223A5B302C305D2C0A092254617267657454656D7065726174757265223A5B343030305D2C0A092254656D7065726174757265223A5B2D33323736382C2D33323736382C2D33323736385D2C0A09225369676E616C496E666F223A66616C73652C0A09225369676E616C4661696C757265223A66616C73652C0A09225369676E616C446F6F72223A747275652C0A092252656D6F7465456E61626C65223A5B31352C302C305D2C0A092250726F63657373416374696F6E223A302C0A09224C69676874223A302C0A0922456C617073656454696D65223A5B302C305D2C0A0922457874656E6465645374617465223A223030313830313034303030303036343030313032303130313136303133413938303235383031433230323538303143323030303030303030303030303030303030303030303030313030303030303030303030303030303030303030303030303030303030303030303030303030303035353134303030303030464630323030303030313032220A2020202020202020207D");
	private static String testSignature = "248340418D1EDCBD2B59143145A956C87C6ABA92DC5093131B62BE961AE7A76E";

	public static void main(String[] args) {
		// testDatatypeConversion();
		// testDecryption();
		// testEncryption();
		 testConnection();
	}

	private static HttpRequest createTestRequest() {
		HttpRequest request = new HttpRequest();
		request.setHost(Config.host);
		request.setHttpMethod("GET");
		request.setResourcePath("/");
		request.setDate(getCurrentTimeInHttpFormat());
		request.setAcceptHeader("application/vnd.miele.v1+json");
		request.setContentTypeHeader(null);
		request.setRequestBody(null);
	
		// Nicht signaturrelevante Infos
		request.setUserAgent("Miele@mobile 2.3.3 Android");
		request.setAcceptEncoding("gzip");
		return request;
	}

	private static void testDecryption() {
		try {
			byte[] decryptedPayload = PayloadEncryptor.decrypt(testEncryptedPayload, Config.groupKey, testSignature);
			System.out.println(PayloadHelper.getStringFromPayload(decryptedPayload));
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
	}

	private static void testDatatypeConversion() {
		try {
			byte[] decryptedPayload = PayloadEncryptor.decrypt(testEncryptedPayload, Config.groupKey, testSignature);
			System.out.println(javax.xml.bind.DatatypeConverter.printHexBinary(decryptedPayload));

			String decryptedPayloadString = PayloadHelper.getStringFromPayload(decryptedPayload);
			System.out.println(decryptedPayloadString);

			byte[] test = PayloadHelper.getPayloadFromString(decryptedPayloadString);
			System.out.println(javax.xml.bind.DatatypeConverter.printHexBinary(test));

			byte[] encryptedPayload = PayloadEncryptor.encrypt(test, Config.groupKey, testSignature);
			System.out.println(javax.xml.bind.DatatypeConverter.printHexBinary(encryptedPayload));
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Genau so verschlüsselt das Miele Gerät. Wichtig: Payload muss immer ein
	 * mehrfaches von 16 Bytes als Länge haben! Das wird vom Miele Gerät durch
	 * Leerzeichen vor der schließenden Klammer im JSON ausgeglichen.
	 */
	private static void testEncryption() {
		String testString = PayloadHelper.getStringFromPayload(testDecryptedPayload);
		byte[] payload = PayloadHelper.getPayloadFromString(testString);
		System.out.println("Test mit Payload (Länge " + payload.length + "): " + testString);

		String signature = testSignature;

		try {
			byte[] encryptedPayload = PayloadEncryptor.encrypt(payload, Config.groupKey, signature);
			System.out.println(
					"Verschlüsselter Payload: " + javax.xml.bind.DatatypeConverter.printHexBinary(encryptedPayload));

			byte[] decryptedPayload = PayloadEncryptor.decrypt(encryptedPayload, Config.groupKey, signature);
			System.out
					.println("Wieder entchlüsselter Payload: " + PayloadHelper.getStringFromPayload(decryptedPayload));
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}

	}

	private static void testConnection() {
		HttpRequest request = createTestRequest();
		request.setSignature(new SignatureBuilder(Config.groupKey, request).build());

		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) new URL("http://" + request.getHost() + request.getResourcePath())
					.openConnection();
			connection.setRequestMethod(request.getHttpMethod());
			connection.setRequestProperty("Accept", request.getAcceptHeader());
			connection.setRequestProperty("Date", request.getDate());
			connection.setRequestProperty("User-Agent", request.getUserAgent());
			connection.setRequestProperty("Host", request.getHost());
			connection.setRequestProperty("Authorization",
					"MieleH256 " + Config.groupId + ":" + request.getSignature());
			connection.setRequestProperty("Accept-Encoding", request.getAcceptEncoding());

			// Response auswerten
			System.out.println("Response Status: " + connection.getResponseCode());
			String responseSignature = connection.getHeaderField("X-Signature").substring(27);
			System.out.println("Response Signature: " + connection.getHeaderField("X-Signature"));
			byte[] bb = fetchPayloadFromConnection(connection);

			try {
				byte[] res = PayloadEncryptor.decrypt(bb, Config.groupKey, responseSignature);
				System.out.println("Decrypted:");
				System.out.println(new String(res, StandardCharsets.UTF_8));
			} catch (GeneralSecurityException e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static byte[] fetchPayloadFromConnection(HttpURLConnection connection) throws IOException {
		InputStream inputStream = connection.getInputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int bytesRead = -1;
		byte buffer[] = new byte[2048];
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			baos.write(buffer, 0, bytesRead);
		}
		byte bb[] = baos.toByteArray();
		return bb;
	}

	private static String getCurrentTimeInHttpFormat() {
		Date currentTime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		return sdf.format(currentTime);
	}
}
