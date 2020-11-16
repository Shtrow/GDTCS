package client.gui;

import com.googlecode.lanterna.gui2.BorderLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;

import java.io.PrintStream;

public class StorePanel extends Panel {
    private String panelText = "";
    PrintStream out;
    public StorePanel(PrintStream out ) {
        super(new BorderLayout());
        this.out = out;

        Label contentLabel = new Label(panelText);
        TextBox promptBox = new PromptBox(out::print)
                .setLayoutData(BorderLayout.Location.BOTTOM);

        this.addComponent(contentLabel.setLayoutData(BorderLayout.Location.CENTER));
        this.addComponent(promptBox);

    }

    public void setPanelText(String panelText) {
        this.panelText = panelText;
    }
}
