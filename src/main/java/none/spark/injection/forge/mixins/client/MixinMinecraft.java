package none.spark.injection.forge.mixins.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import none.spark.SparkUI;
import none.spark.Statics;
import none.spark.event.events.KeyDownEvent;
import none.spark.event.events.KeyUpEvent;
import none.spark.event.events.WorldEvent;
import none.spark.ui.UIStatics;
import none.spark.ui.event.events.UIKeyEvent;
import none.spark.ui.event.events.UIMouseEvent;
import none.spark.util.IconUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.ByteBuffer;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Inject(method = "createDisplay", at = @At(remap = false, value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;setTitle(Ljava/lang/String;)V", shift = At.Shift.AFTER))
    private void mixinCreateDisplay(CallbackInfo callbackInfo) {
        Display.setTitle("Minecraft 1.8.9 - SparkUI");
    }

    @Inject(method = "setWindowIcon", at = @At("HEAD"), cancellable = true)
    private void mixinSetWindowIcon(CallbackInfo callbackInfo) {
        ByteBuffer[] icon = IconUtils.getIconByteBuffer();
        if (icon != null) {
            Display.setIcon(icon);
            callbackInfo.cancel();
        }
    }

    @Inject(method = "resize", at = @At("HEAD"))
    private void mixinResize(int width, int height, CallbackInfo callbackInfo) {
        UIStatics.gameCanvas.width = width;
        UIStatics.gameCanvas.height = height;
    }

    //at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;checkGLError(Ljava/lang/String;)V", ordinal = 2, shift = At.Shift.AFTER)
    @Inject(method = "startGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;checkGLError(Ljava/lang/String;)V", ordinal = 2, shift = At.Shift.AFTER))
    private void mixinStartGame(CallbackInfo callbackInfo) {
        // 游戏加载页面完成时
        Statics.sparkUI = new SparkUI();
        Statics.sparkUI.run();
    }

    @Inject(method = "loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;Ljava/lang/String;)V", at = @At("HEAD"))
    private void mixinLoadWorld(WorldClient p_loadWorld_1_, String p_loadWorld_2_, CallbackInfo callbackInfo) {
        Statics.eventManager.callEvent(new WorldEvent());
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;dispatchKeypresses()V"))
    private void mixinKeyboardNext(CallbackInfo callbackInfo) {
        Statics.eventManager.callEvent(
                Keyboard.getEventKeyState() ?
                        new KeyDownEvent(Keyboard.getEventKey()) :
                        new KeyUpEvent(Keyboard.getEventKey())
        );
        UIStatics.uiEventManager.callEvent(new UIKeyEvent());
    }

    @Inject(method = "runTick", at = @At(remap = false, value = "INVOKE", target = "Lnet/minecraftforge/fml/common/FMLCommonHandler;fireMouseInput()V"))
    private void mixinMouseNext(CallbackInfo callbackInfo) {
        UIStatics.uiEventManager.callEvent(new UIMouseEvent());
    }

}
