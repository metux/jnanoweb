package de.metux.nanoweb.core;

import java.io.IOException;

/**
 * generic interface for request handlers
 *
 * servers call them to do the actual request handling
 */
public interface IHandler {
	/**
	 * handle a request
	 *
	 * @param request	request to be handled
	 * @result		true if the request was properly handled
	 */
	public boolean handle(IRequest request)
	throws IOException;
}
