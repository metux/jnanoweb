package de.metux.nanoweb.daemon;

import de.metux.nanoweb.core.IHandler;
import de.metux.nanoweb.core.IRequest;
import de.metux.nanoweb.core.Request;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * minimalistic multithreaded httpd
 *
 * listener loop runs in called thread, while new worker threads
 * are spawned per request
 */
public class MiniServer {

	/**
	 * internal worker thread class
	 */
	private class ConnectionHandler extends Thread {
		Socket socket;

		public ConnectionHandler(Socket s) {
			socket = s;
		}

		private boolean bad_request(Request request, String comment)
		throws IOException {
			request.replyStatus(IRequest.status_bad_request, "Bad request");
			request.replyFinish();
			info("BAD REQUEST: "+comment);
			socket.close();
			return false;
		}

		public void run() {
			try {
				BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				OutputStream output = socket.getOutputStream();
				Request request = new Request(input, output);

				/** we only support single request per connection **/
				request.replyHeader("Connection", "close");

				{
					/* read the first request line */
					String[] req = input.readLine().split(" ");
					if (req.length != 3) {
						bad_request(request, "wrong number of request line parts");
						return;
					}

					if (!request.setMethod(req[0])) {
						bad_request(request, "unsupported request method");
						return;
					}

					request.setLocation(req[1]);
					request.setProtocol(req[2]);
				}

				/* now read the headers */
				{
					boolean run = true;
					while (run) {
						String hdr = input.readLine();
						if ((hdr == null) || (hdr.equals(""))) {
							run = false;
						} else {
							String[] l = hdr.split(":");
							if (l.length == 2)
								request.addHeader(l[0], l[1]);
						}
					}
				}

				handler.handle(request);
				request.replyFinish();
				socket.close();
			} catch (IOException e) {
				err("IO Error: ", e);
			}
		}
	}

	public MiniServer(int p, IHandler h) {
		port = p;
		handler = h;
	}

	private void err(String msg, Exception e) {
		System.err.println("ERR: "+msg+" "+e.toString());
		e.printStackTrace();
	}

	private void info(String msg) {
		System.err.println("INFO: "+msg);
	}

	private int port;
	private IHandler handler;

	/**
	 * run the http daemon in caller thread
	 * requests are handled in additional threads
	 */
	public void run() {
		ServerSocket serversocket = null;

		try {
			serversocket = new ServerSocket(port);
		} catch (Exception e) {
			err("Failed to bind listener sockt to port "+port, e);
			return;
		}

		while (true) {
			try {
				Socket connectionsocket = serversocket.accept();
				InetAddress client = connectionsocket.getInetAddress();
				info("Connection from: "+client.getHostName());

				ConnectionHandler handler = new ConnectionHandler(connectionsocket);
				handler.start();
			} catch (Exception e) {
				err("Failed to process connection", e);
			}
		}
	}
}
