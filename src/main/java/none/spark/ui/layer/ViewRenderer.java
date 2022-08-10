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

        int mouseEndPosX = textView.mouseEndPosX - textView.posX;
        int mouseEndPosY = textView.mouseEndPosY - textView.posY;
        int selectionEndLine = ((mouseEndPosY - mouseEndPosY % (int) lineHeight) / (int) lineHeight);
        int selectionEndPosX = 0;

        int lineDiff = selectionEndLine - selectionBeginLine;
        int lastRenderedIndex = 0;

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

            finalGlyph = getGlyph(codePoint, textView.fontSize, textView.fontSubstitute);
            // lineWidth - finalGlyph.width / 2f > mouseBeginPosX
            if (lines == selectionBeginLine && mouseBeginPosX > lineWidth - finalGlyph.width / 2f && mouseBeginPosX < lineWidth + finalGlyph.width / 2f && selectionBeginPosX == 0) {
                selectionBeginPosX = (int) lineWidth;
                textView.selectionBeginIndex = indexCount;
            }

            if (lines == selectionEndLine && mouseEndPosX >= lineWidth - finalGlyph.width / 2f && mouseEndPosX <= lineWidth + finalGlyph.width / 2f && selectionEndPosX == 0) {
                selectionEndPosX = (int) lineWidth;
                textView.selectionEndIndex = indexCount;
            }

            // 文字超过右边了，不渲染
            if (!textView.horizontalOverflow && lineWidth > textView.width) {
                continue;
            }
            // 文字超过底下了，没救了
            if (!textView.verticalOverflow && totalHeight > textView.height) {
                lastRenderedIndex = indexCount;
                break;
            }
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

        // render text selection :)
        RenderUtils.awtColor(textView.selectionColor);
        if (lineDiff > 0) {
            // drag to down
            if (mouseEndPosY > textView.height) {
                // cursor under the textView
                textView.selectionEndIndex = lastRenderedIndex;
                RenderUtils.drawRect(
                        textView.posX + selectionBeginPosX,
                        textView.posY + lineHeight * selectionBeginLine,
                        textView.posX + textView.width,
                        textView.posY + lineHeight * (selectionBeginLine + 1)
                );
                for (int i = selectionBeginLine + 1; i < lines; i++) {
                    RenderUtils.drawRect(
                            textView.posX,
                            textView.posY + lineHeight * i,
                            textView.posX + textView.width,
                            textView.posY + lineHeight * (i + 1)
                    );
                }
            } else {
                // cursor in the textView
                RenderUtils.drawRect(
                        textView.posX + selectionBeginPosX,
                        textView.posY + lineHeight * selectionBeginLine,
                        textView.posX + textView.width,
                        textView.posY + lineHeight * (selectionBeginLine + 1)
                );
                for (int i = selectionBeginLine + 1; i < selectionEndLine; i++) {
                    RenderUtils.drawRect(
                            textView.posX,
                            textView.posY + lineHeight * i,
                            textView.posX + textView.width,
                            textView.posY + lineHeight * (i + 1)
                    );
                }
                RenderUtils.drawRect(
                        textView.posX,
                        textView.posY + lineHeight * selectionEndLine,
                        textView.posX + selectionEndPosX,
                        textView.posY + lineHeight * (selectionEndLine + 1)
                );
            }
            // ==== //
        } else if (lineDiff < 0) {
            // drag to up
            if (mouseEndPosY < 0) {
                // cursor over the textView
                textView.selectionEndIndex = 0;
                RenderUtils.drawRect(
                        textView.posX,
                        textView.posY + lineHeight * selectionBeginLine,
                        textView.posX + selectionBeginPosX,
                        textView.posY + lineHeight * (selectionBeginLine + 1)
                );
                for (int i = 0; i < selectionBeginLine; i++) {
                    RenderUtils.drawRect(
                            textView.posX,
                            textView.posY + lineHeight * i,
                            textView.posX + textView.width,
                            textView.posY + lineHeight * (i + 1)
                    );
                }
            } else {
                // cursor in the textView
                RenderUtils.drawRect(
                        textView.posX,
                        textView.posY + lineHeight * selectionBeginLine,
                        textView.posX + selectionBeginPosX,
                        textView.posY + lineHeight * (selectionBeginLine + 1)
                );
                for (int i = selectionEndLine + 1; i < selectionBeginLine; i++) {
                    RenderUtils.drawRect(
                            textView.posX,
                            textView.posY + lineHeight * i,
                            textView.posX + textView.width,
                            textView.posY + lineHeight * (i + 1)
                    );
                }
                RenderUtils.drawRect(
                        textView.posX + selectionEndPosX,
                        textView.posY + lineHeight * selectionEndLine,
                        textView.posX + textView.width,
                        textView.posY + lineHeight * (selectionEndLine + 1)
                );
            }

            // ==== //
        } else {
            // single line selection
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
            // ==== //
        }

        GL11.glColor4f(1, 1, 1, 1);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
}
