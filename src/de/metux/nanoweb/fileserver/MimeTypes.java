package de.metux.nanoweb.fileserver;

/* import javax.activation.MimetypesFileTypeMap; */

/**
 * very trivial mimetype detection
 *
 */
public class MimeTypes {

	/**
	 * get mimetype by filename (looks at the suffix)
	 *
	 * @param filename	filename to detect mimetype from
	 * @result		detected mimetype
	 */
	public static String getMimeType(String filename) {

		/* String mimetype = new MimetypesFileTypeMap().getContentType(path); */

		if (filename == null)
			return "application/octet-stream";

		filename = filename.toLowerCase();

		if (filename.endsWith(".htm") || filename.endsWith(".html"))
			return "text/html";

		if (filename.endsWith(".gif"))
			return "image/gif";

		if (filename.endsWith(".jpg"))
			return "image/jpeg";

		if (filename.endsWith(".css"))
			return "text/css";

		if (filename.endsWith(".xml"))
			return "application/xml";

		if (filename.endsWith(".js"))
			return "application/x-javascript";

		return "text/plain";
	}
}
