package none.spark.ui.font.advanced;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import none.spark.Statics;
import none.spark.ui.UIStatics;
import none.spark.ui.font.Glyph;
import none.spark.ui.font.GlyphPool;
import none.spark.ui.layer.views.TextView;
import none.spark.ui.util.RenderUtils;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

import java.awt.*;

// 参考 https://blog.csdn.net/liushida00/article/details/104013024
public class TextViewRenderer {
    public static final GlyphPool glyphPool = UIStatics.glyphPool;
    public boolean substitute;

    public TextViewRenderer() {
        this.substitute = true;
    }

    public boolean getSubstitute() {
        return this.substitute;
    }

    public void setSubstitute(boolean flag) {
        this.substitute = flag;
    }

    public void renderTextView(TextView textView) {
        // GL11.glScissor 的表现很棒 xD
        //System.out.println(0);

        RenderUtils.awtColor(Color.WHITE);

        float lineWidth = 0;
        float lineHeight = glyphPool.getFontMetrics(textView.fontSize).getHeight();
        float totalHeight = 0;
        boolean canRender = true;

        float scale = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
        GL11.glPushMatrix();
        GL11.glScalef(1.0f / scale, 1.0f / scale, 1.0f);
        int[] codePoints = textView.text.codePoints().toArray();
        for (int codePoint : codePoints) {

            if (textView.lineWrap && "\n".equals(String.valueOf(Character.toChars(codePoint)))) {
                lineWidth = 0;
                totalHeight += lineHeight;
                continue;
            }

//            if (!textView.lineWrap) {
//                lineWidth += 10;
//            }

            Glyph finalGlyph = null;
            if (glyphPool.canDisplay(codePoint)) {
                finalGlyph = glyphPool.getGlyph(codePoint, textView.fontSize);
            } else if (this.substitute) {
                for (GlyphPool substituteGlyphPool : Statics.fontManager.substituteGlyphPools) {
                    if (substituteGlyphPool.canDisplay(codePoint)) {
                        finalGlyph = substituteGlyphPool.getGlyph(codePoint, textView.fontSize);
                        break;
                    }
                }
            }

            if (finalGlyph == null) continue;

            // 水平超出检测
//            boolean isHorizontalOverflow = lineWidth + finalGlyph.width > textView.width;
//            if (!textView.horizontalOverflow && isHorizontalOverflow) {
//                if (textView.autoLineWrap) {
//                    lineWidth = 0;
//                    totalHeight += lineHeight;
//                } else if (textView.lineWrap) {
//                    canRender = false;
//                } else {
//                    break;
//                }
//            }
//
//            if (!isHorizontalOverflow) {
//                canRender = true;
//            }

            // 垂直超出检测
//            if (!textView.verticalOverflow && totalHeight + lineHeight > textView.height) {
//                break;
//            }
            if (canRender) {
                RenderUtils.drawImage(
                        finalGlyph.textureId,
                        textView.posX + lineWidth,
                        textView.posY + totalHeight,
                        textView.posX + (lineWidth + finalGlyph.width),
                        textView.posY + (totalHeight + finalGlyph.height)
                );
            }
            lineWidth += finalGlyph.width;
        }

        GL11.glPopMatrix();
    }
}
