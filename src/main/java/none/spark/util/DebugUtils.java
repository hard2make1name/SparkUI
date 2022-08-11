package none.spark.util;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public final class DebugUtils {

    // Mouse.getEventButtonState()  false : 按键放开, true : 按键按住
    // Mouse.getEventButton()  -1 : 无按键有变化, 0 : 鼠标左键, 1 : 鼠标右键, 2 : 鼠标中键, 3 : 鼠标拓展键1, 4 : 鼠标拓展键2
    // 拓展键一般见于带鼠标宏的鼠标
    // TODO To know the difference between Mouse.getX() and Mouse.getEventX()

    // Keyboard.getEventKeyState() false : 按键放开, true : 按键按住
    // Keyboard.getEventCharacter() 输入字符用的

    // guiScale: 0:auto 1:small 2:normal 3:large
    // Minecraft.getMinecraft().gameSettings.guiScale

    /*
        float scale = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
        GL11.glPushMatrix();
        GL11.glScalef(1.0f / scale, 1.0f / scale, 1.0f);
        ...
        GL11.glPopMatrix();
        // 这个不需要了，hook 的时候已经处理了
        // This is not needed, it has been handled in the hook
    */

    public static void printKeyboardDetails() {
        System.out.printf(
                "getEventCharacter(): {char:%c, int:%d}," +
                        "getEventKey(): %d," +
                        "getKeyName(): %s," +
                        "getEventKeyState(): %b," +
                        "isRepeatEvent() : %b," +
                        "getNumKeyboardEvents() : %d" +
                        "\n"
                , Keyboard.getEventCharacter()
                , (int) Keyboard.getEventCharacter()
                , Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey()
                , Keyboard.getKeyName(Keyboard.getEventKey())
                , Keyboard.getEventKeyState()
                , Keyboard.isRepeatEvent()
                , Keyboard.getNumKeyboardEvents()
        );
    }

    public static void printMouseDetails() {
        System.out.printf(
                "Mouse.getEventButton(): %d," +
                        "Mouse.getEventX(): %d," +
                        "Mouse.getEventY(): %d," +
                        "Mouse.getEventButtonState(): %b" +
                        "\n",
                Mouse.getEventButton(),
                Mouse.getEventX(),
                Mouse.getEventY(),
                Mouse.getEventButtonState()
        );
    }
}
