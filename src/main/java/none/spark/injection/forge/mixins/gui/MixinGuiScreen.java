package none.spark.injection.forge.mixins.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.IChatComponent;
import none.spark.Statics;
import none.spark.event.events.CommandEvent;
import none.spark.ui.UIStatics;
import none.spark.ui.event.events.UIKeyEvent;
import none.spark.ui.event.events.UIMouseEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen {

    @Shadow
    public Minecraft mc;
    @Shadow
    public int width;
    @Shadow
    public int height;
    @Shadow
    public List<GuiButton> buttonList;
    @Shadow
    public FontRenderer fontRendererObj;

    @Shadow
    public void updateScreen() {
    }

    @Shadow
    public abstract void handleComponentHover(IChatComponent component, int x, int y);

    @Shadow
    protected abstract void drawHoveringText(List<String> textLines, int x, int y);

    @Inject(method = "sendChatMessage(Ljava/lang/String;Z)V", at = @At("HEAD"), cancellable = true)
    private void messageSend(String msg, boolean addToChat, CallbackInfo callbackInfo) {
        String prefix = ".";

        if (msg.startsWith(prefix) && addToChat) {
            // addToChat 就是按 上 键可以获取上次输的命令
            this.mc.ingameGUI.getChatGUI().addToSentMessages(msg);
            String[] args = msg.split(" ");

            Statics.eventManager.callEvent(new CommandEvent(args[0], args));
            callbackInfo.cancel();
        }
    }

    @Inject(method = "handleMouseInput", at = @At("HEAD"))
    public void mixinHandleMouseInput(CallbackInfo callbackInfo) {
        UIStatics.uiEventManager.onEvent(new UIMouseEvent());
    }

    @Inject(method = "handleKeyboardInput", at = @At("HEAD"))
    public void mixinHandleKeyboardInput(CallbackInfo callbackInfo){
        UIStatics.uiEventManager.onEvent(new UIKeyEvent());
    }

}