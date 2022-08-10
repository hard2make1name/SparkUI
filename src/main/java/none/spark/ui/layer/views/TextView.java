package none.spark.ui.layer.views;

import none.spark.ui.UIStatics;
import none.spark.ui.event.UIEvent;
import none.spark.ui.event.events.UIMouseEvent;
import none.spark.ui.font.Glyph;
import none.spark.ui.layer.View;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;

// TODO custom font
// TODO Optimize the combination of TextView and ViewRenderer
public class TextView extends View {
    public int posX = 0;
    public int posY = 0;
    public int width = 20;
    public int height = 20;
    public int fontSize = 20;
    public Color fontColor = Color.WHITE;
    public Color selectionColor = new Color(33,66,131);
    public boolean fontSubstitute = true;
    public String text = "Hello, TextView!";

    public boolean richText = false;// TODO rich text
    public boolean lineWrap = true;// 遇到 \n 换行
    public boolean autoLineWrap = true;// 文本自动换行
    public boolean horizontalOverflow = false;// false : 文本不显示超出垂直边界的部分 ,true : 文本可以超出水平边界，继续显示
    public boolean verticalOverflow = false;// false : 文本不显示超出垂直边界的部分 , true : 文本可以超出垂直边界，继续显示

    public TextView() {
    }

    public void setText(String text) {
        this.text = text;
        this.glyphList = null;
        // then the ViewRenderer can know that it's time to updateTextViewGlyphList
    }

    // runtime
    public ArrayList<Glyph> glyphList;
    public int mouseX,mouseY, mouseBeginPosX, mouseBeginPosY, mouseEndPosX, mouseEndPosY;

    // Mouse.getEventButtonState()  false : 按键放开, true : 按键按住
    // Mouse.getEventButton()  -1 : 无按键有变化, 0 : 鼠标左键, 1 : 鼠标右键, 2 : 鼠标中键, 3 : 鼠标拓展键1, 4 : 鼠标拓展键2
    // 拓展键一般见于带鼠标宏的鼠标
    // System.out.println(Mouse.getX() + "|" + Mouse.getY() + "|" + Mouse.getEventX() + "|" + Mouse.getEventY());
    // TODO To know the difference between Mouse.getX() and Mouse.getEventX()

    public void onEvent(UIEvent uiEvent) {
        if (uiEvent instanceof UIMouseEvent) {
//            System.out.printf(
//                    "Mouse.getEventButton(): %d,Mouse.getEventX(): %d,Mouse.getEventY(): %d,Mouse.getEventButtonState(): %b\n",
//                    Mouse.getEventButton(),
//                    Mouse.getEventX(),
//                    Mouse.getEventY(),
//                    Mouse.getEventButtonState()
//            );
//            if (Mouse.isButtonDown(0)) {
//                 System.out.println(Mouse.getX() + "|" + Mouse.getY() + "|" + Mouse.getEventX() + "|" + Mouse.getEventY());
//            }
            if (Mouse.getEventButton() != -1) {
                // Some button state was change
                if (Mouse.getEventButtonState()) {
                    // Mouse down
                    if (Mouse.isButtonDown(0)) {
                        // Left down
                        this.mouseX = Mouse.getEventX();
                        this.mouseY = UIStatics.gameCanvas.height - Mouse.getEventY();
                        if (
                                this.mouseX > this.posX && this.mouseX < this.posX + this.width &&
                                this.mouseY > this.posY && this.mouseY < this.posY + this.height
                        ) {
                            // click on the TextView
                            this.mouseBeginPosX = mouseX;
                            this.mouseBeginPosY = mouseY;
                            this.mouseEndPosX = mouseX;
                            this.mouseEndPosY = mouseY;

                        }
                    }
                }
            } else {
                if (Mouse.isButtonDown(0)) {
                    // Left dragging
                    this.mouseEndPosX = Mouse.getEventX();
                    this.mouseEndPosY = UIStatics.gameCanvas.height - Mouse.getEventY();
                }
            }
        }
    }
}
