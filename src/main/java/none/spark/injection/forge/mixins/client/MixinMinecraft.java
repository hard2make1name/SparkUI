package none.spark.injection.forge.mixins.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import none.spark.SparkUI;
import none.spark.Statics;
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

    @Inject(method = "createDisplay", at = @At(value = "INVOKE",target = "Lorg/lwjgl/opengl/Display;setTitle(Ljava/lang/String;)V", shift = At.Shift.AFTER))
    private void mixinCreateDisplay(CallbackInfo callbackInfo) {
        Display.setTitle("Minecraft 1.8.9 - SparkUI");
    }

    @Inject(method = "setWindowIcon", at = @At("HEAD"), cancellable = true)
    private void setWindowIcon(CallbackInfo callbackInfo) {
        ByteBuffer[] icon = IconUtils.getIconByteBuffer();
        if (icon != null) {
            Display.setIcon(icon);
            callbackInfo.cancel();
        }

    }

    @Inject(method = "startGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;checkGLError(Ljava/lang/String;)V", ordinal = 2, shift = At.Shift.AFTER))
    private void startGame(CallbackInfo callbackInfo) {
        // 游戏加载页面完成时
        Statics.sparkUI = new SparkUI();
        Statics.sparkUI.run();
    }

    @Inject(method = "resize", at = @At("RETURN"))
    private void resizeMixin(int width, int height, CallbackInfo callbackInfo) {
        UIStatics.gameCanvas.width = width;
        UIStatics.gameCanvas.height = height;
        UIStatics.scale = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
    }

    //    @Inject(method = "dispatchKeypresses", at = @At(value = "HEAD"))
//    private void onKey(CallbackInfo callbackInfo) {
//        UIStatics.uiEventManager.onEvent(new UIKeyEvent());
//        System.out.printf(
//                "getEventCharacter(): {char:%c,int:%d}, getEventKey(): %d,getEventKeyState(): %b,isRepeatEvent() : %b,getNumKeyboardEvents() : %d getEventNanoseconds() : %s\n"
//                , Keyboard.getEventCharacter()
//                , (int) Keyboard.getEventCharacter()
//                , Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey()
//                , Keyboard.getEventKeyState()
//                , Keyboard.isRepeatEvent()
//                , Keyboard.getNumKeyboardEvents()
//                , String.valueOf(Keyboard.getEventNanoseconds())
//        );
//
//    }
    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;dispatchKeypresses()V"))
    private void keyboardNext(CallbackInfo callbackInfo) {
        UIStatics.uiEventManager.onEvent(new UIKeyEvent());
        System.out.printf(
                "getEventCharacter(): {char:%c,int:%d}, getEventKey(): %d,getEventKeyState(): %b,isRepeatEvent() : %b,getNumKeyboardEvents() : %d getEventNanoseconds() : %s\n"
                , Keyboard.getEventCharacter()
                , (int) Keyboard.getEventCharacter()
                , Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey()
                , Keyboard.getEventKeyState()
                , Keyboard.isRepeatEvent()
                , Keyboard.getNumKeyboardEvents()
                , Keyboard.getEventNanoseconds()
        );
    }
//    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;setKeyBindState(IZ)V"))
//    private void keyboardNext(CallbackInfo callbackInfo) {
//        UIStatics.uiEventManager.onEvent(new UIKeyEvent());
//        System.out.printf(
//                "getEventCharacter(): {char:%c,int:%d}, getEventKey(): %d,getEventKeyState(): %b,isRepeatEvent() : %b,getNumKeyboardEvents() : %d getEventNanoseconds() : %s\n"
//                , Keyboard.getEventCharacter()
//                , (int) Keyboard.getEventCharacter()
//                , Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey()
//                , Keyboard.getEventKeyState()
//                , Keyboard.isRepeatEvent()
//                , Keyboard.getNumKeyboardEvents()
//                , String.valueOf(Keyboard.getEventNanoseconds())
//        );
//    }
    //鼠标动了这个会打印原先的key事件

    // Mouse.getEventButtonState()  false : 按键放开, true : 按键按住
//                // Mouse.getEventButton()  -1 : 无按键有变化, 0 : 鼠标左键, 1 : 鼠标右键, 2 : 鼠标中键, 3 : 鼠标拓展键1, 4 : 鼠标拓展键2
//                // 拓展键一般见于带鼠标宏的鼠标
// Keyboard.getEventKeyState() false : 按键放开, true : 按键按住
//                // Keyboard.getEventCharacter() 输入字符用的
//    @Inject(remap = false, method = "runTick", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;getEventKey()I", shift = At.Shift.NONE))
//    private void keyboardNext(CallbackInfo callbackInfo) {
//        long keyNano = Keyboard.getEventNanoseconds();
//        if (keyNano == this.lastKeyNano || (Keyboard.getEventKeyState() || Keyboard.getEventKey() == 0 && Character.isDefined(Keyboard.getEventCharacter()))) {
//            return;
//        }
//        this.lastKeyNano = keyNano;
//
//        UIStatics.uiEventManager.onEvent(new UIKeyEvent());
//        System.out.printf(
//                "getEventCharacter(): {char:%c,int:%d}, getEventKey(): %d,getEventKeyState(): %b,isRepeatEvent() : %b,getNumKeyboardEvents() : %d getEventNanoseconds() : %s\n"
//                , Keyboard.getEventCharacter()
//                , (int) Keyboard.getEventCharacter()
//                , Keyboard.getEventKey()
//                , Keyboard.getEventKeyState()
//                , Keyboard.isRepeatEvent()
//                , Keyboard.getNumKeyboardEvents()
//                , String.valueOf(Keyboard.getEventNanoseconds())
//        );
//
//        // 按键之后会打印两次，结果完全相同，这是为什么? 在 dispatchKeypresses 却没有这种状况 :O
//    }

    @Inject(remap = false, method = "runTick", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Mouse;getEventButton()I", shift = At.Shift.NONE))
    private void mouseNext(CallbackInfo callbackInfo) {
        UIStatics.uiEventManager.onEvent(new UIMouseEvent());
        //System.out.printf("Mouse.getEventButton(): %d,Mouse.getEventX(): %d,Mouse.getEventY(): %d,Mouse.getEventButtonState(): %b\n", Mouse.getEventButton(), Mouse.getEventX(), Mouse.getEventY(), Mouse.getEventButtonState());
    }
}
