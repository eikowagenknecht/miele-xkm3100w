package de.phenx.mieletest;

public class HttpRequest {
	private String httpMethod; // Signatur relevant
	private String acceptHeader; // Signatur relevant
	private String contentTypeHeader; // Signatur relevant, optional
    private String date; // Signatur relevant
    private String host; // Signatur relevant
    private String resourcePath; // Signatur relevant

    private String userAgent;
	private String acceptEncoding;
    private String signature;
    private String requestBody;
    
    public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getAcceptEncoding() {
		return acceptEncoding;
	}

	public void setAcceptEncoding(String acceptEncoding) {
		this.acceptEncoding = acceptEncoding;
	}

    public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getAcceptHeader() {
		return acceptHeader;
	}

    /**
     * @param acceptHeader "application/vnd.miele.v1+json"
     */
	public void setAcceptHeader(String acceptHeader) {
		this.acceptHeader = acceptHeader;
	}

	public String getContentTypeHeader() {
		return contentTypeHeader;
	}

	/**
	 * @param contentTypeHeader null
	 */
	public void setContentTypeHeader(String contentTypeHeader) {
		this.contentTypeHeader = contentTypeHeader;
	}

	public String getDate() {
		return date;
	}

	/**
	 * @param date "EEE, dd MMM yyyy HH:mm:ss zzz", Locale US, Timezone GMT
	 * Example: "Fri, 15 Sep 2017 19:57:40 GMT"
	 */
	public void setDate(String date) {
		this.date = date;
	}

	public String getHost() {
		return host;
	}

	/**
	 * @param host "192.168.1.1" or "test.local."
	 */
	public void setHost(String host) {
		this.host = host;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	/**
	 * @param httpMethod "PUT", "GET", ...
	 */
	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	/**
	 * @param resourcePath "/", "/Devices/", ...
	 */
	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}


}
