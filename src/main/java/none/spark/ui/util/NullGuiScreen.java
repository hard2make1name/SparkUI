package none.spark.ui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class NullGuiScreen extends GuiScreen {
    // 我存在的意义只是为了使 mc.currentScreen != null

    public static final Minecraft mc = Minecraft.getMinecraft();

    public boolean guiPauseGame;
    public boolean exitByKeyEscape;

    public NullGuiScreen() {
        this.guiPauseGame = false;
        this.exitByKeyEscape = false;
    }

    public NullGuiScreen(boolean guiPauseGame, boolean exitByKeyEscape) {
        this.guiPauseGame = guiPauseGame;
        this.exitByKeyEscape = exitByKeyEscape;
    }

    public void exit() {
        mc.displayGuiScreen(null);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return this.guiPauseGame;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {

        if (this.exitByKeyEscape && keyCode == 1) {
            mc.displayGuiScreen(null);

            if (mc.currentScreen == null) {
                mc.setIngameFocus();
            }
        }

        mc.dispatchKeypresses();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {

    }
}