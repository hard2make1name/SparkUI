package none.spark.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Objects;

public final class IconUtils {

    public static final String ICON_16X_PATH = "/icon_16x16.png";
    public static final String ICON_32X_PATH = "/icon_32x32.png";

    public static ByteBuffer[] getIconByteBuffer() {
        return new ByteBuffer[]{
                readImageToBuffer(ExternalUtils.getResourceInputStream(ICON_16X_PATH)),
                readImageToBuffer(ExternalUtils.getResourceInputStream(ICON_32X_PATH))
        };
    }

    public static ByteBuffer readImageToBuffer(final InputStream imageStream) {
        Objects.requireNonNull(imageStream);
        try {
            BufferedImage bufferedImage = ImageIO.read(imageStream);
            int[] rgb = bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null, 0, bufferedImage.getWidth());
            ByteBuffer byteBuffer = ByteBuffer.allocate(4 * rgb.length);
            for (int i : rgb) {
                byteBuffer.putInt(i << 8 | i >> 24 & 255);
            }
            // Open JDK 8 on Linux doesn't support byteBuffer.flip();
            ((Buffer) byteBuffer).flip();
            return byteBuffer;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ByteBuffer readImageToBuffer(final BufferedImage bufferedImage) {
        int[] rgb = bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null, 0, bufferedImage.getWidth());
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * rgb.length);
        for (int i : rgb) {
            byteBuffer.putInt(i << 8 | i >> 24 & 255);
        }
        byteBuffer.flip();
        return byteBuffer;
    }
}
