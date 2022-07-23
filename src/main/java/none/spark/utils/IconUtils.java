package none.spark.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Objects;

public final class IconUtils {

    public static ByteBuffer[] getIconByteBuffer() {
        try {
            return new ByteBuffer[]{
                    readImageToBuffer(JarUtils.getResourceInputStream("/icon_16x16.png")),
                    readImageToBuffer(JarUtils.getResourceInputStream("/icon_32x32.png"))
            };
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
            byteBuffer.flip();
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
