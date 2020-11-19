package client.gui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.Theme;
import com.googlecode.lanterna.gui2.BorderLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.PrintStream;

public class StorePanel extends Panel {
    private String panelText = "";
    Label contentLabel;
    GUI gui;
    public StorePanel(GUI gui) {
        super(new BorderLayout());
        this.gui = gui;
        contentLabel = new Label(panelText);
//        contentLabel.setBackgroundColor(TextColor.ANSI.GREEN);
//        contentLabel.setPreferredSize(new TerminalSize(3,3));
        TextBox promptBox = new PromptBox(s -> gui.parse(s).run())
                .setLayoutData(BorderLayout.Location.BOTTOM);
        contentLabel.getSize();

        this.addComponent(contentLabel.setLayoutData(BorderLayout.Location.CENTER));
        this.addComponent(promptBox);

    }

    public void setPanelText(String text) {
        this.panelText += text;
        var textSize = panelText.lines().count();
        var to_crop = textSize - this.getSize().getRows() +1;
        to_crop = (to_crop <0) ? 0 : to_crop;
        String fittedText = this.panelText.replaceFirst("(.*\\n){"+to_crop+"}",
                "");
        contentLabel.setText(fittedText);
    }
}
