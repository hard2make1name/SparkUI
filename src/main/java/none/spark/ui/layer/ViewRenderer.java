package none.spark.ui.layer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import none.spark.Statics;
import none.spark.ui.UIStatics;
import none.spark.ui.font.Glyph;
import none.spark.ui.font.GlyphPool;
import none.spark.ui.layer.views.TextView;
import none.spark.ui.util.RenderUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ViewRenderer {
    public static final GlyphPool glyphPool = UIStatics.glyphPool;

    public Glyph getGlyph(int codePoint, int fontSize, boolean fontSubstitute) {
        if (glyphPool.canDisplay(codePoint)) {
            return glyphPool.getGlyph(codePoint, fontSize);
        } else if (fontSubstitute) {
            for (GlyphPool substituteGlyphPool : Statics.fontManager.substituteGlyphPools) {
                if (substituteGlyphPool.canDisplay(codePoint)) {
                    return substituteGlyphPool.getGlyph(codePoint, fontSize);
                }
            }
        }
        return null;
    }

    public void renderCanvas(Canvas canvas) {
        for (View view : canvas.views) {
            if (view instanceof Canvas) {
                this.renderCanvas((Canvas) view);
            } else if (view instanceof TextView) {
                this.renderTextView((TextView) view);
            }
        }
    }

    public void renderTextView(TextView textView) {
        RenderUtils.awtColor(textView.fontColor);

        float lineWidth = 0;
        float lineHeight = glyphPool.getFontMetrics(textView.fontSize).getHeight();
        float totalHeight = 0;
        float spaceWidth = getGlyph(" ".codePointAt(0), textView.fontSize, textView.fontSubstitute).width;

        GL11.glPushMatrix();
        GL11.glScalef(1.0f / UIStatics.scale, 1.0f / UIStatics.scale, 1.0f);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtils.glScissorVerticalFlipped(textView.posX, textView.posY, textView.width, textView.height);

        Glyph finalGlyph;
        int[] codePoints = textView.text.codePoints().toArray();
        for (int codePoint : codePoints) {

            switch (String.valueOf(Character.toChars(codePoint))) {
                case "\n":
                    if (textView.lineWrap) {
                        lineWidth = 0;
                        totalHeight += lineHeight;
                    }
                    continue;
                case "\t":
                    lineWidth += 4 * spaceWidth;
                    break;
            }
            // Horizontal Overflow Check
            if (lineWidth > textView.width) {
                continue;
            }

            if (totalHeight > textView.height) break;

            finalGlyph = getGlyph(codePoint, textView.fontSize, textView.fontSubstitute);

            // cannot find the glyph
            if (finalGlyph == null) continue;

            if (lineWidth + finalGlyph.width > textView.width) {
                if (textView.autoLineWrap) {
                    lineWidth = 0;
                    totalHeight += lineHeight;
                }
            }

            RenderUtils.drawImage(
                    finalGlyph.textureId,
                    textView.posX + lineWidth,
                    textView.posY + totalHeight,
                    textView.posX + (lineWidth + finalGlyph.width),
                    textView.posY + (totalHeight + finalGlyph.height)
            );
            lineWidth += finalGlyph.width;
        }

        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
}
