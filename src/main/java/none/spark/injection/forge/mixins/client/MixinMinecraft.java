
package none.spark.injection.forge.mixins.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import org.lwjgl.opengl.Display;
import org.lwjgl.input.Keyboard;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
	
	@Inject(method = "createDisplay", at = @At(value = "INVOKE"))
    private void createDisplay(CallbackInfo callbackInfo) {
        Display.setTitle("SparkUI");
    }

    // @Inject(method = "setWindowIcon", at = @At("HEAD"), cancellable = true)
    // private void setWindowIcon(CallbackInfo callbackInfo) {
        
    //     ByteBuffer[] icon = IconUtils.getIconByteBuffer();
    //     if(icon != null) {
    //         Display.setIcon(icon);
    //         callbackInfo.cancel();
    //     }
        
    // }

	@Inject(method = "run", at = @At("HEAD"))
	private void run(CallbackInfo callbackInfo){
        System.out.print("[SparkUI Mixin]");
        for(int i=0;i<50;i++){
            System.out.print("LO");
        }
        // IM.initRemapper();
		// IM.run();
	}

	// @Inject(method = "startGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;checkGLError(Ljava/lang/String;)V", ordinal = 2, shift = At.Shift.AFTER))
 //    private void startGame(CallbackInfo callbackInfo) throws java.io.IOException {
    	
 //    }
	
	// @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;dispatchKeypresses()V", shift = At.Shift.AFTER))
 //    private void onKey(CallbackInfo callbackInfo) {
 //        IM.eventManager.callEvent(new Events.KeyEvent(Keyboard.getEventKey()));
 //    }
}
