package de.phenx.mieletest;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class SignatureBuilder {
	private byte[] mKey;

	private String mAcceptHeader;
	private String mContentTypeHeader;
	private String mDate;
	private String mHost;
	private String mHttpMethod;

	//private String mRequestBody;
	private String mResourcePath;

	private static final Charset CHARSET_ASCII = Charset.forName("US-ASCII");
	private static final String SIGNATURE_TYPE = "HmacSHA256";

	public SignatureBuilder(byte[] groupKey) {
		mKey = groupKey;
	}

	public SignatureBuilder(byte[] groupKey, HttpRequest request) {
		mKey = groupKey;
		mAcceptHeader = request.getAcceptHeader();
		mContentTypeHeader = request.getContentTypeHeader();
		mDate = request.getDate();
		mHost = request.getHost();
		mHttpMethod = request.getHttpMethod();
		//mRequestBody = request.getRequestBody();
		mResourcePath = request.getResourcePath();
	}

	public SignatureBuilder acceptHeader(String p1) {
		mAcceptHeader = p1;
		return this;
	}

	public SignatureBuilder contentTypeHeader(String p1) {
		mContentTypeHeader = p1;
		return this;
	}

	public SignatureBuilder date(String p1) {
		mDate = p1;
		return this;
	}

	public SignatureBuilder host(String p1) {
		mHost = p1;
		return this;
	}

	public SignatureBuilder httpMethod(String p1) {
		mHttpMethod = p1;
		return this;
	}

	/*
	public SignatureBuilder requestBody(String p1) {
		mRequestBody = p1;
		return this;
	}
	*/

	public SignatureBuilder resourcePath(String p1) {
		mResourcePath = p1;
		return this;
	}

	public String build() throws IllegalStateException {
		String sourceString = buildSignatureString();
		byte[] sourceStringASCII = sourceString.getBytes(CHARSET_ASCII);
		byte[] signatureBytes;
		try {
			signatureBytes = sign(sourceStringASCII);
		} catch (Exception e) {
			return sourceString;
		}
		return javax.xml.bind.DatatypeConverter.printHexBinary(signatureBytes).toUpperCase();
	}

	private String buildSignatureString() throws IllegalStateException {
		if (mHttpMethod == null) {
			throw new IllegalStateException("HTTP Method is not set");
		}
		if (mHost == null) {
			throw new IllegalStateException("Host is not set");
		}
		if (mDate == null) {
			throw new IllegalStateException("Date is not set");
		}
		StringBuilder sb = new StringBuilder(mHttpMethod);
		sb.append("\n");
		sb.append(mHost);
		if (mResourcePath != null) {
			sb.append(mResourcePath);
		}
		sb.append("\n");
		if (mContentTypeHeader != null) {
			sb.append(mContentTypeHeader);
		}
		sb.append("\n");
		if (mAcceptHeader != null) {
			sb.append(mAcceptHeader);
		}
		sb.append("\n");
		sb.append(mDate);
		sb.append("\n");
		// Wird durch den Aufruf nie gesetzt von dem Miele Gerät.
		//if (mRequestBody != null) {
		//	sb.append(mRequestBody);
		//}
		return sb.toString();
	}

	private byte[] sign(byte[] p1) throws NoSuchAlgorithmException, InvalidKeyException {
		Mac mac = Mac.getInstance(SIGNATURE_TYPE);
		if (mKey != null) {
			SecretKeySpec secretKey = new SecretKeySpec(mKey, SIGNATURE_TYPE);
			mac.init(secretKey);
		}
		return mac.doFinal(p1);
	}
}
