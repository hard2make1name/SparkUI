package none.spark.ui.font;

import none.spark.ui.utils.FontUtils;
import none.spark.ui.utils.RenderUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class GlyphPool {
    //https://learnopengl.com/code_viewer.php?code=in-practice/text_rendering

    //public static final String test = "";
    public static final String ascii = "!\\\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~€‚ƒ„…†‡ˆ‰Š‹ŒŽ‘’“”•–—˜™š›œžŸ¡¢£¤¥¦§¨©ª«¬®¯°±²³´µ¶·¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþÿ";
    public static final int[] commonSize = {50, 80};
    public final Font originFont;
    // different size of font
    public final ArrayList<Integer> availableSize;
    public final HashMap<Integer, Font> fonts;
    public final HashMap<Integer, FontMetrics> fontMetricses;

    // <size, <codePoint, glyph>>
    public final HashMap<Integer, HashMap<Integer, Glyph>> glyphMap;

    // 一个Font可以分为多种size，而多种的size下就是字符，用codepoint
    // 用字符前之前要指定Size，然后是codepoint
    // 处理Emoji不应该在这里
    public GlyphPool(final Font font) {
        Objects.requireNonNull(font);
        this.originFont = font;

        this.availableSize = new ArrayList<>();
        this.glyphMap = new HashMap<>();
        this.fonts = new HashMap<>();
        this.fontMetricses = new HashMap<>();

//        for (int size : commonSize) {
//            glyphMap.put(size, new HashMap<>());
//        }
//        // 初始化
//
//
//        for (int size : commonSize) {
//            this.addNewSizeFont(size);
//        }
//        // 预加载
//        for (int size : commonSize) {
//            for (int j = 0, lenj = ascii.codePointCount(0, ascii.length()); j < lenj; j++) {
//                addGlyph(ascii.codePointAt(j), size);
//                //glyphMap.get(size).put(codePoint, createGlyph(codePoint, size));
//            }
//        }
    }

    public boolean canDisplay(final int codePoint) {
        return this.originFont.canDisplay(codePoint);
    }

    public Glyph getGlyph(final int codePoint, final int size) {
        if(!this.availableSize.contains(size)){
            this.addSize(size);
        }
        if (!this.glyphMap.get(size).containsKey(codePoint)) {
            this.addGlyph(codePoint, size);
        }
        return this.glyphMap.get(size).get(codePoint);
    }

    public FontMetrics getFontMetrics(int size){
        if(!this.availableSize.contains(size)){
            this.addSize(size);
        }
        return this.fontMetricses.get(size);
    }

    public void addSize(final int size) {
        Font font = originFont.deriveFont(Font.PLAIN, size);
        this.availableSize.add(size);
        this.fonts.put(size, font);
        this.fontMetricses.put(size, FontUtils.getFontMetrics(font));
        this.glyphMap.put(size, new HashMap<>());
    }

    public void addGlyph(final int codePoint, final int size) {
        this.glyphMap.get(size).put(codePoint, createGlyph(codePoint, size));
    }

    public Glyph createGlyph(final int codePoint, final int size) {
        BufferedImage image = createCharImage(codePoint, size);
//        File outputfile = new File("D:\\project\\SparkUI\\run\\SparkUI\\font\\cache\\" + codePoint + "-" + size + ".png");
//        try {
//            ImageIO.write(image, "png", outputfile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }//for test
        return new Glyph(image.getWidth(), image.getHeight(), size, RenderUtils.uploadTexture(image));
    }

    public BufferedImage createCharImage(final int codePoint, final int size) {
        // 处理Emoji不应该在这里
        Font finalFont;
        FontMetrics finalFontMetrics;
        if (!this.availableSize.contains(size)) {
            addSize(size);
        }
        finalFont = this.fonts.get(size);
        finalFontMetrics = this.fontMetricses.get(size);

        int charWidth = finalFontMetrics.charWidth(codePoint);
        int charHeight = finalFontMetrics.getHeight();
        BufferedImage image;
        if (charWidth == 0 || charHeight == 0) {
            return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        } else {
            image = new BufferedImage(charWidth, charHeight, BufferedImage.TYPE_INT_ARGB);
        }

        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setFont(finalFont);
        g.drawString(String.valueOf(Character.toChars(codePoint)), 0, finalFontMetrics.getAscent());
        g.dispose();
        return image;
    }
}
