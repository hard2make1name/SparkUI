package none.spark.ui.layer.views;

import none.spark.ui.event.UIEvent;
import none.spark.ui.event.events.UIKeyEvent;
import none.spark.ui.event.events.UIRender2DEvent;
import none.spark.ui.layer.View;
import none.spark.ui.util.RenderUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

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
        this.textView.selfRender = false;
        this.textView.text = "He";
        this.updateAttribute();
    }

    public void updateAttribute() {
        this.textView.posX = this.posX;
        this.textView.posY = this.posY;
        this.textView.width = this.width;
        this.textView.height = this.height;
    }

    //GL11.glPointSize(1.0f);
    public void render() {
        RenderUtils.awtColor(backgroundColor);
        RenderUtils.drawRect(posX, posY, posX + width, posY + height);

        //神秘的GL, 像素级操作
        if (focus) {
            GL11.glLineWidth(1.0f);
            RenderUtils.awtColor(focusBorderColor);
            RenderUtils.drawRectBorder(posX, posY - 1, posX + width + 1, posY + height);

            GL11.glLineWidth(2.0f);
            RenderUtils.awtColor(focusOutBorderColor);
            RenderUtils.drawRectBorder(posX - 2, posY - 2, posX + width + 2, posY + height + 2);
        } else {
            GL11.glLineWidth(1.0f);
            RenderUtils.awtColor(borderColor);
            RenderUtils.drawRectBorder(posX, posY - 1, posX + width + 1, posY + height);
        }

        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.textView.render();
    }

    public void onKey(UIEvent uiEvent) {
        if (Keyboard.getEventCharacter() != 0) {
            this.textView.text += Keyboard.getEventCharacter();
        }
    }

    public void onEvent(UIEvent uiEvent) {
        if (this.selfRender && uiEvent instanceof UIRender2DEvent) {
            this.render();
        }
        if (uiEvent instanceof UIKeyEvent) {
            this.onKey(uiEvent);
        }
    }
}
