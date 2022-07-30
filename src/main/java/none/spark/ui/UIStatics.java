package none.spark.ui;

import none.spark.ui.event.UIEventManager;
import none.spark.ui.font.FontRenderer;
import none.spark.ui.font.GlyphPool;
import none.spark.ui.font.advanced.TextViewRenderer;
import none.spark.ui.layer.Canvas;

public class UIStatics {

    public static TextViewRenderer textViewRenderer;
    public static UIEventManager uiEventManager;
    public static GlyphPool glyphPool;
    public static FontRenderer font1;
    public static FontRenderer font2;
    public static Canvas gameCanvas;

    public static boolean darkTheme = true;// true : 使用深色主题, false : 使用浅色主题

}
