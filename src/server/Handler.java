/**
 * Class to handle Server request
 *
 * @authors Marais-Viau
 */
package server;

import common.Logs;

import java.io.IOException;
import java.net.Socket;

public class Handler extends Thread {
	private Socket s = null;
	private String addr = "NULL";

	public Handler(Socket s) {
		if (s != null) {
			this.s = s;
			this.addr = s.getInetAddress().toString();
			Logs.log("Socket bind to a new thread -> manage the new connection for " + addr);
		} else {
			Logs.error("Socket s is null -> exit the Thread for " + addr);
			System.exit(1);
		}
	}

	private void disconnect() {
		if (s != null) {
			try {
				s.close();
				Logs.log("Thread socket closed for " + addr);
			} catch (IOException e) {
				Logs.warning("Failed to close the thread socket -> exit thread");
			}
		} else {
			Logs.warning("Thread socket already closed for " + addr);
		}
	}

	private boolean handler() {
		return false;
	}

	public void run() {
		Logs.log("Thread server is running");
		boolean run = true;
		while (run) {
			run = handler();
		}
		disconnect();
		System.exit(0);
	}
}
