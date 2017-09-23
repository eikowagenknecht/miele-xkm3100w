package de.phenx.mieletest;

import java.nio.charset.StandardCharsets;

public class PayloadHelper {
	/**
	 * Wandelt die byte[] Payload in einen String um (UTF-8) 
	 */
	public static String getStringFromPayload(byte[] payload) {
		return new String(payload, StandardCharsets.UTF_8);
	}
	
	public static byte[] getPayloadFromString(String string) {
		return string.getBytes(StandardCharsets.UTF_8);
	}

}
