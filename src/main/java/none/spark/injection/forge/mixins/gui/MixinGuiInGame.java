package none.spark.injection.forge.mixins.gui;

import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import none.spark.Statics;
import none.spark.event.events.Render2DEvent;
import none.spark.ui.UIStatics;
import none.spark.ui.event.events.UIRender2DEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public abstract class MixinGuiInGame {

    // 注入到 HEAD ，颜色是灰的，不知道为啥
    @Inject(method = "renderTooltip", at = @At("RETURN"))
    private void renderTooltipMixin(ScaledResolution scaledResolution, float partialTicks, CallbackInfo callbackInfo) {
        Statics.eventManager.callEvent(new Render2DEvent(scaledResolution, partialTicks));
        UIStatics.uiEventManager.onEvent(new UIRender2DEvent(scaledResolution, partialTicks));
    }

}