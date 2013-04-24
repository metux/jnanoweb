package de.metux.nanoweb.service;

import de.metux.nanoweb.core.IHandler;
import de.metux.nanoweb.core.IRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

/**
 * simple REST request handler for property list lookups
 *
 * the relative pathname (w/o leading "/") is taken as lookup key
 * to the underlying IPropertiesLookup
 *
 * if the list was found, replies with status 200 and the properties
 * as <key>": "+value lines (delimited by \n)
 * otherwise status 404 is replied.
 *
 * on service exception, replies with status 500
 */
public class RESTPropertiesLookupHandler implements IHandler {
	IPropertiesLookup lookup;

	/**
	 * constructor
	 *
	 * @param ll	the underlying IPropertiesListLookup backend
	 */
	public RESTPropertiesLookupHandler(IPropertiesLookup ll) {
		lookup = ll;
	}

	/**
	 * handle the request
	 *
	 * @param request	the request to be handled
	 * @result		true if the request was properly handled
	 */
	public boolean handle(IRequest request)
	throws IOException {

		String param = request.getPath();
		if (param.startsWith("/"))
			param = param.substring(1);

		try
		{
			Properties list = lookup.lookup(param);
			if (list == null) {
				request.replyStatus(IRequest.status_not_found, "NOT FOUND");
			} else
			{
				request.replyStatus(IRequest.status_ok, "OK "+list.size());
				Enumeration<?> e = list.propertyNames();
				while (e.hasMoreElements()) {
					String key = (String) e.nextElement();
					request.replyBody(key+":"+list.getProperty(key)+"\n");
				}
			}
		} catch (ServiceFailure e) {
			request.replyStatus(IRequest.status_internal_error, "service failure");
		}

		return true;
	}
}
