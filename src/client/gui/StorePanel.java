package client.gui;

import com.googlecode.lanterna.gui2.BorderLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;

import java.io.PrintStream;

public class StorePanel extends Panel {
    private String panelText = "";
    Label contentLabel;
    GUI gui;
    public StorePanel(GUI gui) {
        super(new BorderLayout());
        this.gui = gui;

        contentLabel = new Label(panelText);
        TextBox promptBox = new PromptBox(s -> gui.parse(s).run())
                .setLayoutData(BorderLayout.Location.BOTTOM);

        this.addComponent(contentLabel.setLayoutData(BorderLayout.Location.CENTER));
        this.addComponent(promptBox);

    }

    public void setPanelText(String panelText) {
        this.panelText = panelText;
        contentLabel.setText(panelText);
    }
}
