package de.metux.nanoweb.mount;

import java.io.IOException;
import de.metux.nanoweb.core.IHandler;
import de.metux.nanoweb.core.IRequest;

/**
 * simple mounting request handler
 *
 * utilizes MountTable for mountpoint management and path translation
 * passes all requests to the responsible handlers given in mounttable
 */
public class MountingHandler implements IHandler {
	MountTable mounttable;

	/**
	 * constructor using existing mounttable
	 *
	 * @param mt	mounttable to be used for translation
	 */
	public MountingHandler(MountTable mt) {
		mounttable = mt;
	}

	/**
	 * constructor w/o existing mounttable
	 *
	 * @param root		the (catchall) handler for root path ("/")
	 * @param entries	(initial) mounttable entries
	 */
	public MountingHandler(IHandler root, MountEntry entries[]) {
		mounttable = new MountTable(root, entries);
	}

	/**
	 * do mount processing (path translation) and dispatch request to the
	 * actually responsible request handler
	 *
	 * @param request	request to be handled
	 * @result		true if the request was handled properly
	 */
	public boolean handle(IRequest request)
	throws IOException {
		/** we mount /prefix/ to / **/

		MountTable.Mapping mapping = mounttable.find(request.getPath());

		if (mapping == null) {
			request.replyStatus(IRequest.status_internal_error, "Mapper configuration error");
			request.replyBody("Mapper configuration error\n");
			return false;
		}

		request.setAttribute(IRequest.attribute_path, mapping.path);
		request.setAttribute(IRequest.attribute_root, mapping.root);

		return mapping.handler.handle(request);
	}
}
