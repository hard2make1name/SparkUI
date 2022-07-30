package none.spark.ui.event.events;

import net.minecraft.client.gui.ScaledResolution;
import none.spark.ui.event.UIEvent;

public class UIRender2DEvent extends UIEvent {
    public String getName() {
        return "render2d";
    }

    public ScaledResolution scaledResolution;
    public float partialTicks;

    public UIRender2DEvent(ScaledResolution scaledResolution, float partialTicks) {
        this.scaledResolution = scaledResolution;
        this.partialTicks = partialTicks;
    }
}
