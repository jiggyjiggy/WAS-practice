package org.example.http.request;

import java.util.Objects;

// GET /calculate?operand1=11&operator=*&operand2=55
public class RequestLine {
	private final String method;    // GET
	private final String urlPath;   // /calculate
	private QueryStrings queryString;     // operand1=11&operator=*&operand2=55
	
	public RequestLine(String method, String urlPath, String queryString) {
		this.method = method;
		this.urlPath = urlPath;
		this.queryString = new QueryStrings(queryString);
	}
	
	public RequestLine(String requestLine) {
		System.out.println(requestLine);
		String[] tokens = requestLine.split(" ");
		this.method = tokens[0];
		String[] urlPathTokens = tokens[1].split("\\?");
		this.urlPath = urlPathTokens[0];
		
		if (urlPathTokens.length == 2) {
			this.queryString = new QueryStrings(urlPathTokens[1]);
		}
	}
	
	public boolean isGetRequest() {
		return "GET".equals(this.method);
	}
	
	public boolean matchPath(String requestPath) {
		return urlPath.equals(requestPath);
	}
	
	public QueryStrings getQueryStrings() {
		return this.queryString;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RequestLine that = (RequestLine) o;
		return Objects.equals(method, that.method) && Objects.equals(urlPath, that.urlPath) && Objects.equals(queryString, that.queryString);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(method, urlPath, queryString);
	}
}
