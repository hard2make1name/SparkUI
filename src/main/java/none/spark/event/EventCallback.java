package none.spark.event;

public interface EventCallback {
    // java是真的nb（）
    <T extends Event> void callback(T event);
}
