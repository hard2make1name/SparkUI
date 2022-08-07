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
import java.util.ArrayList;

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
        // ?
        float spaceWidth = getGlyph(" ".codePointAt(0), textView.fontSize, textView.fontSubstitute).width;

        GL11.glEnable(GL11.GL_SCISSOR_TEST);

        int scissorWidth = textView.width, scissorHeight = textView.height;

        if (textView.horizontalOverflow) {
            scissorWidth = UIStatics.gameCanvas.width - textView.posX;
        }
        if (textView.verticalOverflow) {
            scissorHeight = UIStatics.gameCanvas.height - textView.posY;
        }

        RenderUtils.glScissorVerticalFlipped(textView.posX, textView.posY, scissorWidth, scissorHeight);
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
            // 文字超过右边了，不渲染
            if (!textView.horizontalOverflow && lineWidth > textView.width) {
                continue;
            }

            // 文字超过底下了，没救了
            if (!textView.verticalOverflow && totalHeight > textView.height) break;

            finalGlyph = getGlyph(codePoint, textView.fontSize, textView.fontSubstitute);

            // cannot find the glyph
            if (finalGlyph == null) continue;

            // 自动换行
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
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
}
