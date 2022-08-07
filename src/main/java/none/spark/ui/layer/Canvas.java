package none.spark.ui.layer;

import net.minecraft.client.Minecraft;
import none.spark.ui.event.UIEvent;
import none.spark.ui.util.NullGuiScreen;

import java.util.ArrayList;

@SuppressWarnings("CanBeFinal")
public class Canvas extends View {

    public boolean selfRender = false;
    public int width;
    public int height;
    public ArrayList<View> views;

    public Canvas(int width, int height) {
        this.width = width;
        this.height = height;
        this.views = new ArrayList<>();
    }

    public static void freeMouse() {
        Minecraft.getMinecraft().displayGuiScreen(new NullGuiScreen(false, true));
    }

    public ArrayList<View> getViews() {
        return this.views;
    }

    public void addView(View view) {
        this.views.add(view);
    }

    public void onEvent(UIEvent uiEvent) {
        for (View view : this.views) {
            view.onEvent(uiEvent);
        }
    }
}
