package de.metux.nanoweb.fileserver;

import de.metux.nanoweb.core.IHandler;
import de.metux.nanoweb.core.IRequest;
import de.metux.nanoweb.core.StrUtil;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * simple fileserver handler.
 *
 * just looks for the requested file request path under the
 * root given in constructor
 */
public class FileServerHandler implements IHandler {

	private String root;

	/**
	 * constructor
	 *
	 * @param r	file server root
	 */
	public FileServerHandler(String r) {
		root = r;
	}

	/**
	 * handle file not found reply - may be overwritten by derived classes
	 *
	 * @param request	the request to be handled
	 * @param path		requested path
	 * @param localpath	translated local path
	 * @result		true if the request successfully handled
	 */
	public boolean reply_fileNotFound(IRequest request, String localpath)
	throws IOException {
		System.err.println("File not found: "+request.getPath()+" ("+localpath+")");
		request.replyStatus(IRequest.status_not_found, "File not found");
		request.replyBody("File not found: "+request.getPath()+"\n");
		return true;
	}

	/**
	 * handle IO error reply - may be overwritten by derived classes
	 *
	 * @param request	the request to be handled
	 * @param path		requested path
	 * @param localpath	translated local path
	 * @result		true if the request successfully handled
	 */
	public boolean reply_IOError(IRequest request, String localpath)
	throws IOException {
		System.err.println("IO error: "+request.getPath()+" ("+localpath+")");
		request.replyStatus(IRequest.status_not_found, "IO error");
		request.replyBody("IO error: "+request.getPath()+"\n");
		return true;
	}

	/**
	 * handle the request
	 *
	 * @param request	the request to be handled
	 * @result		returns true when request had been handled
	 */
	public boolean handle(IRequest request)
	throws IOException {
		String path = root+"/"+StrUtil.safe_normalize_path(request.getPath());
		System.err.println("Relative path: "+path);
		System.err.println("Root: "+root);

		try {
			File file = new File(path);
			long size = file.length();

			String mimetype = MimeTypes.getMimeType(path);

			System.err.println("Content-Type: "+mimetype);
			System.err.println("Content-Size: "+size);

			FileInputStream is = new FileInputStream(path);

			request.replyHeader(IRequest.header_content_type, mimetype);
			request.replyHeader(IRequest.header_content_length, String.valueOf(size));

			byte buffer[] = new byte[4096];
			int sz;
			while ((sz = is.read(buffer))>0) {
				System.err.println("GOT "+sz+" BYTES");
				request.replyBody(buffer, sz);
			}

		} catch (FileNotFoundException e) {
			reply_fileNotFound(request, path);
		} catch (IOException e) {
			reply_IOError(request, path);
		}

		return true;
	}
}
