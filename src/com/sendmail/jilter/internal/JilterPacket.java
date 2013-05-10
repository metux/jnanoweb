/*
 * Copyright (c) 2001-2004 Sendmail, Inc. All Rights Reserved
 */

package com.sendmail.jilter.internal;

import java.io.IOException;
import java.nio.ByteBuffer;
import biz.vnc.jilter.JilterLog;

/**
 */

public class JilterPacket {
	private static final int STATE_COLLECTING_LENGTH = 0;
	private static final int STATE_COLLECTING_COMMAND = 1;
	private static final int STATE_COLLECTING_DATA = 2;
	private static final int STATE_COMPLETED = 3;

	private int currentState = STATE_COLLECTING_LENGTH;
	private int currentLength = 0;
	private int currentLengthLength = 0;
	private int currentCommand = 0;
	private ByteBuffer currentData = null;
	private int currentDataLength = 0;

	private static int unsignedByteToInt(byte b) {
		return (((int) b) & 0x0FF);
	}

	public boolean
	process(
	    ByteBuffer dataBuffer
	)
	throws IOException {
		int bytesToUse = 0;
		boolean spinAgain = false;

		do {
			switch (this.currentState) {
				case STATE_COLLECTING_LENGTH:
					JilterLog.edebug("STATE_COLLECTING_LENGTH");
					bytesToUse = Math.min(4 - this.currentLengthLength, dataBuffer.remaining());

					for (int counter = 0; counter < bytesToUse; ++counter) {
						this.currentLength <<= 8;
						this.currentLength += unsignedByteToInt(dataBuffer.get());
						++this.currentLengthLength;
					}

					if (this.currentLengthLength == 4) {
						currentState = STATE_COLLECTING_COMMAND;
						--this.currentLength;   // Minus one for the command byte
						JilterLog.edebug("Collected length is " + this.currentLength);
						this.currentData = ByteBuffer.allocate(this.currentLength);
					}

					break;

				case STATE_COLLECTING_COMMAND:
					JilterLog.edebug("STATE_COLLECTING_COMMAND");

					this.currentCommand = unsignedByteToInt(dataBuffer.get());
					JilterLog.edebug("Collected command is '" + ((char) this.currentCommand) + "'");

					this.currentState = (this.currentLength == 0) ? STATE_COMPLETED : STATE_COLLECTING_DATA;
					JilterLog.edebug("New state is " + this.currentState);
					break;

				case STATE_COLLECTING_DATA:
					JilterLog.edebug("STATE_COLLECTING_DATA");
					bytesToUse = Math.min(this.currentLength - this.currentDataLength, dataBuffer.remaining());

					this.currentData.put((ByteBuffer) dataBuffer.asReadOnlyBuffer().limit(dataBuffer.position() + bytesToUse));
					dataBuffer.position(dataBuffer.position() + bytesToUse);

					this.currentDataLength += bytesToUse;
					JilterLog.edebug("Found " + bytesToUse + " bytes to apply to data");

					if (this.currentDataLength == this.currentLength) {
						JilterLog.edebug("Collected all the data");

						this.currentData.flip();
						this.currentState = STATE_COMPLETED;
					}

					break;

				case STATE_COMPLETED:
					JilterLog.edebug("STATE_COMPLETED");
					break;

				default:
					JilterLog.error("Unhandled case", new Exception());
					break;
			}
		} while ((dataBuffer.remaining() > 0) && (this.currentState != STATE_COMPLETED));

		return this.currentState == STATE_COMPLETED;
	}

	public int getCommand() {
		return this.currentCommand;
	}

	public ByteBuffer getData() {
		return this.currentData;
	}

	public void reset() {
		this.currentState = STATE_COLLECTING_LENGTH;
		this.currentLength = 0;
		this.currentLengthLength = 0;
		this.currentDataLength = 0;
		this.currentData = null;
	}
}
