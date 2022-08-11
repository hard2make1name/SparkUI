package none.spark.injection.forge.mixins.gui;

import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import none.spark.Statics;
import none.spark.event.events.Render2DEvent;
import none.spark.ui.UIStatics;
import none.spark.ui.event.events.UIRender2DEvent;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public abstract class MixinGuiInGame {

    // 注入到 HEAD ，GL渲染出来的的颜色是灰的，不知道为啥
    @Inject(method = "renderTooltip", at = @At("RETURN"))
    private void renderTooltipMixin(ScaledResolution scaledResolution, float partialTicks, CallbackInfo callbackInfo) {
        GL11.glPushMatrix();
        UIStatics.scale = scaledResolution.getScaleFactor();
        GL11.glScalef(1.0f / UIStatics.scale, 1.0f / UIStatics.scale, 1.0f);
        Statics.eventManager.callEvent(new Render2DEvent(scaledResolution, partialTicks));
        UIStatics.uiEventManager.callEvent(new UIRender2DEvent());
        GL11.glPopMatrix();
    }
}