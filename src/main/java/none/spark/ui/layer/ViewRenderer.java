package none.spark.ui.layer;

import none.spark.Statics;
import none.spark.manager.GLCapStack;
import none.spark.ui.UIStatics;
import none.spark.ui.font.Glyph;
import none.spark.ui.font.GlyphPool;
import none.spark.ui.layer.views.TextField;
import none.spark.ui.util.RenderUtils;
import org.lwjgl.opengl.GL11;

public class ViewRenderer {
    public static final GlyphPool glyphPool = UIStatics.glyphPool;

    public Glyph getGlyph(int codePoint, int fontSize, boolean fontSubstitute) {
        //  本来想在 TextField 存 Glyph 缓存的以减少 ViewRenderer 工作量
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

//    public void updateTextViewGlyphList(TextField textView) {
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
            } else if (view instanceof TextField) {
                this.renderTextField((TextField) view);
            }
        }
    }

    public void renderTextField(TextField textField) {
        // 上色
        RenderUtils.awtColor(textField.fontColor);

        // 限制渲染区域
//        int scissorWidth = textField.width, scissorHeight = textField.height;
//        if (textField.horizontalOverflow) {
//            scissorWidth = UIStatics.gameCanvas.width - textField.posX;
//        }
//        if (textField.verticalOverflow) {
//            scissorHeight = UIStatics.gameCanvas.height - textField.posY;
//        }
//        GL11.glEnable(GL11.GL_SCISSOR_TEST);
//        RenderUtils.glScissorVerticalFlipped(textField.posX, textField.posY, scissorWidth, scissorHeight);

        float lineWidth = 0;// 起始点是左上角
        float lineHeight = glyphPool.getFontMetrics(textField.fontSize).getHeight();
        float totalHeight = 0;// 起始点是左上角
        int lines = 0;
        float spaceWidth = getGlyph(CODEPOINT_SPACE, textField.fontSize, textField.fontSubstitute).width;

        // 这些坐标是相对于 textField 的左上角的
        int mouseBeginPosX = textField.mouseBeginPosX - textField.posX;
        int mouseBeginPosY = textField.mouseBeginPosY - textField.posY;
        int selectionBeginLine = ((mouseBeginPosY - mouseBeginPosY % (int) lineHeight) / (int) lineHeight);
        int selectionBeginPosX = 0;
        boolean isSelectionBeginCaught = false;

        int mouseEndPosX = textField.mouseEndPosX - textField.posX;
        int mouseEndPosY = textField.mouseEndPosY - textField.posY;
        int selectionEndLine = ((mouseEndPosY - mouseEndPosY % (int) lineHeight) / (int) lineHeight);
        int selectionEndPosX = 0;
        boolean isSelectionEndCaught = false;

        int lastRenderedIndex = 0;
        int lastRenderedPosX = 0;
        int lastRenderedPosY = 0;

        // 当 totalHeight > textField.height 时
        // 进入底部临界，之后的文字在 glScissor 后可能会只见一半

        boolean bottomCritical = false;

        int[] codePoints = textField.text.codePoints().toArray();
        Glyph finalGlyph;
        int indexCount = 0;
        for (int codePoint : codePoints) {
            // 渲染前，数据未更改

            // 控制符逻辑
            if (codePoint == CODEPOINT_NEW_LINE) {
                if (textField.lineWrap) {
                    if (!textField.verticalOverflow) {
                        if (totalHeight + lineHeight > textField.height) {
                            // 已经底部临界，再换行就看不到了
                            break;
                        } else {
                            lineWidth = 0;
                            totalHeight += lineHeight;
                            lines++;
                        }
                    } else {
                        lineWidth = 0;
                        totalHeight += lineHeight;
                        lines++;
                    }
                } else {
                    lineWidth += spaceWidth * 2;
                }
                continue;
            }

            finalGlyph = getGlyph(codePoint, textField.fontSize, textField.fontSubstitute);

            // autoLineWrap
//            if (textField.autoLineWrap && lineWidth + finalGlyph.width > textField.width) {
//                lineWidth = 0;
//                totalHeight += lineHeight;
//                lines++;
//            }
            if (textField.autoLineWrap && lineWidth + finalGlyph.width > textField.width) {
                if (!textField.verticalOverflow) {
                    if (totalHeight + lineHeight > textField.height) {
                        // 已经底部临界，再换行就看不到了
                        break;
                    } else {
                        lineWidth = 0;
                        totalHeight += lineHeight;
                        lines++;
                    }
                } else {
                    lineWidth = 0;
                    totalHeight += lineHeight;
                    lines++;
                }
            }

            // 抓取鼠标起始位置
            if (lines == selectionBeginLine && mouseBeginPosX > lineWidth - finalGlyph.width / 2f && mouseBeginPosX < lineWidth + finalGlyph.width / 2f && selectionBeginPosX == 0) {
                selectionBeginPosX = (int) lineWidth;// 字符的左边
                textField.selectionBeginIndex = indexCount;
                isSelectionBeginCaught = true;
            }
            // 抓取鼠标结束位置
            if (lines == selectionEndLine && mouseEndPosX >= lineWidth + finalGlyph.width / 2f && mouseEndPosX <= lineWidth + finalGlyph.width + finalGlyph.width / 2f && selectionEndPosX == 0) {
                selectionEndPosX = (int) lineWidth + finalGlyph.width;// 字符的右边
                textField.selectionEndIndex = indexCount + 1;
                isSelectionEndCaught = true;
            }

            lastRenderedPosX = (int) lineWidth + finalGlyph.width;// 字符的右边
            lastRenderedPosY = (int) lineHeight * lines;
            lastRenderedIndex = indexCount;

            RenderUtils.drawImage(
                    finalGlyph.textureId,
                    textField.posX + lineWidth,
                    textField.posY + totalHeight,
                    textField.posX + lineWidth + finalGlyph.width,
                    textField.posY + totalHeight + finalGlyph.height
            );
            // 渲染后，数据已更改
            lineWidth += finalGlyph.width;
            indexCount++;
        }
        if (!isSelectionBeginCaught) {
            // the cursor down on the textView at first, but not touch any character
            selectionBeginPosX = lastRenderedPosX;
            selectionBeginLine = lines;
            textField.selectionBeginIndex = textField.text.length();
        }
        if (!isSelectionEndCaught) {
            // the cursor down on the textView at last, but not touch any character
            if (mouseEndPosY > lastRenderedPosY) {
                selectionEndPosX = lastRenderedPosX;
                selectionEndLine = lines;
                textField.selectionEndIndex = textField.text.length() - 1;
            } else if (mouseEndPosY < 0) {
                // selectionEndPosX = 0;
                selectionEndLine = 0;
                textField.selectionEndIndex = 0;
            }
        }

        // render text selection :)
        int lineDiff = selectionEndLine - selectionBeginLine;

        GLCapStack.push(GL11.GL_BLEND, GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        RenderUtils.awtColor(textField.selectionColor);
        if (lineDiff > 0) {
            RenderUtils.drawRect(
                    textField.posX + selectionBeginPosX,
                    textField.posY + lineHeight * selectionBeginLine,
                    textField.posX + textField.width,
                    textField.posY + lineHeight * (selectionBeginLine + 1)
            );
            for (int i = selectionBeginLine + 1; i < selectionEndLine; i++) {
                RenderUtils.drawRect(
                        textField.posX,
                        textField.posY + lineHeight * i,
                        textField.posX + textField.width,
                        textField.posY + lineHeight * (i + 1)
                );
            }
            RenderUtils.drawRect(
                    textField.posX,
                    textField.posY + lineHeight * selectionEndLine,
                    textField.posX + selectionEndPosX,
                    textField.posY + lineHeight * (selectionEndLine + 1)
            );
        } else if (lineDiff < 0) {
            RenderUtils.drawRect(
                    textField.posX,
                    textField.posY + lineHeight * selectionBeginLine,
                    textField.posX + selectionBeginPosX,
                    textField.posY + lineHeight * (selectionBeginLine + 1)
            );
            for (int i = selectionEndLine + 1; i < selectionBeginLine; i++) {
                RenderUtils.drawRect(
                        textField.posX,
                        textField.posY + lineHeight * i,
                        textField.posX + textField.width,
                        textField.posY + lineHeight * (i + 1)
                );
            }
            RenderUtils.drawRect(
                    textField.posX + selectionEndPosX,
                    textField.posY + lineHeight * selectionEndLine,
                    textField.posX + textField.width,
                    textField.posY + lineHeight * (selectionEndLine + 1)
            );
        } else {
            RenderUtils.drawRect(
                    textField.posX + selectionBeginPosX,
                    textField.posY + lineHeight * selectionBeginLine,
                    textField.posX + selectionEndPosX,
                    textField.posY + lineHeight * (selectionBeginLine + 1)
            );
        }
        GLCapStack.pop();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glColor4f(1, 1, 1, 1);
    }
}
