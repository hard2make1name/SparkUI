package none.spark.injection.forge.mixins.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
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

@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen {

    @Shadow
    public Minecraft mc;

    @Inject(method = "sendChatMessage(Ljava/lang/String;Z)V", at = @At("HEAD"), cancellable = true)
    private void messageSend(String msg, boolean addToChat, CallbackInfo callbackInfo) {
        String prefix = ".";

        if (msg.startsWith(prefix) && addToChat) {
            // addToChat means you can press key UP to get the last message you have sent
            this.mc.ingameGUI.getChatGUI().addToSentMessages(msg);
            String[] args = msg.split(" ");

            Statics.eventManager.callEvent(new CommandEvent(args[0], args));
            callbackInfo.cancel();
        }
    }

    // The event hooks in MixinMinecraft is effective only not in GuiScreen
    // So it is necessary to hook them there again
    @Inject(method = "handleMouseInput", at = @At("HEAD"))
    public void mixinHandleMouseInput(CallbackInfo callbackInfo) {
        UIStatics.uiEventManager.callEvent(new UIMouseEvent());
    }

    @Inject(method = "handleKeyboardInput", at = @At("HEAD"))
    public void mixinHandleKeyboardInput(CallbackInfo callbackInfo) {
        UIStatics.uiEventManager.callEvent(new UIKeyEvent());
    }
}