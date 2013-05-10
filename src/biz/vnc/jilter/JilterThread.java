package biz.vnc.jilter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import com.sendmail.jilter.JilterHandler;
import com.sendmail.jilter.JilterProcessor;


public class JilterThread implements Runnable {
	private SocketChannel socket = null;
	private JilterProcessor processor = null;

	/**
	 * Constructor.
	 *
	 * @param socket
	 *            the incoming socket from the MTA.
	 * @param handler
	 *            the handler containing callbacks for the milter protocol.
	 */
	public JilterThread(SocketChannel socket, JilterHandler handler)
	throws IOException {
		this.socket = socket;
		this.socket.configureBlocking(true);
		this.processor = new JilterProcessor(handler);
	}

	public void run() {
		ByteBuffer dataBuffer = ByteBuffer.allocateDirect(4096);
		try {
			while (this.processor.process(this.socket, (ByteBuffer) dataBuffer.flip())) {
				dataBuffer.compact();
				//JilterLog.debug("Going to read");
				if (this.socket.read(dataBuffer) == -1) {
					//JilterLog.debug("socket reports EOF, exiting read loop");
					break;
				}
				//JilterLog.debug("Back from read");
			}
		} catch (IOException e) {
			JilterLog.error("Unexpected exception, connection will be closed", e);
		}
		finally {
			//JilterLog.debug("Closing processor");
			this.processor.close();
			//JilterLog.debug("Processor closed");
			try {
				//JilterLog.debug("Closing socket");
				this.socket.close();
				//JilterLog.debug("Socket closed");
			} catch (IOException e) {
				JilterLog.error("Unexpected exception", e);
			}
		}
	}
}
