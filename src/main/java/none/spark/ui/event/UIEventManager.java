package none.spark.ui.event;

import none.spark.ui.UIStatics;
import none.spark.ui.layer.Canvas;

public class UIEventManager {

    public static final Canvas gameCanvas = UIStatics.gameCanvas;

    public UIEventManager() {
    }

    public void onEvent(UIEvent uiEvent) {
        UIEventManager.gameCanvas.onEvent(uiEvent);
    }
}
