package xyz.danielbray.Brender.RenderNode.Utility.Logging;

/**
 * 
 * Does what it says on the tin. It's a singleton logging class.
 * 
 * @author Daniel Bray
 * @since Jun 21
 *
 */
public class Logger {
	
	private static Logger logger;
	
	private Logger() {}
	
	public void log(Level level, String message)
	{
		System.out.print(level.toString());
		System.out.println(": " + message);
	}
	
	public void logError(String message)
	{
		log(Level.ERROR, message);
	}
	
	public static Logger getLogger()
	{
		if(logger == null)
		{
			logger = new Logger();
		}
		return logger;
	}

}
