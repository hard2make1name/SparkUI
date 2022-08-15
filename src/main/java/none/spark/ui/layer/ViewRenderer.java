package none.spark.ui.layer;

import none.spark.Statics;
import none.spark.manager.GLCapStack;
import none.spark.ui.UIStatics;
import none.spark.ui.font.Glyph;
import none.spark.ui.font.GlyphPool;
import none.spark.ui.layer.views.TextField;
import none.spark.ui.util.RenderUtils;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

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

    public static final int CODEPOINT_NEW_LINE = "\n".codePointAt(0);
    public static final int CODEPOINT_SPACE = " ".codePointAt(0);


    public void renderTextField(TextField textField) {
        // 上色
        RenderUtils.awtColor(textField.fontColor);

        // 限制渲染区域
        if (!textField.debugNotScissor) {
            int scissorWidth = textField.width, scissorHeight = textField.height;
            if (textField.horizontalOverflow) {
                scissorWidth = UIStatics.gameCanvas.width - textField.posX;
            }
            if (textField.verticalOverflow) {
                scissorHeight = UIStatics.gameCanvas.height - textField.posY;
            }
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            RenderUtils.glScissorVerticalFlipped(textField.posX, textField.posY, scissorWidth, scissorHeight);
        }

        int lineWidth = 0;// 起始点是左上角
        int lineHeight = glyphPool.getFontMetrics(textField.fontSize).getHeight();
        int totalHeight = 0;// 起始点是左上角
        int lineCount = 0;
        ArrayList<Integer> lineWidthList = new ArrayList<>();
        ArrayList<Integer> lineLastIndexList = new ArrayList<>();

        Glyph spaceGlyph = getGlyph(CODEPOINT_SPACE, textField.fontSize, textField.fontSubstitute);

        // 这些坐标是相对于 textField 的左上角的
        int mouseBeginPosX = textField.mouseBeginPosX - textField.posX;
        int mouseBeginPosY = textField.mouseBeginPosY - textField.posY;
        int selectionBeginLine = ((mouseBeginPosY - mouseBeginPosY % lineHeight) / lineHeight);
        int selectionBeginPosX = 0;
        boolean isSelectionBeginCaught = false;

        int mouseEndPosX = textField.mouseEndPosX - textField.posX;
        int mouseEndPosY = textField.mouseEndPosY - textField.posY;
        int selectionEndLine = ((mouseEndPosY - mouseEndPosY % lineHeight) / lineHeight);
        int selectionEndPosX = 0;
        boolean isSelectionEndCaught = false;

        // 当 totalHeight + lineHeight > textField.height 时
        // 进入底部临界，之后的文字在 glScissor 后可能会只见一半

        int[] codePoints = textField.text.codePoints().toArray();
        Glyph finalGlyph;
        int indexCount = 0;
        for (int codePoint : codePoints) {
            if (codePoint == CODEPOINT_NEW_LINE) {
                if (textField.lineWrap) {
                    if (!textField.verticalOverflow) {
                        if (totalHeight + lineHeight > textField.height) {
                            // 已经底部临界，再换行就看不到了
                            break;
                        } else {
                            lineWidthList.add(lineWidth);
                            lineLastIndexList.add(indexCount);

                            lineWidth = 0;
                            totalHeight += lineHeight;
                            lineCount++;
                        }
                    }
                } else {
                    lineWidth += spaceGlyph.width;
                }
            }
            finalGlyph = getGlyph(codePoint, textField.fontSize, textField.fontSubstitute);

            // autoLineWrap
            if (textField.autoLineWrap && lineWidth + finalGlyph.width > textField.width) {
                if (!textField.verticalOverflow) {
                    if (totalHeight + lineHeight > textField.height) {
                        // 已经底部临界，再换行就看不到了
                        break;
                    } else {
                        lineWidthList.add(lineWidth);
                        lineLastIndexList.add(indexCount);

                        lineWidth = 0;
                        totalHeight += lineHeight;
                        lineCount++;
                    }
                }
            }

            if (textField.selectable) {
                // catch the beginning of cursor
                if (
                        !isSelectionBeginCaught &&
                                lineCount == selectionBeginLine &&
                                mouseBeginPosX >= lineWidth - finalGlyph.width / 2f &&
                                mouseBeginPosX < lineWidth + finalGlyph.width / 2f
                ) {
                    selectionBeginPosX = lineWidth;
                    textField.selectionBeginIndex = indexCount;
                    isSelectionBeginCaught = true;
                }
                // catch the ending of cursor
                if (
                        !isSelectionEndCaught &&
                                lineCount == selectionEndLine &&
                                mouseEndPosX >= lineWidth - finalGlyph.width / 2f &&
                                mouseEndPosX < lineWidth + finalGlyph.width / 2f
                ) {
                    selectionEndPosX = lineWidth;
                    textField.selectionEndIndex = indexCount;
                    isSelectionEndCaught = true;
                }
            }

            RenderUtils.drawImage(
                    finalGlyph.textureId,
                    textField.posX + lineWidth,
                    textField.posY + totalHeight,
                    textField.posX + lineWidth + finalGlyph.width,
                    textField.posY + totalHeight + finalGlyph.height
            );

            lineWidth += finalGlyph.width;
            indexCount++;
        }
        lineWidthList.add(lineWidth);
        lineLastIndexList.add(indexCount);

        if (textField.selectable) {
            if (selectionBeginLine > lineCount) selectionBeginLine = lineCount;
            if (selectionEndLine > lineCount) selectionEndLine = lineCount;
            //System.out.print(0);
            if (!isSelectionBeginCaught) {
                // the cursor down on the textView at first, but not touch any character
                if (mouseBeginPosX > lineWidthList.get(selectionBeginLine)) {
                    selectionBeginPosX = lineWidthList.get(selectionBeginLine);
                    textField.selectionBeginIndex = lineLastIndexList.get(selectionBeginLine);
                }
            }
            if (!isSelectionEndCaught) {
                // the cursor down on the textView at last, but not touch any character
                if (mouseEndPosX > lineWidthList.get(selectionEndLine) || mouseEndPosY > lineCount*lineHeight) {
                    selectionEndPosX = lineWidthList.get(selectionEndLine);
                    textField.selectionEndIndex = lineLastIndexList.get(selectionEndLine);
                }
            }
        }
        System.out.print(textField.selectionBeginIndex+"|"+textField.selectionEndIndex+"\n");

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
                    textField.posX + lineWidthList.get(selectionBeginLine),
                    textField.posY + lineHeight * (selectionBeginLine + 1)
            );
            for (int i = selectionBeginLine + 1; i < selectionEndLine; i++) {
                RenderUtils.drawRect(
                        textField.posX,
                        textField.posY + lineHeight * i,
                        textField.posX + lineWidthList.get(i),
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
                        textField.posX + lineWidthList.get(i),
                        textField.posY + lineHeight * (i + 1)
                );
            }
            RenderUtils.drawRect(
                    textField.posX + selectionEndPosX,
                    textField.posY + lineHeight * selectionEndLine,
                    textField.posX + lineWidthList.get(selectionEndLine),
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

        if (textField.showCursor) {
            GL11.glColor4f(0, 1, 0, 1);
            GL11.glLineWidth(2f);
            RenderUtils.drawLine(
                    textField.posX + selectionEndPosX,
                    textField.posY + selectionEndLine * lineHeight,
                    textField.posX + selectionEndPosX,
                    textField.posY + (selectionEndLine + 1) * lineHeight
            );
        }

        if (textField.debugOutline) {
            GL11.glColor4f(1, 0, 0, 1);
            GL11.glLineWidth(5f);
            RenderUtils.drawRectBorder(
                    textField.posX,
                    textField.posY,
                    textField.posX + textField.width,
                    textField.posY + textField.height
            );
        }

        GLCapStack.pop();
        if (!textField.debugNotScissor) {
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        }

        GL11.glColor4f(1, 1, 1, 1);
    }
}
