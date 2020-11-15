package test;

import com.googlecode.lanterna.gui2.BorderLayout;
import com.googlecode.lanterna.gui2.Interactable;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.input.KeyStroke;
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
//            this.handleKeyStroke(new KeyStroke(KeyType.Enter));

        }

    @Override
    public synchronized Result handleKeyStroke(KeyStroke keyStroke) {
            if(keyStroke.getKeyType() == KeyType.Enter){
                return Result.HANDLED;
            }
            return super.handleKeyStroke(keyStroke);
    }

    @Override
    protected void afterLeaveFocus(FocusChangeDirection direction, Interactable nextInFocus) {
        super.afterLeaveFocus(direction, nextInFocus);
        System.out.println("SDF");
        this.takeFocus();
    }

}
