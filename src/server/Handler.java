/**
 * Class to handle Server request
 *
 * @authors Marais-Viau
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import common.Index;
import common.Logs;
import common.Message;

public class Handler extends Thread {
	private Socket s = null;
	private BufferedReader in = null;
	private PrintWriter out = null;
	private boolean wantAnExit = false;

	private String addr = null;
	private String name = null;
	private Index index = null;

	public Handler(Index index, Socket s) {
		if (s != null) {
			this.s = s;
			this.addr = s.getInetAddress().toString();
			setInAndOut();
			this.index = index;
			Logs.log("Socket bind to a new thread -> manage the new connection for " + addr);
		} else {
			Logs.error("Socket s is null -> exit the Thread for " + addr);
			wantAnExit = true;
		}
	}

	private void setInAndOut() {
		try {
			this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			this.out = new PrintWriter(s.getOutputStream());
		} catch (IOException e) {
			Logs.error("Can't set input and output flux for -> exit the thread for " + addr);
			wantAnExit = true;
		}
	}

	private void write(Message m) {
		String packet = m.toNetFormat();
		out.print(packet);
		out.flush();
	}

	private Message read() {
		String buffer = "";
		String data = "";
		try {
			while ((data = in.readLine()) != null && !data.equals(".")) {
				buffer += (data + "\n");
			}
			if (data == null) {
				Logs.error("Client has been disconnected for " + addr + " -> closing connection");
				wantAnExit = true;
				return null;
			}
			buffer = buffer.trim();
			return Message.stringToMessage(buffer);
		} catch (IllegalArgumentException e) {
			Logs.warning("Can't Handle the format for " + addr + " -> drop message");
			return null;
		} catch (NullPointerException e) {
			Logs.warning("Write on a close channel for " + addr + " -> drop channel");
			wantAnExit = true;
			return null;
		} catch (IOException e) {
			Logs.warning("An IOException occured on " + addr + " -> skipping");
			return null;
		}
	}

	private void disconnect() {
		if (s != null) {
			try {
				if(name != null) {
					index.removeUser(name);
				}
				s.close();
				Logs.log("Thread socket closed for " + addr);
			} catch (IOException e) {
				Logs.warning("Failed to close the thread socket -> exit thread");
			}
		} else {
			Logs.warning("Thread socket already closed for " + addr);
		}
	}

	private void sendConnect(String[] args, boolean newUser, boolean send) {
		Message msg = null;
		if(send) {
			if (newUser) {
				msg = new Message(Message.MessageType.CONNECT_OK, args);
			} else {
				msg = new Message(Message.MessageType.CONNECT_NEW_USER_OK, args);
			}
			Logs.log("Connection complete with " + name + " on " + addr);
		} else  {
			if(newUser) {
				msg = new Message(Message.MessageType.CONNECT_KO);
			} else {
				msg = new Message(Message.MessageType.CONNECT_NEW_USER_KO);
			}
			Logs.warning("Connection failed with " + addr + " -> retrying with new method");
		}
		write(msg);
	}

	private void connect(Message m) {
		String[] args = m.getArgs();
		boolean send = true, newUser = false;
		if (args == null || args.length != 1 || args[0] == null) {
			send = false;
		} else {
			String [] argSend = { "" };
			if (args[0].charAt(0) == '#') { // ------------ Token
				argSend[0] = args[0].substring(1);
				if(index.isValidToken(argSend[0])) {
					name = index.getUserFromToken(argSend[0]);
					if (name == null) { send = false; }
					index.addIp(addr, name);
				} else { send = false; }
			} else { // ------------------------------------ User
				name = args[0];
				if(index.isValidUser(name)) {
					argSend[0] = index.getToken(name);
					if(argSend[0] == null) { argSend[0] = index.initNewToken(name); }
				} else {
					newUser = true;
					if(index.addUser(name, addr)) {
						argSend[0] = index.getToken(name);
					} else {
						send = false;
					}
				}
			}
			sendConnect(argSend, newUser, send);
		}
	}

	private void handler(Message m) {
		switch (m.getType()) {
			case CONNECT:
				connect(m);
				break;
			case DISCONNECT:
				wantAnExit = true;
				Logs.log("Ask for deconnection for " + addr);
				break;
			default:
				Logs.warning("Unknown header for " + addr + " -> skipping\n" + m);
				break;
		}
	}

	public void run() {
		boolean run = true;
		while (run && !wantAnExit) {
			Message m = read();
			if (m != null) {
				handler(m);
			} else {
				continue;
			}
		}
	disconnect();
	}
}
