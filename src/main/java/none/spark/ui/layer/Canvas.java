package none.spark.ui.layer;

import net.minecraft.client.Minecraft;
import none.spark.ui.event.UIEvent;
import none.spark.ui.event.events.UIRender2DEvent;
import none.spark.ui.util.NullGuiScreen;

import java.util.ArrayList;

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
        if(this.selfRender && uiEvent instanceof UIRender2DEvent){
            this.render();
        }else{
            for (View view : this.views) {
                if (view instanceof Canvas) {
                    this.onEvent(((Canvas) view).getViews(), uiEvent);
                } else {
                    view.onEvent(uiEvent);
                }
            }
        }
    }

    public void onEvent(ArrayList<View> views, UIEvent uiEvent) {
        if(this.selfRender && uiEvent instanceof UIRender2DEvent){
            this.render();
        }else{
            for (View view : views) {
                if (view instanceof Canvas) {
                    this.onEvent(((Canvas) view).getViews(), uiEvent);
                } else {
                    view.onEvent(uiEvent);
                }
            }
        }
    }

    public void render() {
        for (View view : this.views) {
            if (view instanceof Canvas) {
                this.render(((Canvas) view).getViews());
            } else {
                view.render();
            }
        }
    }

    public void render(ArrayList<View> views) {
        for (View view : views) {
            if (view instanceof Canvas) {
                this.render(((Canvas) view).getViews());
            } else {
                view.render();
            }
        }
    }
}
