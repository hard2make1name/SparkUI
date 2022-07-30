package none.spark.manager;

import none.spark.ui.font.FontRenderer;
import none.spark.ui.font.GlyphPool;
import none.spark.ui.util.FontUtils;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class FontManager {
    // TODO 需要优化
    public File fontDir;
    public HashMap<String, FontRenderer> fontRenderers;
    public ArrayList<GlyphPool> substituteGlyphPools;

    public FontManager(File fontDir) {
        this.fontDir = fontDir;
        this.fontRenderers = new HashMap<>();
        this.substituteGlyphPools = new ArrayList<>();
    }

    public File getFontFile(String fontFileName) {
        return new File(fontDir, fontFileName);
    }

    public Font getFont(String fontFileName) {
        return FontUtils.readOriginFont(getFontFile(fontFileName));
    }

    // fontRendererName 不一定是文件名，可以自定义
    public FontRenderer getFontRenderer(String fontRendererName) {
        return this.fontRenderers.get(fontRendererName);
    }

    public void addFontRenderer(String fontRendererName, FontRenderer fontRenderer) {
        this.fontRenderers.put(fontRendererName, fontRenderer);
    }

    public void addFontRenderer(String fontRendererName, Font font) {
        this.fontRenderers.put(fontRendererName, new FontRenderer(font));
    }

    public void addFontRenderer(String fontRendererName, String fontFileName) {
        this.fontRenderers.put(fontRendererName, new FontRenderer(getFont(fontFileName)));
    }

    public void addSubstituteGlyphPool(GlyphPool glyphPool) {
        this.substituteGlyphPools.add(glyphPool);
    }
}