package none.spark.event.events;

import none.spark.event.Event;

public class KeyUpEvent extends Event {

    public final int keyCode;

    public KeyUpEvent(int keyCode) {
        this.keyCode = keyCode;

    }

    public String getName() {
        return "keyup";
    }

}
