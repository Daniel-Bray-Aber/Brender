package xyz.danielbray.Brender.RenderNode.Interface;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import xyz.danielbray.Brender.RenderNode.Utility.Logging.Logger;
import xyz.danielbray.Brender.RenderNode.Work.BlenderFile;

public class FileAccepter implements HttpHandler {

	@Override
	public void handle(HttpExchange ex) throws IOException {
		switch(ex.getRequestMethod())
		{
		case "POST":
			handlePOST(ex);
			break;
		case "OPTIONS":
			handleOPTIONS(ex);
			break;
		}
	}
	
	private void handlePOST(HttpExchange ex) throws IOException
	{
		String response = "";
		try {
			BlenderFile bf = new BlenderFile(ex.getRequestBody());
			response = "{\"fileAccept\":\"yes\"}";
			System.out.println(bf.getFile().getAbsolutePath());
		} catch(FileNotFoundException e) {
			response = "{\"fileAccept\":\"no\"}";
			Logger.getLogger().logError("File not found exception when creating blender file");
			e.printStackTrace();
		} catch(IOException e) {
			response = "{\"fileAccept\":\"no\"}";
			Logger.getLogger().logError("IO Excpetion when creating blender file");
			e.printStackTrace();
		}

		
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
	
	private void handleOPTIONS(HttpExchange ex) throws IOException
	{
		Headers headers = ex.getResponseHeaders();
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Methods", "POST, OPTIONS");
        headers.add("Access-Control-Allow-Headers", "Content-Type, Authorization");
        ex.sendResponseHeaders(200, 0);
        ex.getResponseBody().close();
	}

}
