package org.example.http.request;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpRequest {
	private final RequestLine requestLine;
//	private final HttpRequestHeader httpRequestHeader;
//	private final Body body;
	
	public HttpRequest(BufferedReader bufferedReader) throws IOException {
		this.requestLine = new RequestLine(bufferedReader.readLine());
	}
	
	public boolean isGetRequest() {
		return requestLine.isGetRequest();
	}
	
	public boolean matchPath(String path) {
		return requestLine.matchPath(path);
	}
	
	public QueryStrings getQueryStrings() {
		return requestLine.getQueryStrings();
	}
}
