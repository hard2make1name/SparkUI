package none.spark.injection.forge.mixins.misc;

import org.spongepowered.asm.mixin.Mixin;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Display.class)
public class MixinDisplay {

    @Shadow(remap = false)
    private static final org.lwjgl.opengl.DisplayImplementation display_impl;

    @Inject(remap = false, method = "setTitle",  at= @At(value = "INVOKE_ASSIGN",target = "Lorg/lwjgl/opengl/DisplayImplementation;setTitle(Ljava/lang/String;)V"))
    public static void setTitle(String newTitle, CallbackInfo callbackInfo) {
        display_impl.setTitle(newTitle+" | SparkUI");
    }
}
