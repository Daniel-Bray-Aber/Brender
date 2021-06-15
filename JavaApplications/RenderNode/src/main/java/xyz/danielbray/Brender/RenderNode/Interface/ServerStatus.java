package xyz.danielbray.Brender.RenderNode.Interface;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ServerStatus implements HttpHandler {

	@Override
	public void handle(HttpExchange ex) throws IOException {
		String response = "{\"server_status\":\"up\"}";
		Headers headers = ex.getResponseHeaders();
		
		headers.set("Content-Type", "application/json");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Methods", "GET, OPTIONS");
        headers.add("Access-Control-Allow-Headers", "Content-Type,Authorization");
		
		ex.sendResponseHeaders(200, response.length());
		OutputStream os = ex.getResponseBody();
		os.write(response.getBytes());
		os.close();
		
	}

}
