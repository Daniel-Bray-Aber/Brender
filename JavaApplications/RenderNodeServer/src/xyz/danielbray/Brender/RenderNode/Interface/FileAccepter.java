package xyz.danielbray.Brender.RenderNode.Interface;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class FileAccepter implements HttpHandler {

	@Override
	public void handle(HttpExchange ex) throws IOException {

		
		System.out.println(ex.getRequestMethod());
		
		InputStream is = ex.getRequestBody();
		List<Integer> input = new ArrayList<Integer>();
		
		int i;
		while((i = is.read()) != -1)
		{
			input.add(i);
		}
		
		
		

		
		
		String response = "{\"fileAccept\":\"yes\"}";
		Headers headers = ex.getResponseHeaders();
		
		headers.set("Content-Type", "application/json");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Methods", "POST, OPTIONS");
        headers.add("Access-Control-Allow-Headers", "Content-Type, Authorization");
		
		ex.sendResponseHeaders(200, response.length());
		OutputStream os = ex.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}

}
