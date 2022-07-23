package none.spark.utils;

import java.io.IOException;
import java.io.InputStream;

public class JarUtils {

    public static InputStream getResourceInputStream(String resourcePath) throws IOException {
        return Class.class.getResourceAsStream(resourcePath);
    }

}