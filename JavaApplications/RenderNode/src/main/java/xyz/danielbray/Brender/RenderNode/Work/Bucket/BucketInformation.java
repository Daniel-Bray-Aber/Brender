package xyz.danielbray.Brender.RenderNode.Work.Bucket;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import software.amazon.awssdk.regions.Region;
import xyz.danielbray.Brender.RenderNode.Utility.Logging.Level;
import xyz.danielbray.Brender.RenderNode.Utility.Logging.Logger;

/**
 * 
 * This class is used to represent all the information needed to access a bucker
 * on an s3 server.
 * 
 * @author Daniel Bray
 * @since Jun 21
 *
 */
public class BucketInformation {

	public final String bucketName;
	public final URI endPoint;
	public final String keyID;
	public final String secretKey;
	public final Region region;

	/**
	 * 
	 * Does the same as the other constructor of the bucket information, except it
	 * sets the region by default to Region.EU_CENTRAL_1
	 * 
	 * This creates a bucket information object containing the information passed
	 * in. The variables are copied into public variables in the this class of the
	 * same name.
	 * 
	 * 
	 * @param bucketName The name of the bucket.
	 * @param endpoint   The endpoint that the bucket this exists at. (WARNING: this
	 *                   needs to have the scheme otherwise it will cause a crash
	 *                   when this is used to connect to a bucket)
	 * @param keyID      This is the key id for the bucket
	 * @param secretKey  This is the secret key for the bucket
	 */
	public BucketInformation(String bucketName, URI endpoint, String keyID, String secretKey) {
		this(bucketName, endpoint, keyID, secretKey, Region.EU_CENTRAL_1);
	}

	/**
	 * 
	 * This creates a bucket information object containing the information passed
	 * in. The variables are copied into public variables in the this class of the
	 * same name.
	 * 
	 * @param bucketName The name of the bucket.
	 * @param endpoint   The endpoint that the bucket this exists at. (WARNING: this
	 *                   needs to have the scheme otherwise it will cause a crash
	 *                   when this is used to connect to a bucket)
	 * @param keyID      This is the key id for the bucket
	 * @param secretKey  This is the secret key for the bucket
	 * @param region     This is region the bucket exists in.
	 */
	public BucketInformation(String bucketName, URI endpoint, String keyID, String secretKey, Region region) {
		this.bucketName = bucketName;
		this.endPoint = endpoint;
		this.keyID = keyID;
		this.secretKey = secretKey;
		this.region = region;
	}

	/**
	 * 
	 * This will read in a file containing the information necesary to make a
	 * bucketInformation object.
	 * 
	 * TODO: This needs to support regions eventually. It is currently being skipped
	 * due to the fact in testing I found that it did not make a difference with
	 * linode buckets.
	 * 
	 * @param location the file to read from.
	 * @return An bucket information object that contains the information held in
	 *         the file.
	 * @throws FileNotFoundException thrown when the file pointed to by the
	 *                               parameter does not exist
	 * @throws IOException           thrown when there is an error reading the file.
	 */
	public static BucketInformation buildFromFile(File location) throws FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(location));
		String bucketName = null, keyID = null, secretKey = null;
		URI endpoint = null;

		String line;
		while ((line = br.readLine()) != null) {
			String tokens[] = line.split(" ");
			if (tokens.length == 2) {
				switch (tokens[0].toLowerCase()) {
				case "bucketname":
					bucketName = tokens[1];
					break;
				case "endpoint":
					try {
						endpoint = new URI(tokens[1]);
					} catch (URISyntaxException e) {
						Logger.getLogger().log(Level.WARNING,
								"When loading a bucket information from a file, URI was not correctly formatted: \""
										+ tokens[1] + "\"");
						e.printStackTrace();
					}
					break;
				case "keyid":
					keyID = tokens[1];
					break;
				case "secretkey":
					secretKey = tokens[1];
					break;
				default:
					break;
				}
			}
		}
		br.close();

		if (bucketName != null && keyID != null && secretKey != null && endpoint != null) {
			return new BucketInformation(bucketName, endpoint, keyID, secretKey);
		} else {
			return null;
		}

	}

}
