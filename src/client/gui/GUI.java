package client.gui;

import client.Controller;
import client.DataProvider;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Scanner;
import java.util.function.Function;
import java.util.regex.Pattern;

public  class GUI implements Runnable {

    StorePanel storePanel;
    PrintStream out;
    InputStream in;
    Controller controller;
    DataProvider dataProvider;
    Function<String,Runnable> currentStep;
    DefaultTerminalFactory defaultTerminalFactory;
    WindowBasedTextGUI gui;

    public GUI(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
        this.currentStep = mainParser;
    }

    @Override
    public void run() {
        defaultTerminalFactory = new DefaultTerminalFactory();
        Screen mainPrompt = null;
        try {
            mainPrompt = defaultTerminalFactory.createScreen();
            mainPrompt.startScreen();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        gui = new MultiWindowTextGUI(mainPrompt);
        Window window = new BasicWindow("GDT");
        var hints = new HashSet<Window.Hint>();
        hints.add(Window.Hint.EXPANDED);
        window.setHints(hints);

        Panel mainPanel = new Panel(new GridLayout(2));

        LayoutData layoutData = GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.FILL, true, true);
        storePanel = new StorePanel(this);
        storePanel.setLayoutData(layoutData).withBorder(Borders.singleLine("Store"));
        mainPanel.addComponent(storePanel);
        Panel chatPanel = new ChatPanel();
        chatPanel.setLayoutData(layoutData).withBorder(Borders.singleLine("Chat"));
        mainPanel.addComponent(chatPanel);

        window.setComponent(mainPanel);
        gui.addWindowAndWait(window);
        header();

    }

    public void print(String s) {
        storePanel.setPanelText(s);
    }

    public void println(String s) {
        storePanel.setPanelText(s + "\n");
    }

    public void printf(String format, Object... args) {
        String s = String.format(format,args);
        storePanel.setPanelText(s);

    }

    private String read() {
        StringBuilder s = new StringBuilder();
        Scanner scanner = new Scanner(in);
        while (scanner.hasNextLine()) {
            s.append(scanner.next());
        }
        return s.toString();
    }

    private final Runnable missingArg = () -> println("Missing arguments");

    private void header() {
        String head = "********************************\n" + "* Good Duck Transport Protocol *\n"
                + "*        Client Utility        *\n" + "*       By Marais - Viau       *\n"
                + "********************************\n";
        println(head);
    }

    Function<String,Runnable> mainParser = command -> {
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
                    return () -> createAnnonceGUI(null);
//                    return () -> dataProvider.postAnc(createAnnonceHelper(true));
                else {
                    return (() -> dataProvider.postAnc(Arrays.copyOfRange(tokens, 1, tokens.length)));
                }
            case "update":
                if (tokens.length == 2)
                    return () -> {
                        String[] l = new String[5];
                        l[0] = tokens[1];
                        createAnnonceGUI(tokens[1]);
                    };
                else if (tokens.length < 5)
                    return missingArg;
                else {
                    return (() -> dataProvider.updateAnc(Arrays.copyOfRange(tokens, 1, tokens.length)));
                }
            case "exit":
                return dataProvider::disconnect;
            case "echo":
                return () -> println("ECHO :" + command);
            case "domains":
                return dataProvider::getDomains;
            case "ancs":
                return () -> dataProvider.getProductByDomain(tokens[1]);
            case "ip":
                return () -> dataProvider.requestIP(tokens[1]);
            case "":
                return () -> println("");
            case "help":
                return this::displayHelp;
            default:
                return () -> println("Command not found");
        }
    };



    private void displayHelp() {
        String st = "- connect              Reconnect to the server\n" +
                "- exit                 Quite GTDT Client\n" +
                "- domains              Display available domains\n" +
                "- ancs [DOMAIN]        Display all \"annonces\" from DOMAIN\n" +
                "- own                  Request your \"annonces\"\n" +
                "- post                 Post an \"annonces\" (enter interactive mode)\n" +
                "- update [ANC ID]      Update an \"annonces\"(enter interactive mode)\n" +
                "- ip [ANC ID]          Request the ip of the \"annonces\" owner\n" +
                "- delete [ANC ID]      Delete an \"annonces\"";
        println(st);
    }

    private void createAnnonceGUI(String ancId){
        boolean newEntry = ancId == null;
        BasicWindow window = new BasicWindow(newEntry?"Create Annonce":"Updating "+ancId);
        Panel panel = new Panel();
        panel.setLayoutManager(new GridLayout(2));

        final Label lblOutput = new Label("");

        panel.addComponent(new Label("Domain"));
        final TextBox domain = new TextBox().addTo(panel);

        panel.addComponent(new Label("Title"));
        final TextBox title = new TextBox().addTo(panel);

        panel.addComponent(new Label("Description"));
        final TextBox description = new TextBox().addTo(panel);

        panel.addComponent(new Label("Price"));
        final TextBox price = new TextBox().addTo(panel);

        panel.addComponent(new EmptySpace(new TerminalSize(0, 0)));
        panel.addComponent(new EmptySpace(new TerminalSize(0, 0)));

        new Button("Submit", () -> {
            String[] args;
            if(!newEntry){
                args = new String[]{"null", "null", "null", "null"};
                if(!domain.getText().isEmpty()) args[0] = domain.getText();
                if(!title.getText().isEmpty()) args[1] = title.getText();
                if(!description.getText().isEmpty()) args[2] = description.getText();
                if(!price.getText().isEmpty()) args[3] = price.getText();
                String[] l = new String[5];
                l[0] = ancId;
                System.arraycopy(args, 0, l, 1, 4);
                dataProvider.updateAnc(l);
            }
            else {
                args = new String[4];
                args[0] = domain.getText();
                args[1] = title.getText();
                args[2] = description.getText();
                args[3] = price.getText();
                dataProvider.postAnc(args);
            }
            window.close();
        }).addTo(panel).setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.BEGINNING, GridLayout.Alignment.BEGINNING));


        new Button("Cancel", window::close).addTo(panel);

//        panel.addComponent(buttons);


//        panel.addComponent(new EmptySpace(new TerminalSize(0, 0)));
        panel.addComponent(lblOutput);

        // Create window to hold the panel
        window.setComponent(panel);
        gui.addWindowAndWait(window);
    }
}

