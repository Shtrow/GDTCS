package test;

import com.googlecode.lanterna.gui2.BorderLayout;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.input.KeyType;

import java.util.function.Consumer;

public class PromptBox extends TextBox {
        public PromptBox(Consumer<String> behavior){
            super();
            this.setLayoutData(BorderLayout.Location.CENTER);
            this.setInputFilter((interactable, keyStroke) -> {
                if(keyStroke.getKeyType() == KeyType.Enter){
                    String d =this.getText();
                    behavior.accept(d);
                    this.setText("");
                }
                return true;
            });

        }
}
