package none.spark.event.events;

import none.spark.event.Event;

public class KeyEvent extends Event {

    public final int keyCode;

    public KeyEvent(int keyCode) {
        this.keyCode = keyCode;

    }

    public String getName() {
        return "key";
    }

}
