package common;

import common.Message;
import java.util.LinkedList;
import java.util.TreeMap;


/**
 * Class to manage in and out Messages
 *
 * @author Marais-Viau
 */
public class LetterBox {
	private static LetterBox box                                = null;
	private TreeMap<String, LinkedList<Message>> received       = null;
	private TreeMap<Long, TreeMap<String, MessageSender>>  sent = null;
	private final static int MAX_ATTEMPT                        = 3;

	private LetterBox() {
		this.received = new TreeMap<String, LinkedList<Message>>();
		this.sent = new TreeMap<Long, TreeMap<String, MessageSender>>();
	}
	
	/**
	 * Create or get a unique instance of LetterBox
	 *
	 * @return a unique LetterBox
	 */
	public static synchronized LetterBox get() {
		if(box == null) {
			box = new LetterBox();
		}
		return box;
	}

	/**
	 * Adds a Message to someone into the sending list
	 *
	 * @param dest who you are trying to contact
	 * @param m the message sent
	 * @return true if it adds it with succeed
	 */
	public synchronized boolean addToSendingList(String dest, Message m) {
		String[] args = m.getArgs();
		if(args != null && args.length >= 2) {
			try {
				long timestamp = Long.parseLong(args[1]);
				TreeMap<String, MessageSender> sendingList = sent.get(timestamp);
				if(sendingList == null) {
					sendingList = new TreeMap<String, MessageSender>();
					sent.put(timestamp, sendingList);
				}
				sendingList.put(dest, new MessageSender(m));
				Logs.log("Insert new message -> Box)");
				return true;
			} catch(NumberFormatException e) {
				Logs.warning("Can't parse message timestamp.");
			}
		}
		return false;
	}

	/**
	 * Get the list of the messages available for sending
	 *
	 * @return the list containing all of the message
	 */
	public synchronized LinkedList<Message> getSendingList() {
		long timestamp = System.currentTimeMillis();
		LinkedList<Message> toSend = new LinkedList<Message>();
		for( long timestampKey : sent.keySet()) {
			TreeMap<String, MessageSender> messages = sent.get(timestampKey);
			if(messages != null) {
				for(String dest : messages.keySet()) {
					MessageSender message = messages.get(dest);
					if(message.next < timestamp) {
						toSend.add(message.message);
					}
					message.updateAttempt();
					if(message.attempt >= MAX_ATTEMPT) {
						messages.remove(dest);
						continue;
					}
				}
			}
			if(messages == null || messages.size() <= 0) {
				sent.remove(timestampKey);
			}
		}
		return toSend;
	}


	/**
	 * Removes a message from the list of messages to send
	 *
	 * @param timestamp the date when the message was first sent
	 * @param addr the addr where it has been sent
	 * @return true if it acks the message else false
	 */
	public synchronized boolean ackMsg(Long timestamp, String dest) {
		TreeMap<String, MessageSender> messages = sent.get(timestamp);
		if(messages != null) {
			MessageSender sender = messages.get(dest);
			if(sender != null) {
				messages.remove(dest);
				if(messages.size() <= 0) {
					sent.remove(timestamp);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Inserts a message received into the letter box
	 *
	 * @param src the source from whom you received the message
	 * @param m the message you received
	 */
	public synchronized void insertInLetterBox(String src, Message m) {
		LinkedList<Message> messages = received.get(src);
		if(messages == null) {
			messages = new LinkedList<Message>();
			received.put(src, messages);
		}
		messages.add(m);
	}

	/**
	 * Gets the list of the message for a certain user
	 *
	 * @param sender the name of the person who sent the messages
	 * @return the list of all the messages received, return null if empty
	 */
	public synchronized LinkedList<Message> getNewMsgFor(String sender) {
		LinkedList<Message> messages = received.get(sender);
		received.remove(sender);
		return messages;
	}

	private class MessageSender {
		int attempt = 0;
		long next = 0;
		Message message = null;

		MessageSender(Message m) {
			this.attempt = 0;
			this.next = System.currentTimeMillis();
			this.message = m;
		}

		void updateAttempt() {
			this.attempt++;
			this.next = System.currentTimeMillis() + (attempt*2);
		}
	}

}
