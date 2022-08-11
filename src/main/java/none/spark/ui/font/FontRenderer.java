package none.spark.ui.font;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import none.spark.Statics;
import none.spark.ui.util.RenderUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;

// GG
public class FontRenderer {
    public GlyphPool glyphPool;
    public boolean substitute;

    public FontRenderer(Font font) {
        this.glyphPool = new GlyphPool(font);
        this.substitute = true;
    }

    public boolean getSubstitute() {
        return this.substitute;
    }

    public void setSubstitute(boolean flag) {
        this.substitute = flag;
    }

    // guiScale: 0:auto 1:small 2:normal 3:large
    // Minecraft.getMinecraft().gameSettings.guiScale
    // 这样窗口怎么缩放，guiScale大或小，都不影响字体在屏幕的大小和绝对位置
    // 我记得这个缩放似乎跟坐标有关系（记不清楚了，也许是x,y乘一个scale），但是现在开发没有用到

    public void drawChar(int codePoint, int size, float x, float y) {
        Glyph finalGlyph = null;
        if (glyphPool.canDisplay(codePoint)) {
            finalGlyph = glyphPool.getGlyph(codePoint, size);
        } else if (this.substitute) {
            for (GlyphPool substituteGlyphPool : Statics.fontManager.substituteGlyphPools) {
                if (substituteGlyphPool.canDisplay(codePoint)) {
                    finalGlyph = substituteGlyphPool.getGlyph(codePoint, size);
                    break;
                }
            }
        }
        if (finalGlyph == null) return;

        float scale = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
        GL11.glPushMatrix();
        GL11.glScalef(1.0f / scale, 1.0f / scale, 1.0f);
        RenderUtils.drawImage(finalGlyph.textureId, x, y, x + finalGlyph.width, y + finalGlyph.height);
        GL11.glPopMatrix();

    }

    public void drawChar(char ch, int size, float x, float y) {
        drawChar(Character.toString(ch).codePointAt(0), size, x, y);
    }

    //for (int i = 0, len = str.codePointCount(0, str.length()); i < len; i++) {
    //            int codePoint = str.codePointAt(str.offsetByCodePoints(0, i));
    public void drawString(String str, int size, float x, float y) {
        float lineWidth = 0;
        float lineHeight = this.glyphPool.getFontMetrics(size).getHeight();
        float totalHeight = 0;

        float scale = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
        GL11.glPushMatrix();
        GL11.glScalef(1.0f / scale, 1.0f / scale, 1.0f);
        int[] codePoints = str.codePoints().toArray();
        for (int codePoint : codePoints) {
            String ch = String.valueOf(Character.toChars(codePoint));

            if ("\n".equals(ch)) {
                lineWidth = 0;
                totalHeight += lineHeight;
                continue;
            }
            Glyph finalGlyph = null;
            if (glyphPool.canDisplay(codePoint)) {
                finalGlyph = glyphPool.getGlyph(codePoint, size);
            } else if (this.substitute) {
                for (GlyphPool substituteGlyphPool : Statics.fontManager.substituteGlyphPools) {
                    if (substituteGlyphPool.canDisplay(codePoint)) {
                        finalGlyph = substituteGlyphPool.getGlyph(codePoint, size);
                        break;
                    }
                }
            }

            if (finalGlyph == null) continue;

            RenderUtils.drawImage(
                    finalGlyph.textureId,
                    lineWidth + x,
                    totalHeight + y,
                    lineWidth + (x + finalGlyph.width),
                    totalHeight + (y + finalGlyph.height)
            );

            lineWidth += finalGlyph.width;
        }
        GL11.glPopMatrix();
    }
}
