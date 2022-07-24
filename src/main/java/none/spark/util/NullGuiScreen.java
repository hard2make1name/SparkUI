package none.spark.util;

import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class NullGuiScreen extends GuiScreen {
    // 我存在的意义只是为了使 mc.currentScreen != null

    // @Override
    //    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    //        super.keyTyped(typedChar, keyCode);
    //    }

    //    @Override
    //    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    //        super.drawScreen(mouseX, mouseY, partialTicks);
    //    }

    public static final net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getMinecraft();

    private boolean guiPauseGame;
    private boolean exitByKeyEscape;

    public NullGuiScreen() {
        this.guiPauseGame = false;
        this.exitByKeyEscape = false;
    }

    public NullGuiScreen(boolean guiPauseGame, boolean exitByKeyEscape) {
        this.guiPauseGame = guiPauseGame;
        this.exitByKeyEscape = exitByKeyEscape;
    }

    public void exit() {
        this.mc.displayGuiScreen((GuiScreen) null);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return this.guiPauseGame;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {

        if (this.exitByKeyEscape && keyCode == 1) {
            this.mc.displayGuiScreen((GuiScreen) null);

            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        }

        mc.dispatchKeypresses();
    }
}