package none.spark.ui.util;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Objects;

public final class FontUtils {

    public static FontMetrics getFontMetrics(Font font) {
        Objects.requireNonNull(font);
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        g.setFont(font);
        FontMetrics fontMetrics = g.getFontMetrics();

        g.dispose();
        return fontMetrics;
    }

    public static boolean isChinese(int codePoint) {
        return Character.UnicodeScript.of(codePoint) == Character.UnicodeScript.HAN;
    }

    // https://blog.csdn.net/congqingbin/article/details/97144558
    public static boolean isEmoji(int codePoint) {
        return (0x0080 <= codePoint && codePoint <= 0x02AF) ||
                (0x0300 <= codePoint && codePoint <= 0x03FF) ||
                (0x0600 <= codePoint && codePoint <= 0x06FF) ||
                (0x0C00 <= codePoint && codePoint <= 0x0C7F) ||
                (0x1DC0 <= codePoint && codePoint <= 0x1DFF) ||
                (0x1E00 <= codePoint && codePoint <= 0x1EFF) ||
                (0x2000 <= codePoint && codePoint <= 0x209F) ||
                (0x20D0 <= codePoint && codePoint <= 0x214F) ||
                (0x2190 <= codePoint && codePoint <= 0x23FF) ||
                (0x2460 <= codePoint && codePoint <= 0x25FF) ||
                (0x2600 <= codePoint && codePoint <= 0x27EF) ||
                (0x2900 <= codePoint && codePoint <= 0x29FF) ||
                (0x2B00 <= codePoint && codePoint <= 0x2BFF) ||
                (0x2C60 <= codePoint && codePoint <= 0x2C7F) ||
                (0x2E00 <= codePoint && codePoint <= 0x2E7F) ||
                (0xA490 <= codePoint && codePoint <= 0xA4CF) ||
                (0xE000 <= codePoint && codePoint <= 0xF8FF) ||
                (0xFE00 <= codePoint && codePoint <= 0xFE0F) ||
                (0xFE30 <= codePoint && codePoint <= 0xFE4F) ||
                (0x1F000 <= codePoint && codePoint <= 0x1F02F) ||
                (0x1F0A0 <= codePoint && codePoint <= 0x1F0FF) ||
                (0x1F100 <= codePoint && codePoint <= 0x1F64F) ||
                (0x1F680 <= codePoint && codePoint <= 0x1F6FF) ||
                (0x1F910 <= codePoint && codePoint <= 0x1F96B) ||
                (0x1F980 <= codePoint && codePoint <= 0x1F9E0);
    }

    public static Font readOriginFont(final File fontFile) {
        try {
            final InputStream inputStream = Files.newInputStream(fontFile.toPath());
            Font awtClientFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            inputStream.close();
            return awtClientFont;
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}