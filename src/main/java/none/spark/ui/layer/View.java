package none.spark.ui.layer;

import none.spark.ui.event.UIEvent;

public abstract class View {
    public View() {}

    public abstract void onEvent(UIEvent uiEvent);
}
