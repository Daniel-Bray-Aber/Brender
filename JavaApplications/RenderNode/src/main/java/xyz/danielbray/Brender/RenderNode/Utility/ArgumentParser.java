package xyz.danielbray.Brender.RenderNode.Utility;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * This class is for parsing input arguments, and then providing access to them
 * via a singleton.
 * 
 * When getting the options for
 * 
 * @author Daniel Bray
 * @since Jun 21
 *
 */
public class ArgumentParser {

	private static ArgumentParser instance;

	private Map<String, String> arguments = new HashMap<String, String>();

	private ArgumentParser() {}

	/**
	 * 
	 * This will will return the option of for the argument specified in arguments
	 * name.
	 * 
	 * 
	 * @param argumentName the name of the argument to find the value for
	 * @return the option assosiated with the argument or an empty string if there
	 *         is none, or null if it was never specified
	 * 
	 */
	public String get(String argumentName) {
		return arguments.get(argumentName);
	}

	/**
	 * 
	 * Returns whether or not the argument was specified.
	 * 
	 * @param argumentName the argument to test
	 * @return true if the argument was specified or false if not.
	 */
	public boolean argumentSpecified(String argumentName) {
		return arguments.containsKey(argumentName);
	}

	/**
	 * 
	 * Will parse the args by looking for an arguments name that is preceded by a
	 * '-'. If it finds an argument it will look for a matching option by checking
	 * if the next value starts with '-' if it does not it is used as the option for
	 * the argument.
	 * 
	 * If it finds an argument without '-' at the beginning this is ignored.
	 * 
	 * If it finds an argument without an option the option of the argument is set
	 * to an empty string.
	 * 
	 * 
	 * @param args The list of arguments to parse.
	 * 
	 */
	public void parseArguments(String args[]) {
		if (args != null && args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				// Test if the argument name is correctly formatted.
				if (args[i].length() > 1 && args[i].getBytes()[0] == '-') {
					// Get the name
					String argumentName = args[i].substring(1, args[i].length());
					String option = "";
					// See if there is a matching option
					if (args[i + 1].getBytes()[0] != '-') {
						option = args[i + 1];
						i++;
					}
					// Store arguments in the map
					arguments.put(argumentName, option);
				}
			}
		}

	}

	/**
	 * 
	 * Returns the instance of the ArgumentParser class.
	 * 
	 * @return the instance of the ArgumentParser class.
	 */
	public static ArgumentParser getInstance() {
		if (instance == null) {
			instance = new ArgumentParser();
		}
		return instance;
	}

}
