package none.spark.ui.event;

import none.spark.ui.UIStatics;
import none.spark.ui.event.events.UIKeyEvent;
import none.spark.ui.event.events.UIMouseEvent;
import none.spark.ui.event.events.UIRender2DEvent;
import none.spark.ui.layer.Canvas;
import none.spark.ui.layer.View;
import none.spark.ui.layer.views.TextField;
import none.spark.util.DebugUtils;
import none.spark.util.ExternalUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;

public class UIEventManager {

    public static final Canvas gameCanvas = UIStatics.gameCanvas;

    public ArrayList<View> targets = new ArrayList<>();

    public UIEventManager() {
        this.targets.add(gameCanvas);
    }

    public void callEvent(UIEvent uiEvent, ArrayList<View> aTargets) {
        for (View view : aTargets) {
            if (view instanceof Canvas) {
                this.callEvent(uiEvent, ((Canvas) view).views);
            } else {
                this.handleEvent(uiEvent, view);
            }
        }
    }

    public void callEvent(UIEvent uiEvent) {
        for (View view : this.targets) {
            if (view instanceof Canvas) {
                this.callEvent(uiEvent, ((Canvas) view).views);
            } else {
                this.handleEvent(uiEvent, view);
            }
        }
    }

    public boolean isControlDown = false;

    public void handleEvent(UIEvent uiEvent, View view) {
        // except Canvas
        if (uiEvent instanceof UIRender2DEvent) {
            if (view instanceof TextField) {
                UIStatics.viewRenderer.renderTextField((TextField) view);
            }
        } else if (uiEvent instanceof UIKeyEvent) {
            if (Keyboard.getEventKey() == 29 || Keyboard.getEventKey() == 157) {
                this.isControlDown = Keyboard.getEventKeyState();
            }
            if (this.isControlDown && Keyboard.getEventKeyState()) {
                if (Keyboard.getEventKey() == 46) {
                    // Ctrl + C = Copy
                    if (view instanceof TextField) {
                        TextField textField = (TextField) view;
                        if (textField.selectionEndIndex > textField.selectionBeginIndex) {
                            ExternalUtils.setClipboardString(textField.text.substring(textField.selectionBeginIndex, textField.selectionEndIndex));
                        } else {
                            ExternalUtils.setClipboardString(textField.text.substring(textField.selectionEndIndex, textField.selectionBeginIndex));
                        }
                    }
                }
            }
            DebugUtils.printKeyboardDetails();
        } else if (uiEvent instanceof UIMouseEvent) {
            if (Mouse.getEventButton() != -1) {
                // Some mouse's button state was change
                if (Mouse.getEventButtonState()) {
                    // Mouse down
                    if (Mouse.isButtonDown(0)) {
                        // Left down
                        if (view instanceof TextField) {
                            TextField textField = (TextField) view;
                            int mouseX = Mouse.getEventX();
                            int mouseY = UIStatics.gameCanvas.height - Mouse.getEventY();
                            if (
                                    mouseX > textField.posX && mouseX < textField.posX + textField.width &&
                                            mouseY > textField.posY && mouseY < textField.posY + textField.height
                            ) {
                                // mouse down on the TextField
                                textField.mouseBeginPosX = mouseX;
                                textField.mouseBeginPosY = mouseY;
                                textField.mouseEndPosX = mouseX;
                                textField.mouseEndPosY = mouseY;
                                //textField.focus = true;
                            }/* else {
                                textField.focus = true;
                            }*/
                        }
                    }
                }
            } else {
                if (Mouse.isButtonDown(0)) {
                    // Left dragging
                    if (view instanceof TextField) {
                        TextField textField = (TextField) view;
                        //if (textField.focus) {
                            textField.mouseEndPosX = Mouse.getEventX();
                            textField.mouseEndPosY = UIStatics.gameCanvas.height - Mouse.getEventY();
                        //}
                    }
                }
            }
        }
    }
}
