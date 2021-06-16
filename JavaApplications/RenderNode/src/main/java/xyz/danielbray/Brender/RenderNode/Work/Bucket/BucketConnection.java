package xyz.danielbray.Brender.RenderNode.Work.Bucket;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import xyz.danielbray.Brender.RenderNode.Utility.Logging.Level;
import xyz.danielbray.Brender.RenderNode.Utility.Logging.Logger;

/**
 * 
 * This class represents a connection to a bucket.
 * 
 * Please rember to call close on this class when you are done with it.
 * 
 * @author Daniel Bray
 * @since Jun 21
 *
 */
public class BucketConnection {

	private final S3Client s3;
	private final BucketInformation bucketInformation;

	/**
	 * 
	 * This creates a bucket connection object from bucket information object.
	 * 
	 * This does not verify that the connection will work at all though.
	 * 
	 * @param bucketInformation information about the bucket this should connect to.
	 * 
	 */
	public BucketConnection(BucketInformation bucketInformation) {
		this.bucketInformation = bucketInformation;
		AwsCredentials credentials = AwsBasicCredentials.create(bucketInformation.keyID, bucketInformation.secretKey);
		s3 = S3Client.builder().credentialsProvider(StaticCredentialsProvider.create(credentials))
				.region(bucketInformation.region).endpointOverride(bucketInformation.endPoint).build();
	}

	/**
	 * 
	 * This get a file at the location specified in bucket. This path is relative to
	 * the bucket, not to the local filesystem.
	 * 
	 * TODO: make this say why it can't get the file if it can't
	 * 
	 * @param bucketPath the path on the bucket to the file to retreiv.
	 * @return the bytes contained in the file, or null if it wasn't able to get the
	 *         file.
	 */
	public byte[] getFile(File bucketPath) {
		GetObjectRequest objectRequest = GetObjectRequest.builder().key(bucketPath.getPath())
				.bucket(bucketInformation.bucketName).build();
		try {
			ResponseBytes<GetObjectResponse> objectBytes = s3.getObjectAsBytes(objectRequest);
			return objectBytes.asByteArray();
		} catch (S3Exception e) {
			Logger.getLogger().log(Level.WARNING, "Failed to get object \"" + bucketPath.getPath()
					+ "\" from bucket: \"" + bucketInformation.bucketName + "\"");
			return null;
		}
	}

	/**
	 * 
	 * This will upload a file to the bucket. The file it will upload is the one
	 * located at the location localPath points to, and it will upload to the
	 * location pointed to by bucketPath on the object bucket.
	 * 
	 * TODO: make this explain why it fails.
	 * 
	 * @param localPath the path to the file to upload
	 * @param bucketPath the destination file on the object server.
	 * @return true if the file was uploaded, false if it wasn't for any reason.
	 */
	public boolean putFile(File localPath, File bucketPath) {
		byte[] bytes = null;
		FileInputStream fis = null;

		boolean success = true;

		try {
			bytes = new byte[(int) localPath.length()];
			fis = new FileInputStream(localPath);
			fis.read(bytes);
		} catch (IOException e) {
			Logger.getLogger().log(Level.WARNING,
					"Failed to get file: \"" + localPath.getPath() + "\" from disk when uploading.");
			success = false;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (success) {
			PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketInformation.bucketName)
					.key(bucketPath.getPath()).build();

			try {
				s3.putObject(putObjectRequest, RequestBody.fromBytes(bytes));
				// TODO: should probably check if the request works.
			} catch (S3Exception e) {
				success = false;
			}
		}

		return success;

	}

	public void close() {
		s3.close();
	}

}
