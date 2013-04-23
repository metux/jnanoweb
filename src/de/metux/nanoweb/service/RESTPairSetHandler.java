package de.metux.nanoweb.service;

import de.metux.nanoweb.core.IHandler;
import de.metux.nanoweb.core.IRequest;
import de.metux.nanoweb.core.StrUtil;
import java.io.IOException;

/**
 * simple REST request handler for checking elements in an simple pair set
 *
 * the first two elements of relative pathname are taken as the parameters
 * A and B for the set lookup.
 *
 * when (A,B) is found in the set, replies with status 200 and body "YES\n",
 * otherwise status 200 and body "NO\n".
 *
 * wrong number of parameters will be replied with status 404, lookup
 * exceptions with status 500.
 */
public class RESTPairSetHandler implements IHandler {
	IPairSet pairset;

	/**
	 * constructor
	 *
	 * @param ps	underlying IPairSet to be queried
	 */
	public RESTPairSetHandler(IPairSet ps) {
		pairset = ps;
	}

	/**
	 * handle the request
	 *
	 * @param request	request to be handled
	 * @result		true if the request could be properly handled
	 */
	public boolean handle(IRequest request)
	throws IOException {
		String[] params = StrUtil.split_path(request.getPath());
		if (params.length<2) {
			request.replyStatus(IRequest.status_not_found, "parameter missing");
			return false;
		}
		if (params.length>2) {
			request.replyStatus(IRequest.status_not_found, "too many paramters");
			return false;
		}

		try
		{
			if (pairset.check(params[0], params[1])) {
				request.replyStatus(IRequest.status_ok, "OK");
				request.replyBody("YES\n");
			} else
			{
				request.replyStatus(IRequest.status_ok, "OK");
				request.replyBody("NO\n");
			}
		} catch (ServiceFailure e) {
			request.replyStatus(IRequest.status_internal_error, "service failure");
		}

		return true;
	}
}
