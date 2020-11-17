package client;


import common.Message;
import common.Logs;
import common.LetterBox;
import common.PeerList;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.lang.Runnable;
import java.util.LinkedList;
import java.util.Arrays;
import java.io.IOException;

/**
 * Class to manage client connexion
 *
 * @author Marais-Viau
 */
public class PeerService extends Thread {
	private DatagramSocket s = null;
	private PeerList index = null;
	private LetterBox box = null;
	private final static int PACKAGE_LEN = 1024;

	public PeerService(int port, int timeout){
		try {
			this.s = new DatagramSocket(port);
			this.s.setSoTimeout(timeout);
			this.index = PeerList.get();
			this.box = LetterBox.get();
		} catch(SocketException e) {
			System.out.println("Sorry port " + port + " is already bound -> exit.");
			System.exit(1);
		}
	}

	private String removeDot(byte[] buffer) {
		String[] args = new String(buffer).split("\n.\n");
		return (args.length >= 2) ? args[0]:null;
	}

	private Message readWithTimeout() {
		try {
			byte[] buffer = new byte[PACKAGE_LEN];
			DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
			s.receive(inPacket);
			String cleanBuffer = removeDot(buffer);
			if(cleanBuffer == null) {
				Logs.warning("Missing point -> drop");
				return null;
			}
			Message m = Message.stringToMessage(cleanBuffer);
			m.setAddress(inPacket.getAddress(), inPacket.getPort());
			return m;
		} catch(SocketTimeoutException e) {
			Logs.log("Socket timeout -> reading");
		} catch(Exception e) {
			Logs.error("Fatal error -> exception get for reading: " + e);
			System.exit(1);
		}
		return null;
	}

	private long getTimestamp(String time) {
		try {
			return Long.parseLong(time);
		} catch(IllegalArgumentException e) {
			Logs.warning("Can't convert timestamp");
			return -1;
		}
	}

	private void handleAck(Message m, String[] args, long timestamp) {
		Logs.log("Handling ack");
		if(index.addOrUpdate(args[0], null)) {
			Logs.log("Add a new user for handling");
		}
		if(!box.ackMsg(timestamp, args[0])) {
			Logs.warning("Fail to ack message");
		}
	}

	private void handleMsg(Message m, String[] args) {
		Logs.log("Handling msg");
		if(index.addOrUpdate(args[0], m.getAddress())) {
			Logs.log("Someone new is trying to reach you");
		}
		box.insertInLetterBox(args[0], m);
		String[] argsSender = {
			args[0],
			args[1]
		};
		Message respond = new Message(Message.MessageType.MSG_ACK, argsSender, m.getAddress());
		box.addToSendingList(respond);
	}

	private void printArgs(Message m) {
		String [] args = m.getArgs();
		String display = "";
		if(args != null) {
			for(String s : args) {
				display += "|->" + s + "\n";
			}
		}
		Logs.log("Handler receive\n<"+m.getType()+">"+"\n"+display);
	}

	private void handle(Message m) {
		printArgs(m);
		if(m.getType() == Message.MessageType.MSG && m.getArgs().length >= 3 ) {
			String[] args = m.getArgs();
			if(getTimestamp(args[1])== -1) return;
			handleMsg(m, args);
		} else if (m.getType() == Message.MessageType.MSG_ACK && m.getArgs().length == 2) {
			String[] args = m.getArgs();
			long timestamp = getTimestamp(args[1]);
			if(timestamp == -1) return;
			handleAck(m, args, timestamp);
		} else {
			Logs.warning("Unknow message -> drop");
		}
	}

	private void sendMessages() {
		for(Message m : box.getSendingList()) {
			byte[] buffer = m.toNetFormat().getBytes();
			if(buffer.length > PACKAGE_LEN) {
				Logs.warning("Need to truncate message -> shouldn't happen");
				buffer = Arrays.copyOfRange(buffer, 0, PACKAGE_LEN-1);
			}
			InetSocketAddress ipPort = m.getAddress();
			if(ipPort.isUnresolved()) {
				continue;
			}
			InetAddress ip = ipPort.getAddress();
			int port = ipPort.getPort();
			DatagramPacket outPacket = new DatagramPacket(buffer, buffer.length, ip, port);
			try {
				Logs.log("Sending packet: " + outPacket);
				s.send(outPacket);
			} catch(IOException e) {
				Logs.warning("Can't send the message -> next");
			}
		}
	}

	@Override
	public void run() {
		while(true) {
			Message m = readWithTimeout();
			if(m != null) {
				handle(m);
			}
			sendMessages();
		}
	}
}
