package none.spark.util;

import java.io.IOException;
import java.io.InputStream;

public final class JarUtils {

    public static InputStream getResourceInputStream(String resourcePath) throws IOException {
        return Class.class.getResourceAsStream(resourcePath);
    }

}