package test;

import com.googlecode.lanterna.gui2.*;

public class ChatPanel extends Panel {
    public ChatPanel(){
        super(new BorderLayout());
        ComboBox<String> comboBox = new ComboBox<String>();
        comboBox.addItem("User1");
        comboBox.addItem("User2");
        this.addComponent(comboBox);
        BorderLayout layout = (BorderLayout) this.getLayoutManager();
        Label text = new Label("Lorem Lipsum");
        this.addComponent(text);
        PromptBox promptBox = new PromptBox(s -> {});
        this.addComponent(promptBox);
        promptBox.setLayoutData(BorderLayout.Location.BOTTOM);
        text.setLayoutData(BorderLayout.Location.CENTER);


    }

}
