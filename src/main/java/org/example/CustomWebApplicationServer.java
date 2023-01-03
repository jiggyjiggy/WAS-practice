package org.example;

import org.example.handler.request.ClientRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * custom tomcat(WAS)을 만들어 보는 과정
 */
public class CustomWebApplicationServer {
	private final int port;
	private final ExecutorService executorService = Executors.newFixedThreadPool(10);   // thread pool 구현법 중 하나
	private static final Logger logger = LoggerFactory.getLogger(CustomWebApplicationServer.class);
	
	public CustomWebApplicationServer(int port) {
		this.port = port;
	}
	
	public void start() throws IOException {
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			logger.info("[CustomWebApplicationServer] started {} port", port);
			
			Socket clientSocket;
			logger.info("[CustomWebApplicationServer] waiting for client");
			
			while (!Objects.isNull(clientSocket = serverSocket.accept())) {
				logger.info("[CustomWebApplicationServer] client connected");
				
				/**
				 * Step3 - Thread Pool을 적용해 안정적인 서비스가 가능하도록 한다.
				 * */
				executorService.execute(new ClientRequestHandler(clientSocket));
			}
		}
	}
}
