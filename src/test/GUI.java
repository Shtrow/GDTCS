package test;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.io.IOException;
import java.util.HashSet;

public class GUI implements Runnable {

    public GUI(){

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
        Panel contentPanel = new Panel(new BorderLayout());
        Panel promptPanel = new Panel(new BorderLayout());
//        Label title = new Label("This is a label that spans two columns");
//
//        title.setLayoutData(GridLayout.createLayoutData(
//                GridLayout.Alignment.BEGINNING, // Horizontal alignment in the grid cell if the cell is larger than the component's preferred size
//                GridLayout.Alignment.BEGINNING, // Vertical alignment in the grid cell if the cell is larger than the component's preferred size
//                true,       // Give the component extra horizontal space if available
//                false,        // Give the component extra vertical space if available
//                2,                  // Horizontal span
//                1));                  // Vertical span
//        contentPanel.addComponent(title);
        Label contentLabel = new Label("");

        TextBox promptBox = new PromptBox(contentLabel::setText).setLayoutData(BorderLayout.Location.BOTTOM);

        promptPanel.addComponent(promptBox);

        contentPanel.addComponent(promptPanel.setLayoutData(BorderLayout.Location.BOTTOM));
        contentPanel.addComponent(contentLabel.setLayoutData(BorderLayout.Location.TOP));

        mainPanel.addComponent(contentPanel.setLayoutData(GridLayout.createHorizontallyFilledLayoutData()).withBorder(Borders.singleLine("Store")));
        mainPanel.addComponent(new ChatPanel().setLayoutData(GridLayout.createHorizontallyFilledLayoutData()).withBorder(Borders.singleLine("Chat")));


        window.setComponent(mainPanel);
        gui.addWindowAndWait(window);
    }

    public static void main(String[] args) {
        GUI gui = new GUI();
        gui.run();
    }
}
