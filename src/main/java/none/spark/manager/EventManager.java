package none.spark.manager;

import none.spark.event.Event;
import none.spark.event.EventCallback;

import java.util.ArrayList;
import java.util.HashMap;

public class EventManager {

    public HashMap<String, ArrayList<EventCallback>> eventListeners;

    public EventManager() {
        this.eventListeners = new HashMap<>();
    }

    public void clearEventListeners() {
        this.eventListeners = new HashMap<>();
    }

    // Nashorn 自动兼容了回调函数 :D
    public void addListener(String eventName, EventCallback eventCallback) {
        if (!eventListeners.containsKey(eventName)) {
            eventListeners.put(eventName, new ArrayList<>());
        }
        eventListeners.get(eventName).add(eventCallback);
    }

    public void callEvent(Event event) {
        if (!eventListeners.containsKey(event.getName())) return;

        for (EventCallback eventCallback : this.eventListeners.get(event.getName())) {
            try {
                eventCallback.callback(event);
            } catch (Throwable e) {
                System.err.print("\nScriptException (In Event Callback): \n" + e.getMessage() + "\n");
                StackTraceElement[] stackTraceElements = e.getStackTrace();

                for (StackTraceElement stackTraceElement : stackTraceElements) {
                    if ("<eval>".equals(stackTraceElement.getFileName())) {
                        System.err.print("At line number: " + stackTraceElement.getLineNumber() + "\n");
                    }
                }

            }
        }
    }

}
