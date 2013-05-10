package biz.vnc.jilter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.sendmail.jilter.JilterHandler;
import biz.vnc.util.SimpleGetopt;


public abstract class JilterServer {
	public abstract JilterHandler allocHandler();

	public int run(SocketAddress endpoint)
	throws IOException, UnknownHostException {
		JilterLog.debug("Opening socket");
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(true);
		JilterLog.debug("Binding to endpoint " + endpoint);
		serverSocketChannel.socket().bind(endpoint);
		JilterLog.debug("Bound to " + serverSocketChannel.socket().getLocalSocketAddress());

		while (true) {
			SocketChannel connection = null;
			try {
				//JilterLog.debug("Going to accept");
				connection = serverSocketChannel.accept();
				JilterLog.debug("Got a connection from "
				+ connection.socket().getInetAddress().getHostAddress());

				//JilterLog.debug("Firing up new thread");
				new Thread(new JilterThread(connection, allocHandler()),
				"Jilter "
				+ connection.socket().getInetAddress()
				.getHostAddress()).start();
				//JilterLog.debug("Thread started");
			} catch (IOException e) {
				JilterLog.error("Unexpected exception",e);
			}
		}
	}

	public static SocketAddress parseSocketAddress(String address)
	throws UnknownHostException {
		Pattern pattern = Pattern.compile("inet\\s*:\\s*(\\d+)\\s*@\\s*(\\S+)");
		Matcher matcher = pattern.matcher(address);

		if (!matcher.matches()) {
			JilterLog.debug("Unrecognized port \"" + address + "\"");
			return null;
		}

		JilterLog.debug("Successfully parsed socket address, port is "
		+ matcher.group(1) + ", host is " + matcher.group(2));
		return new InetSocketAddress(InetAddress.getByName(matcher.group(2)),
		Integer.parseInt(matcher.group(1)));
	}

	public void usage() {
		System.out.println("Usage: -p <port information> -c <handler class> [-v]");
		System.out.println();
		System.out.println(" -p <port information> -- the port to listen on");
		System.out.println(" -v -- turn on verbosity");
		System.out.println();
		System.out.println(" <port information> is of the format \"inet:port@host\"");
	}

	public int run_main(String[] args) {
		SimpleGetopt options = new SimpleGetopt(args, "u:P:p:c:v");
		SocketAddress socketAddress = null;

		while (true) {
			int option = options.nextopt();

			if (option == -1)
				break;

			switch (option) {
				case 'p':
					JilterLog.debug("Socket address specified is " + options.getOptarg());
					try {
						socketAddress = parseSocketAddress(options.getOptarg());
					} catch (UnknownHostException e) {
						JilterLog.error("UnknownHostExecption", e);
						System.err.println("unknown host name. terminating");
						return 2;
					}
					break;
			}
		}

		if ((socketAddress == null)) {
			usage();
			return 1;
		}

		try {
			run(socketAddress);
			return 0;
		} catch (IOException e) {
			JilterLog.error("IOException", e);
			return 1;
		}
	}
}
