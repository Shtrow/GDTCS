package test;

import com.googlecode.lanterna.gui2.BorderLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;

public class ChatPanel extends Panel {
    public ChatPanel(){
        super(new BorderLayout());
        BorderLayout layout = (BorderLayout) this.getLayoutManager();
        PromptBox promptBox = new PromptBox(s -> {});
        this.addComponent(promptBox);
        promptBox.setLayoutData(BorderLayout.Location.BOTTOM);
        Label text = new Label("Lorem Lipsum");
        this.addComponent(text);
        text.setLayoutData(BorderLayout.Location.CENTER);

    }

}
