package none.spark.event.events;

import none.spark.event.Event;

public class KeyDownEvent extends Event {

    public final int keyCode;

    public KeyDownEvent(int keyCode) {
        this.keyCode = keyCode;

    }

    public String getName() {
        return "keydown";
    }

}
