package xyz.danielbray.Brender.RenderNode.Work;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;

/**
 * 
 * This class will store a representation of blenderfile
 * 
 * @author Daniel Bray
 * @since Jun 21
 *
 */
public class BlenderFile {

	private final File file;

	/**
	 * 
	 * This constructor takes in an input stream which should contain the raw bytes
	 * of blend file. It will save them to file.
	 * 
	 * @param is Input stream containing raw byte for a blend file.
	 */
	public BlenderFile(InputStream is) throws IOException, FileNotFoundException {
		String fileName = LocalDateTime.now().toString();
		file = new File(fileName + ".blend");
		OutputStream os = new FileOutputStream(file);

		int i;
		while ((i = is.read()) != -1) {
			os.write(i);
		}
		os.close();
	}

	/**
	 * 
	 * This represenets a blend file at the location given by the file passed in.
	 * 
	 * @param f The locaiton of blend file.
	 */
	public BlenderFile(File f) {
		file = f;
	}

	/**
	 * 
	 * Returns a File with the location of the blend file this object represents.
	 * 
	 * @return File with location of blend file this object represents.
	 */
	public File getFile() {
		return file;
	}

}
