package xyz.danielbray.Brender.RenderNode;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import xyz.danielbray.Brender.RenderNode.Interface.Server;
import xyz.danielbray.Brender.RenderNode.Utility.Logging.Level;
import xyz.danielbray.Brender.RenderNode.Utility.Logging.Logger;

public class Main {

	public static void main(String args[]) {
		InetSocketAddress address = processArgs(args);
		if(address == null)
		{
			Logger.getLogger().log(Level.ERROR, "System exiting due to argument processing error.");
			return;
		}
		// Start the server.
		Logger.getLogger().log(Level.INFO, "Starting render node server on: " + address);
		try {
			new Server(address);
		} catch (IOException e) {
			Logger.getLogger().log(Level.ERROR, "Failed to start server");
			e.printStackTrace();
		} catch(IllegalArgumentException e) {
			Logger.getLogger().log(Level.ERROR, "Failed to start server invalid InetSocketAddress. This should not have happened);");
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Process argument array passed in to produce
	 * an InetSocketAddress address. Which should be used
	 * to initiliase a http server.
	 * 
	 * Will return null if there is an invalid argument,
	 * invalid value, or if it can't get local the local
	 * host address when leaving the -a parameter unused.
	 * 
	 * @param args the arguments to process
	 * @return InetSocketAddressto initialise a http server with or null if there was an error.
	 * 
	 */
	private static InetSocketAddress processArgs(String args[]) {
		int port = 6456;
		InetAddress addr = null;
		boolean error = false;

		// If there are arguments to process, process them.
		if (args.length != 0) {
			for (int i = 0; i < args.length && !error; i++) {
				switch (args[i]) {
				// Process the port argument
				case "-p":
				case "--port":
					i++;
					if (i < args.length) {
						try {
							port = Integer.parseInt(args[i]);
						} catch (NumberFormatException e) {
							Logger.getLogger().logError("Argument: \"" + args[i] + "\" is not a valid port numer.");
							error = true;
						}
					} else {
						Logger.getLogger().logError("\"" + args[i - 1] + "\" must have a port after it.");
						error = true;
					}
					break;

				// Process the interface address option
				case "-a":
				case "--inetAddress":
					i++;
					if (i < args.length) {
						try {
							addr = InetAddress.getByName(args[i]);
						} catch (UnknownHostException e) {
							Logger.getLogger().logError("Argument: \"" + args[i] + "\" is not a valid inetaddress.");
							error = true;
						}

					} else {
						Logger.getLogger().logError("\"" + args[i - 1] + "\" must have a port after it.");
						error = true;
					}
					break;

				// Process invalid option
				default:
					Logger.getLogger().logError("\"" + args[i] + "\" is not a valid option.");
					error = true;
					break;
				} // switch(args[i])
			}
		} // if( args.lenght != 0)

		// Return the values gotten, or null if there's an error.
		if (!error) {
			if (addr == null) {
				try {
					addr = InetAddress.getByName("localhost");
				} catch (UnknownHostException e) {
					Logger.getLogger().logError("Couldn't get local host inetadress");
					e.printStackTrace();
					return null;
				}
			}

			return new InetSocketAddress(addr, port);
		} else {
			return null;
		}

	} // prcessArgs

}
