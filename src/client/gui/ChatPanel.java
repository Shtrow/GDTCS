package client.gui;

import com.googlecode.lanterna.gui2.*;

public class ChatPanel extends Panel {
    public ComboBox<String> getUserList() {
        return comboBox;
    }

    private final ComboBox<String> comboBox;
    public ChatPanel(){
        super(new BorderLayout());
        Panel bottomPanel = new Panel(new GridLayout(2));
        comboBox = new ComboBox<String>();
        comboBox.addItem("User1");
        comboBox.addItem("User2");
        BorderLayout layout = (BorderLayout) this.getLayoutManager();
        Label text = new Label("Lorem Lipsum");
        PromptBox promptBox = new PromptBox(s -> {});
        this.addComponent(text);
        bottomPanel.addComponent(
                promptBox.setLayoutData(
                        GridLayout.createLayoutData(GridLayout.Alignment.FILL,
                                GridLayout.Alignment.BEGINNING,
                                true,
                                false)));
        bottomPanel.addComponent(
                comboBox.setLayoutData(
                        GridLayout.createLayoutData(GridLayout.Alignment.END,
                                GridLayout.Alignment.END,
                                false,
                                false))
        );
//        this.addComponent(promptBox);
        this.addComponent(bottomPanel);
        text.setLayoutData(BorderLayout.Location.CENTER);
        bottomPanel.setLayoutData(BorderLayout.Location.BOTTOM);


    }

}
