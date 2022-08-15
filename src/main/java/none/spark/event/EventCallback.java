package none.spark.event;

public interface EventCallback {
    // Java D1D ONE 900D J00B :-))
    <T extends Event> void callback(T event);
}
