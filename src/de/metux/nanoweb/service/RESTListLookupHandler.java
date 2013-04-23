package de.metux.nanoweb.service;

import de.metux.nanoweb.core.IHandler;
import de.metux.nanoweb.core.IRequest;
import java.io.IOException;

/**
 * simple REST request handler for fetching simple string lists
 *
 * relative pathname (w/o leading "/") is taken as key for the
 * query to the underlying IListLookup
 *
 * if list was not found, replies with 404, otherwise 200 and
 * list elements - separated by linefeed ("\n") - in the body
 *
 * in case of service failure exception, replies with 500
 */
public class RESTListLookupHandler implements IHandler {
	IListLookup lookup;

	/**
	 * constructor with given IListLookup backend
	 *
	 * @param ll	IListLookup backend to be queried
	 */
	public RESTListLookupHandler(IListLookup ll) {
		lookup = ll;
	}

	/**
	 * handle the request
	 *
	 * @param request	request to be handled
	 * @result		true if the request could be handled
	 */
	public boolean handle(IRequest request)
	throws IOException {

		String param = request.getPath();
		if (param.startsWith("/"))
			param = param.substring(1);

		try
		{
			String[] list = lookup.lookup(param);
			if (list == null) {
				request.replyStatus(IRequest.status_not_found, "NOT FOUND");
			} else
			{
				request.replyStatus(IRequest.status_ok, "OK "+list.length);
				for (int x=0; x<list.length; x++)
					request.replyBody(list[x]+"\n");
			}
		} catch (ServiceFailure e) {
			request.replyStatus(IRequest.status_internal_error, "service failure");
		}

		return true;
	}
}
