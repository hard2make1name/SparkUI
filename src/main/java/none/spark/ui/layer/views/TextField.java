package none.spark.ui.layer.views;

import none.spark.ui.layer.View;

import java.awt.*;

// TODO custom font
// TODO rich text
public class TextField extends View {
    public int posX = 0;
    public int posY = 0;
    public int width = 20;
    public int height = 20;
    public int fontSize = 20;
    public Color fontColor = Color.WHITE;
    public boolean fontSubstitute = true;
    public String text = "Hello, TextField!";
    public Color selectionColor = new Color(0, 0, 255, 128);
    public boolean lineWrap = true;// 遇到 \n 换行
    public boolean autoLineWrap = true;// 文本自动换行
    public boolean horizontalOverflow = false;// false : 文本不显示超出垂直边界的部分 ,true : 文本可以超出水平边界，继续显示
    public boolean verticalOverflow = false;// false : 文本不显示超出垂直边界的部分 , true : 文本可以超出垂直边界，继续显示

    public boolean focus = false;
    public boolean editable = false;
    public boolean selectable = false;
    public boolean copyable = false;

    public TextField() {
    }

    // 实例化之后要是都自己管理渲染和事件，那就 性能-- 了，所以渲染交给 ViewRenderer ，事件交给 UIEventManager
    // After instanced, if both renders and events are managed by themselves, then performance is lower, so render to the ViewRenderer and event to the UIEventManager

    // runtime
    public int mouseBeginPosX = this.posX, mouseBeginPosY = this.posY, mouseEndPosX = this.posX, mouseEndPosY = this.posY;
    public int selectionBeginIndex = 0, selectionEndIndex = 0;
}
