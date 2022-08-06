package none.spark.ui.layer.views;

import none.spark.ui.event.UIEvent;
import none.spark.ui.layer.View;

import java.awt.*;

//懒得做抽象层了
// TODO 字体自定义
public class TextView extends View {
    public int posX = 0;
    public int posY = 0;
    public int width = 20;
    public int height = 20;
    public int fontSize = 20;
    public Color fontColor = Color.WHITE;
    public boolean fontSubstitute = true;
    public String text = "Hello World!";

    public boolean richText = false;// TODO 富文本
    public boolean lineWrap = true;// 遇到 \n 换行
    public boolean autoLineWrap = true;// 文本自动换行
    public boolean horizontalOverflow = false;// false : 文本不显示超出垂直边界的部分 ,true : 文本可以超出水平边界，继续显示
    public boolean verticalOverflow = false;// false : 文本不显示超出垂直边界的部分 , true : 文本可以超出垂直边界，继续显示

    public TextView() {}

    public void onEvent(UIEvent uiEvent) {
    }
}
