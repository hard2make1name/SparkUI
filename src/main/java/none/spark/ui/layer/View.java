package none.spark.ui.layer;

import none.spark.ui.event.UIEvent;

public abstract class View {
    public View() {
    }
    // child View doesn't need to render when it recieves UIRender2DEvent
    // It will render by parent View
    public boolean selfRender = true;

    public abstract void render();

    public abstract void onEvent(UIEvent uiEvent);
}
