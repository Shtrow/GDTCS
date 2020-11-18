package client.gui;

import client.DataProvider;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import common.LetterBox;
import common.Logs;
import common.Message;
import common.PeerList;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChatPanel extends Panel {
    public ComboBox<String> getUserList() {
        return comboBox;
    }
    private ComboBox<String> comboBox;
    private volatile ConcurrentHashMap<String,String> messageHistory = new ConcurrentHashMap<>();
    private Label text = new Label("Welcome to the GDT Chat!");
    private GUI gui;
    private Panel chatPanel = new Panel(new BorderLayout());
    private Panel blankPanel = new Panel().addComponent(new EmptySpace(new TerminalSize(0,0)));
    private Panel currentPanel = chatPanel;
    public ChatPanel(GUI gui){
        super(new GridLayout(1));
        this.addComponent(currentPanel.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.FILL, GridLayout.Alignment.FILL,true,true
        )));
        this.gui = gui;
        comboBox = new ComboBox<String>();

        Thread thread = new Thread(new LetterBoxThread()); thread.start();

//        Panel bottomPanel = new Panel(new GridLayout(2));
        PromptBox promptBox = new PromptBox(s -> {
            synchronized (messageHistory){
                String user = comboBox.getSelectedItem();
                String[] args = {"send_msg", user,s};
                    String hist = messageHistory.getOrDefault(user,"");
                    String newMessage = hist + "\n" + "[me] " + s;
                    messageHistory.put (user, newMessage);
                    setText(newMessage);
                    gui.sendMsg(false, args);
            }
        });
        chatPanel.addComponent(comboBox.setLayoutData(BorderLayout.Location.TOP));
        chatPanel.addComponent(text.setLayoutData(BorderLayout.Location.CENTER));
        chatPanel.addComponent(promptBox.setLayoutData(BorderLayout.Location.BOTTOM));
        comboBox.addListener((i, i1) -> {
            String selectedUser = comboBox.getSelectedItem();
            if (selectedUser == null) {
                return;
            }
        });
    }

    private void updatePeerList(){
        peerListToComboBox();
        if(gui.getIpBook().getPeerList().isEmpty()) {
            Logs.log("No contact yet");
            //Set this panel invisible
        }
    }

    private void peerListToComboBox(){
        this.comboBox.clearItems();
        var peerList = gui.getIpBook().getPeerList();
        peerList.forEach(this.comboBox::addItem);
    }

    private void textAppend(String s){
        text.setText(text.getText() + "\n" + s);
    }

    private void setText(String s){
        text.setText(s);
    }

    /**
     * Thread which will update messages received
     */
    public class LetterBoxThread implements Runnable{
        final int updateFrequency = 300;
        @Override
        public synchronized void run() {
            //TODO Hide chat panel when there is nobody in the contact list
//            currentPanel = (comboBox.getItemCount() == 0) ? blankPanel : chatPanel;
            var letterBox = ChatPanel.this.gui.getBox();
            while(true){
                try {
                    Thread.sleep(updateFrequency);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Set<String> peerList = gui.getIpBook().getPeerList();
//                var messages =  letterBox.getAllNewMsg();
//                if(messages !=null){
//                    messages.forEach(message -> {
//
//                    });
//                }
                peerList.forEach(s -> {
                    var messages = letterBox.getNewMsgFor(s);
                    if (messages != null){
                        var stringMessages = messages.stream().map(message -> {
                            var stringMessage = Arrays.stream(message.getArgs()).reduce(String::concat);
                            return stringMessage.get();
                        });
                        var stringMessage = stringMessages.reduce((s1, s2) -> s1 + "\n"+s2);
                        stringMessage.ifPresent(value -> {
                            var new_text = messageHistory.getOrDefault(s,"")+ "\n"+value;
                            messageHistory.put(s,new_text);
                            setText(new_text);
                        });
                        updatePeerList();
                    }
                });
            }
        }
    }

    public void sendMsg(boolean anc, String[] tokens) {
        if(tokens != null && tokens.length == 3) {
            String dest = null;
            InetSocketAddress addr = null;
            if(anc) {
                Message m = gui.getDataProvider().requestIP(tokens[1]);
                if(m != null && m.getArgs() != null && m.getArgs().length == 2) {
                    dest = m.getArgs()[1];
                    addr = new InetSocketAddress(m.getArgs()[0], 7201);
                    gui.getIpBook().addOrUpdate(m.getArgs()[1], addr);
                } else {
                    Logs.warning("The server didn't find the address!");
                    return;
                }
            } else {
                dest = tokens[1];
                addr = gui.getIpBook().getIp(dest);
                if(addr == null) {
                    Logs.warning("Warning this person can't be found anymore.");
                    return;
                }
            }
            String[] args = prepareMsg(dest, tokens[2]);
            Message request = new Message(Message.MessageType.MSG, args, addr);
            gui.getBox().insertInLetterBox(dest, request);
        }
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



}
