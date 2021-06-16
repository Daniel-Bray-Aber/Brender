package xyz.danielbray.Brender.RenderNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import xyz.danielbray.Brender.RenderNode.Interface.Server;
import xyz.danielbray.Brender.RenderNode.Utility.ArgumentParser;
import xyz.danielbray.Brender.RenderNode.Utility.Logging.Level;
import xyz.danielbray.Brender.RenderNode.Utility.Logging.Logger;
import xyz.danielbray.Brender.RenderNode.Work.Bucket.BucketInformation;

public class Main {

	public static void main(String args[]) {
		ArgumentParser.getInstance().parseArguments(args);

		String bucketFileInfoLocation = ArgumentParser.getInstance().get("b");

		if (bucketFileInfoLocation == null) {
			Logger.getLogger()
					.logError("System exiting due to no bucket information file specified with -b. Please specify it.");
			return;
		}

		BucketInformation bucketInformation = null;
		try {
			bucketInformation = BucketInformation.buildFromFile(new File(bucketFileInfoLocation));
		} catch (FileNotFoundException e) {
			Logger.getLogger().logError(
					"System exiting due to bucket information file \"" + bucketFileInfoLocation + "\" not existing. ");
			return;
		} catch (IOException e) {
			Logger.getLogger().logError("System exiting due not being able to read bucket information file ("
					+ bucketFileInfoLocation + "). Please make sure the application can read the file.");
			return;
		}

		if (bucketInformation == null) {
			Logger.getLogger().logError("System exiting due to not being able to build bucket infomation from file \""
					+ bucketFileInfoLocation + "\"");
			return;
		}

		InetSocketAddress address = getINetSocketAddressFromArgs();
		if (address == null) {
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
		} catch (IllegalArgumentException e) {
			Logger.getLogger().log(Level.ERROR,
					"Failed to start server invalid InetSocketAddress. This should not have happened);");
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Gets the InetSocketAddress for the http server to bind to. This will use the
	 * -p and --port arguments to choose the port. This will use the -a and
	 * --inetAddress to choose the address.
	 * 
	 * This will return null if there is an error.
	 * 
	 * @return the InetSocketAdress that the http server should bound to.
	 * 
	 */
	private static InetSocketAddress getINetSocketAddressFromArgs() {
		ArgumentParser parser = ArgumentParser.getInstance();

		int port = 6456;
		String addr = "localhost";
		boolean error = false;

		String portArgument = null;
		String portOption = null;
		if (parser.argumentSpecified("p")) {
			portArgument = "-p";
			portOption = parser.get("p");
		} else if (parser.argumentSpecified("-port")) {
			portArgument = "--port";
			portOption = parser.get("-port");
		}
		if (portOption != null) {
			try {
				port = Integer.parseInt(portOption);
			} catch (NumberFormatException e) {
				String message;
				if (portOption.isEmpty()) {
					message = "\"" + portArgument + "\" must have a port after it.";
				} else {
					message = "\"" + portOption + "\" is not a valid port";
				}
				Logger.getLogger().logError(message);
				error = true;
			}
		}

		String inetAddrArgument = null;
		String inetAddrOption = null;
		if (parser.argumentSpecified("a")) {
			inetAddrArgument = "a";
			inetAddrOption = parser.get("a");
		} else if (parser.argumentSpecified("-inetAddress")) {
			inetAddrArgument = "--inetAddress";
			inetAddrOption = parser.get("-inetAddress");
		}
		if (inetAddrOption != null) {
			if (inetAddrOption.isEmpty()) {
				Logger.getLogger().logError("\"" + inetAddrArgument + "\" must have an adderss after it.");
				error = true;
			} else {
				addr = inetAddrArgument;
			}
		}

		InetSocketAddress result = null;
		if (!error) {
			try {
				InetAddress inetAddr = InetAddress.getByName(addr);
				result = new InetSocketAddress(inetAddr, port);
			} catch (UnknownHostException e) {
				Logger.getLogger().logError("Could not resolve: \"" + addr + "\"");
			}
		}
		return result;
	}

}
