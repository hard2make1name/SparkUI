package none.spark.ui.layer.views;

import none.spark.ui.event.UIEvent;
import none.spark.ui.layer.View;
import org.lwjgl.input.Keyboard;

import java.awt.*;

// 参考 https://blog.csdn.net/weixin_38211198/article/details/89739013
public class InputField extends View {

    public int posX = 0;
    public int posY = 0;
    public int width = 20;
    public int height = 20;
    public int fontSize = 20;
    public boolean readOnly = false;
    public boolean interactable = true;
    public boolean visible = true;
    public int lengthLimit = -1;
    public String hint = "There is hint!";
    public String text = "Hello World!";
    public boolean focus = false;
    public boolean drawBackground = true;
    // TODO 透明先不搞，我这里抄了 IDEA 的输入框颜色
    public Color backgroundColor = new Color(69, 73, 74, 255);
    public Color borderColor = new Color(100, 100, 100, 255);
    public Color focusBorderColor = new Color(70, 109, 148, 255);
    public Color focusOutBorderColor = new Color(61, 97, 133, 255);

    public TextView textView = new TextView();
    public int selectionBegin = 0;
    public int selectionEnd = 0;
    //System.currentTimeMillis()

    public InputField() {
        this.textView.text = "He";
    }

    public void onKey(UIEvent uiEvent) {
        if (Keyboard.getEventCharacter() != 0) {
            this.textView.text += Keyboard.getEventCharacter();
        }
    }

    public void onEvent(UIEvent uiEvent){}
}
