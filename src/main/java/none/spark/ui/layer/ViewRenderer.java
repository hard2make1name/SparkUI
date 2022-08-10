package none.spark.ui.layer;

import net.minecraft.client.renderer.entity.Render;
import none.spark.Statics;
import none.spark.ui.UIStatics;
import none.spark.ui.font.Glyph;
import none.spark.ui.font.GlyphPool;
import none.spark.ui.layer.views.TextView;
import none.spark.ui.util.RenderUtils;
import org.lwjgl.opengl.GL11;

public class ViewRenderer {
    public static final GlyphPool glyphPool = UIStatics.glyphPool;

    public Glyph getGlyph(int codePoint, int fontSize, boolean fontSubstitute) {
        //  本来想在 TextView 存 Glyph 缓存的以减少 ViewRenderer 工作量
        //  但是意识到 GlyphPool 里面的 ArrayList 和 HashMap 提供 RandomAccess 我就放心了
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

//    public void updateTextViewGlyphList(TextView textView) {
//        textView.glyphList = new ArrayList<>();
//
//        int[] codePoints = textView.text.codePoints().toArray();
//        for (int codePoint : codePoints) {
//            textView.glyphList.add(this.getGlyph(codePoint, textView.fontSize, textView.fontSubstitute));
//        }
//    }

    // 报 switch 需要常量
//    public enum CodePoint {
//        NEW_LINE("\n".codePointAt(0));
//        public int codePoint;
//        CodePoint(int codePoint) {
//            this.codePoint = codePoint;
//        }
//    }
    public static final int CODEPOINT_NEW_LINE = "\n".codePointAt(0);
    public static final int CODEPOINT_SPACE = " ".codePointAt(0);


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
        // 上色
        RenderUtils.awtColor(textView.fontColor);

        // 限制渲染区域
        int scissorWidth = textView.width, scissorHeight = textView.height;
        if (textView.horizontalOverflow) {
            scissorWidth = UIStatics.gameCanvas.width - textView.posX;
        }
        if (textView.verticalOverflow) {
            scissorHeight = UIStatics.gameCanvas.height - textView.posY;
        }
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtils.glScissorVerticalFlipped(textView.posX, textView.posY, scissorWidth, scissorHeight);

        float lineWidth = 0;
        float lineHeight = glyphPool.getFontMetrics(textView.fontSize).getHeight();
        float totalHeight = 0;
        int lines = 0;
        float spaceWidth = getGlyph(CODEPOINT_SPACE, textView.fontSize, textView.fontSubstitute).width;

        // 这些坐标是相对于 textView 的左上角的
        int mouseBeginPosX = textView.mouseBeginPosX - textView.posX;
        int mouseBeginPosY = textView.mouseBeginPosY - textView.posY;
        int selectionBeginLine = ((mouseBeginPosY - mouseBeginPosY % (int) lineHeight) / (int) lineHeight);
        int selectionBeginPosX = 0;
        int selectionBeginIndex = 0;

        int mouseEndPosX = textView.mouseEndPosX - textView.posX;
        int mouseEndPosY = textView.mouseEndPosY - textView.posY;
        int selectionEndLine = ((mouseEndPosY - mouseEndPosY % (int) lineHeight) / (int) lineHeight);
        int selectionEndPosX = 0;
        int selectionEndIndex = 0;

        int lineDiff = selectionEndLine - selectionBeginLine;

        int[] codePoints = textView.text.codePoints().toArray();
        Glyph finalGlyph;
        int indexCount = 0;
        for (int codePoint : codePoints) {
            if (codePoint == CODEPOINT_NEW_LINE) {
                if (textView.lineWrap) {
                    lineWidth = 0;
                    totalHeight += lineHeight;
                    lines++;
                }
                //lineWidth += spaceWidth * 2;
                continue;
            }

            if (lines == selectionBeginLine) {
                if(lineWidth > mouseBeginPosX && selectionBeginPosX == 0){
                    selectionBeginPosX = (int) lineWidth;
                    selectionBeginIndex = indexCount;
                }
            }

            if (lines == selectionEndLine && lineWidth > mouseEndPosX && selectionEndPosX == 0) {
                selectionEndPosX = (int) lineWidth;
                selectionEndIndex = indexCount;
            }

            finalGlyph = getGlyph(codePoint, textView.fontSize, textView.fontSubstitute);
            // 文字超过右边了，不渲染
            if (!textView.horizontalOverflow && lineWidth > textView.width) {
                continue;
            }
            // 文字超过底下了，没救了
            if (!textView.verticalOverflow && totalHeight > textView.height) break;
            // 自动换行
            if (lineWidth + finalGlyph.width > textView.width) {
                if (textView.autoLineWrap) {
                    lineWidth = 0;
                    totalHeight += lineHeight;
                    lines++;
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
            indexCount++;
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        RenderUtils.awtColor(textView.selectionColor);
        if (lineDiff > 0) {
            // drag to down
            RenderUtils.drawRect(
                    textView.posX + selectionBeginPosX,
                    textView.posY + lineHeight * selectionBeginLine,
                    textView.posX + textView.width,
                    textView.posY + lineHeight * (selectionBeginLine + 1)
            );
            for (int i = selectionBeginLine; i < selectionEndLine - 1; i++) {
                RenderUtils.drawRect(
                        textView.posX,
                        textView.posY + lineHeight * (i + 1),
                        textView.posX + textView.width,
                        textView.posY + lineHeight * (i + 2)
                );
            }
            RenderUtils.drawRect(
                    textView.posX,
                    textView.posY + lineHeight * selectionEndLine,
                    textView.posX + selectionEndPosX,
                    textView.posY + lineHeight * (selectionEndLine + 1)
            );
        } else if (lineDiff < 0) {
            // drag to up
            RenderUtils.drawRect(
                    textView.posX,
                    textView.posY + lineHeight * selectionBeginLine,
                    textView.posX + selectionBeginPosX,
                    textView.posY + lineHeight * (selectionBeginLine + 1)
            );
            for (int i = selectionEndLine; i < selectionBeginLine - 1; i++) {
                RenderUtils.drawRect(
                        textView.posX,
                        textView.posY + lineHeight * (i + 1),
                        textView.posX + textView.width,
                        textView.posY + lineHeight * (i + 2)
                );
            }
            RenderUtils.drawRect(
                    textView.posX + selectionEndPosX,
                    textView.posY + lineHeight * selectionEndLine,
                    textView.posX + textView.width,
                    textView.posY + lineHeight * (selectionEndLine + 1)
            );
        } else {
            if (selectionEndPosX > selectionBeginPosX) {
                // drag left to right
                RenderUtils.drawRect(
                        textView.posX + selectionBeginPosX,
                        textView.posY + lineHeight * selectionBeginLine,
                        textView.posX + selectionEndPosX,
                        textView.posY + lineHeight * (selectionBeginLine + 1)
                );
            } else {
                // drag right to left
                RenderUtils.drawRect(
                        textView.posX + selectionEndPosX,
                        textView.posY + lineHeight * selectionBeginLine,
                        textView.posX + selectionBeginPosX,
                        textView.posY + lineHeight * (selectionBeginLine + 1)
                );
            }

        }

        GL11.glColor4f(1, 1, 1, 1);
    }
}
