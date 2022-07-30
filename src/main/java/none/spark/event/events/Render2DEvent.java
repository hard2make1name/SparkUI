package none.spark.event.events;

import net.minecraft.client.gui.ScaledResolution;
import none.spark.event.Event;

public class Render2DEvent extends Event {
    public final ScaledResolution scaledResolution;
    public final float partialTicks;

    public Render2DEvent(ScaledResolution scaledResolution, float partialTicks) {
        this.scaledResolution = scaledResolution;
        this.partialTicks = partialTicks;
    }

    public String getName() {
        return "render2d";
    }
}
