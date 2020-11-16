package client.gui;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Scanner;

public class GUI implements Runnable {

    StorePanel storePanel;
    PrintStream out;
    InputStream in;
    public GUI(PrintStream out){
        this.out = out;

    }

    @Override
    public void run() {
        System.out.println("Hello World!");
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        Screen mainPrompt = null;
        try {
            mainPrompt = defaultTerminalFactory.createScreen();
            mainPrompt.startScreen();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        WindowBasedTextGUI gui = new MultiWindowTextGUI(mainPrompt);
        Window window = new BasicWindow("GDT");
        var hints = new HashSet<Window.Hint>();
        hints.add(Window.Hint.EXPANDED);
        window.setHints(hints);

        Panel mainPanel = new Panel(new GridLayout(2));

        LayoutData layoutData =  GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.FILL,true,true);
        storePanel = new StorePanel(out);
        storePanel.setLayoutData(layoutData).withBorder(Borders.singleLine("Store"));
        mainPanel.addComponent(storePanel);
        Panel chatPanel = new ChatPanel();
        chatPanel.setLayoutData(layoutData).withBorder(Borders.singleLine("Chat"));
        mainPanel.addComponent(chatPanel);


        window.setComponent(mainPanel);
        gui.addWindowAndWait(window);
    }
    public void print(String s){
        storePanel.setPanelText(s);
    }

    public void println(String s){
        storePanel.setPanelText(s+"\n");
    }
    
    private String read(){
        StringBuilder s = new StringBuilder();
        Scanner scanner = new Scanner(in);
        while(scanner.hasNextLine()){
            s.append(scanner.next());
        }
        return s.toString();
    }

}
