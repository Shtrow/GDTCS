package client;

import java.util.Arrays;
import java.util.Scanner;
import java.net.InetSocketAddress;
import java.lang.Long;

import common.PeerList;
import common.LetterBox;
import common.Message;
import common.Logs;

/**
 * Controller, manage the commandline
 *
 * @author Marais-Viau
 */
public class Controller implements Runnable {
	private final DataProvider dataProvider;
	private final PeerList ipBook;
	private final LetterBox box;
	private final Runnable missingArg = () -> System.out.println("Missing arguments");

	/**
	 * Constructor
	 *
	 * @param dataProvider the service that handle the data
	 */
	public Controller(DataProvider dataProvider) {
		this.dataProvider = dataProvider;
		this.ipBook = PeerList.get();
		this.box = LetterBox.get();
	}

	private void header() {
		String head = "********************************\n" + "* Good Duck Transport Protocol *\n"
				+ "*        Client Utility        *\n" + "*       By Marais - Viau       *\n"
				+ "********************************\n";
		System.out.println(head);
	}

	@Override
	public void run() {
		boolean run = true;
		header();
		Scanner scanner = new Scanner(System.in);
		while (run) {
			System.out.flush();
			System.out.print(">> ");
			System.out.flush();
              String commandString = scanner.nextLine();
			Runnable command = parse(commandString);
			command.run();
		}
		scanner.close();
	}

	/**
	 * Parse the command line
	 *
	 * @param command the command send from the terminal
	 */
	public Runnable parse(String command) {
		String[] tokens = command.split("\\s+");
		if (tokens.length < 1)
			return () -> {
			};
		switch (tokens[0]) {
			case "own":
				return dataProvider::getMyAn;
			case "connect":
				if (tokens.length < 2) {
					return dataProvider::connect;
				} else {
					return () -> {
						dataProvider.connect(tokens[1]);
					};
				}
			case "delete":
				if (tokens.length < 2) {
					return missingArg;
				} else {
					return () -> {
						dataProvider.deleteAnc(tokens[1]);
					};
				}
			case "post":
				if (tokens.length < 5)
					return () -> dataProvider.postAnc(createAnnonceHelper(true));
				else {
					return (() -> dataProvider.postAnc(Arrays.copyOfRange(tokens, 1, tokens.length)));
				}
			case "update":
				if (tokens.length == 2)
					return () -> {
						String[] l = new String[5];
						l[0] = tokens[1];
						String[] b = createAnnonceHelper(false);
						System.arraycopy(b, 0, l, 1, 4);
						dataProvider.updateAnc(l);
					};
				else if (tokens.length < 5)
					return missingArg;
				else {
					return (() -> dataProvider.updateAnc(Arrays.copyOfRange(tokens, 1, tokens.length)));
				}
			case "exit":
				return dataProvider::disconnect;
			case "echo":
				return () -> System.out.println(command);
			case "domains":
				return dataProvider::getDomains;
			case "ancs":
				return () -> dataProvider.getProductByDomain(tokens[1]);
			case "ip":
				return () -> dataProvider.requestIP(tokens[1]);
			case "send_msg_to":
				return () -> sendMsg(false, tokens);
			case "send_msg_for":
				return () -> sendMsg(true, tokens);
			case "help":
				return this::displayHelp;
			default:
				return () -> System.out.println("Command not found");
		}

	}

	private void displayHelp() {
		int indentSize = 20;
		String s = "" + "- %" + -indentSize + "s Reconnect to the server\n" + "- %" + -indentSize + "s Quite GTDT Client\n"
				+ "- %" + -indentSize + "s Display available domains\n" + "- %" + -indentSize
				+ "s Display all \"annonces\" from DOMAIN\n" + "- %" + -indentSize + "s Request your \"annonces\"\n" + "- %"
				+ -indentSize + "s Post an \"annonces\" (enter interactive mode)\n" + "- %" + -indentSize
				+ "s Update an \"annonces\"(enter interactive mode)\n" + "- %" + -indentSize
				+ "s Request the ip of the \"annonces\" owner\n" + "- %" + -indentSize + "s Delete an \"annonces\"\n";
		System.out.printf(s, "connect", "exit", "domains", "ancs [DOMAIN]", "own", "post", "update [ANC ID]", "ip [ANC ID]",
				"delete [ANC ID]");
	}

	private String[] prepareMsg(String dest, String msg) {
		String[] msgTable = msg.split("\n");
		String[] args = new String[msgTable.length + 2];
		args[0] = dest;
		args[1] = Long.valueOf(System.currentTimeMillis()).toString();
		for(int i = 0 ; i < msgTable.length ; i++) {
			args[i+2] = msgTable[i];
		}
		return args;
	}

	private void sendMsg(boolean anc, String[] tokens) {
		if(tokens != null && tokens.length == 3) {
			String dest = null;
			InetSocketAddress addr = null;
			if(anc) {
				Message m = dataProvider.requestIP(tokens[1]);
				if(m != null && m.getArgs() != null && m.getArgs().length == 2) {
					dest = m.getArgs()[1];
					addr = new InetSocketAddress(m.getArgs()[0], 7201);
					ipBook.addOrUpdate(m.getArgs()[1], addr);
					Logs.log("New adress added with success");
				} else {
					Logs.warning("The server didn't find the address!");
					return;
				}
			} else {
				dest = tokens[1];
				addr = ipBook.getIp(dest);
				if(addr == null) {
					Logs.warning("Warning this person can't be found anymore.");
					return;
				}
			}
			String[] args = prepareMsg(dest, tokens[2]);
			Message request = new Message(Message.MessageType.MSG, args, addr);
			box.addToSendingList(dest, request);
			Logs.log("Inserted new message ->");
		}
	}

	private String[] createAnnonceHelper(boolean new_entry) {
		String[] args = new String[4];
		Scanner scanner = new Scanner(System.in);
		for (int i = 0; i < args.length; i++) {
			switch (i) {
				case 0:
					System.out.println("Please inform the domain");
					break;
				case 1:
					System.out.println("Please inform the title");
					break;
				case 2:
					System.out.println("Please inform the description");
					break;
				case 3:
					System.out.println("Please inform the price");
					break;
			}
			if (!new_entry) {
				System.out.println("(Type nothing for no information)");
			}

			String s = scanner.nextLine();
			s.trim();
			if (s.isEmpty()) {
				if (!new_entry) {
					args[i] = "null";
				} else {
					i--;
				}
			} else
				args[i] = s;
		}
		return args;
	}
}
