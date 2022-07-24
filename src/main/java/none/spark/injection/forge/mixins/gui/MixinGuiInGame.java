
package none.spark.injection.forge.mixins.gui;

import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import none.spark.Statics;
import none.spark.event.events.Render2DEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public abstract class MixinGuiInGame {

    @Inject(method = "renderTooltip", at = @At("HEAD"))
    private void renderTooltipMixin(ScaledResolution scaledResolution, float partialTicks, CallbackInfo callbackInfo) {
        Statics.eventManager.callEvent(new Render2DEvent());
    }

}