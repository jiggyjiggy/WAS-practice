package org.example.handler.request;

import org.example.calculator.Calculator;
import org.example.calculator.vo.PositiveNumber;
import org.example.http.request.HttpRequest;
import org.example.http.request.QueryStrings;
import org.example.http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientRequestHandler implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(ClientRequestHandler.class);
	private final Socket clientSocket;
	
	public ClientRequestHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	

	@Override
	public void run() {
		logger.info("[ClientRequestHandler] new client {} started", Thread.currentThread().getName());
		
		/**
		 * Step1 - 사용자 요청을 메인 Thread가 처리하도록 한다.
		 */
		try (
			InputStream inputStream = clientSocket.getInputStream();
			OutputStream outputStream = clientSocket.getOutputStream()
		) {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
			DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
			
			
			HttpRequest httpRequest = new HttpRequest(bufferedReader);
		
			// GET /calculate?operand1=11&operator=*&operand2=55 HTTP/1.1
			if (httpRequest.isGetRequest() && httpRequest.matchPath("/calculate")) {
				QueryStrings queryStrings = httpRequest.getQueryStrings();
				
				int operand1 = Integer.parseInt(queryStrings.getValue("operand1"));
				String operator = queryStrings.getValue("operator");
				int operand2 = Integer.parseInt(queryStrings.getValue("operand2"));
				
				Calculator calculator = new Calculator();
				int result = calculator.calculate(new PositiveNumber(operand1), operator, new PositiveNumber(operand2));
				byte[] body = String.valueOf(result).getBytes();
				
				HttpResponse response = new HttpResponse(dataOutputStream);
				response.response200Header("application/json", body.length);
				response.responseBody(body);
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
}
