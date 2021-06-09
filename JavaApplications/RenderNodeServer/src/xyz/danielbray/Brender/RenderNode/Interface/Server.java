package xyz.danielbray.Brender.RenderNode.Interface;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import xyz.danielbray.Brender.RenderNode.Utility.Logging.Level;
import xyz.danielbray.Brender.RenderNode.Utility.Logging.Logger;

/**
 * 
 * This class is the main server class for the application. It will handle all
 * the https requests it gets.
 * 
 * @author Daniel Bray
 * @since Jun 21
 *
 */
public class Server {

	HttpServer httpServer;

	/**
	 * 
	 * Creates the server on the interface and port specified in the
	 * InetSocketAddress passed in.
	 * 
	 * @param inetSocAddr InetSocketAddress that contains the interface address and
	 *                    port to start the server on.
	 * @throws IOException Thrown when the server cant start on the interface
	 *                     address and prot that inetsocaddr contains
	 */
	public Server(InetSocketAddress inetSocAddr) throws IOException, IllegalArgumentException {
		if (inetSocAddr == null) {
			throw new IllegalArgumentException("Starting sever with null inetsocaddr");
		}

		// the 0 makes the server choose the sytem default.
		httpServer = HttpServer.create(inetSocAddr, 0);
		httpServer.createContext("/status", new ServerStatus());
		httpServer.createContext("/close", new CloseHandler());
		httpServer.createContext("/upload", new FileAccepter());
		httpServer.setExecutor(null);
		httpServer.start();
	}

	/**
	 * 
	 * This class is the context which will close the server.
	 * 
	 * 
	 * @author Daniel Bray
	 * @since Jun 21
	 *
	 */
	private class CloseHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange ex) throws IOException {
			String response = "{\"closing\":\"true\"}";
			Headers headers = ex.getResponseHeaders();
			headers.set("Content-Type", "application/javascript");
			headers.add("Access-Control-Allow-Origin", "*");
			headers.add("Access-Control-Allow-Methods", "GET, OPTIONS");
	        headers.add("Access-Control-Allow-Headers", "Content-Type,Authorization");
			ex.sendResponseHeaders(200, response.length());
			OutputStream os = ex.getResponseBody();
			os.write(response.getBytes());
			os.close();
			
			Logger.getLogger().log(Level.INFO, "Closing server due to web request from: " + ex.getRemoteAddress().toString());

			/* 
			 * 
			 * Has to be done on a seperate thread as stop is blocking but the it won't
			 * unblock untill the server closes it's connections, but it won't close it's
			 * connections untill this handler has finished.
			 * 
			 */
			new Thread(() -> {
				httpServer.stop(0);
			}).start();
		}
	}

}
