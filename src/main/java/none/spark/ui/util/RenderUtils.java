package none.spark.ui.util;

import none.spark.manager.GLCapStack;
import none.spark.ui.UIStatics;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public final class RenderUtils {

    // In Java, float or double? https://www.cnblogs.com/zzzz76/p/6881805.html
    // I prefer float, I think it isn't necessary to use double

    // 不会用 GL11 就去学，别再表演用 4 个 drawRect 画边框的神奇操作出来
    // If you don't know how to use GL11, don't perform the magic of drawing borders with 4 drawRect()
    // 有关于 GL11.glEnable() 和 GL11.glDisable() 的操作时，在前面加上一句GLCapStack.push()
    //
    public static final double _PI_divide_180 = (Math.PI / 180);
    public static final double _180_divide_PI = (180 / Math.PI);

    public static int getRGB(int r, int g, int b) {
        return ((0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF));
    }

    public static int getARGB(int a, int r, int g, int b) {
        return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF));
    }

    public static void colorHex(int hex) {
        // RGBA
        GL11.glColor4f(
                (hex >> 16 & 0xFF) / 255f,
                (hex >> 8 & 0xFF) / 255f,
                (hex & 0xFF) / 255f,
                (hex >> 24 & 0xFF) / 255f
        );
    }

    public static void awtColor(Color color) {
        GL11.glColor4f(
                (color.getRed()) / 255.0f,
                (color.getGreen()) / 255.0f,
                (color.getBlue()) / 255.0f,
                (color.getAlpha()) / 255.0f
        );
    }

    public static void glScissorVerticalFlipped(int posX, int posY, int width, int height) {
        // if you use original glScissor, you will notice that posY starts at the bottom of screen
        GL11.glScissor(posX, UIStatics.gameCanvas.height - posY - height, width, height);
    }

    public static void drawRect(float x, float y, float x2, float y2) {
        //Gui.drawRect((int)x, (int) y,(int)x2,(int)y2,RenderUtils.getARGB(128,255,255,255));
        GL11.glBegin(GL11.GL_QUADS);

        GL11.glVertex2f(x2, y);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x, y2);
        GL11.glVertex2f(x2, y2);

        GL11.glEnd();
    }

    public static void drawRectBorder(float x, float y, float x2, float y2) {
        GL11.glBegin(GL11.GL_LINE_LOOP);

        GL11.glVertex2f(x2, y);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x, y2);
        GL11.glVertex2f(x2, y2);

        GL11.glEnd();
    }

    public static double toRadians(double degrees) {
        return degrees * _PI_divide_180;
    }

    public static double toDegrees(double radians) {
        return radians * _180_divide_PI;
    }

    public static void drawHollowCircle(double x, double y, double radius) {
        int sections = 50;
        double dAngle = 2 * Math.PI / sections;
        double xx;
        double yy;

        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(1);

        GL11.glBegin(GL11.GL_LINE_LOOP);
        for (int i = 0; i < sections; i++) {
            xx = (radius * Math.sin(i * dAngle));
            yy = (radius * Math.cos(i * dAngle));
            GL11.glVertex2d(xx + x, yy + y);
        }
        GL11.glEnd();

        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    public static void drawFilledCircle(double x, double y, double radius) {
        int sections = 50;
        double dAngle = 2 * Math.PI / sections;
        double xx;
        double yy;

        GL11.glBegin(GL11.GL_POLYGON);
        for (int i = 0; i < sections; i++) {
            xx = (radius * Math.sin(i * dAngle));
            yy = (radius * Math.cos(i * dAngle));
            GL11.glVertex2d(xx + x, yy + y);
        }
        GL11.glEnd();
    }

    public static void drawArc(double x, double y, double radius, double degree1, double degree2) {
        if (degree1 > degree2) {
            double _deg = degree1;
            degree1 = degree2;
            degree2 = _deg;
        }

        int sections = 20;
        double degreePerSection = (degree2 - degree1) / sections;

        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(1);

        GL11.glBegin(GL11.GL_LINE_STRIP);

        GL11.glVertex2d(
                x + (radius * Math.sin(toRadians(degree1))),
                y + (radius * Math.cos(toRadians(degree1)))
        );
        for (int i = 0; i < sections; i++) {
            GL11.glVertex2d(
                    x + (radius * Math.sin(toRadians(i * degreePerSection))),
                    y + (radius * Math.cos(toRadians(i * degreePerSection)))
            );
        }
        GL11.glVertex2d(
                x + (radius * Math.sin(toRadians(degree2))),
                y + (radius * Math.cos(toRadians(degree2)))
        );

        GL11.glEnd();
    }

    public static void drawHollowFan(double x, double y, double radius, double degree1, double degree2) {
        if (degree1 > degree2) {
            double _deg = degree1;
            degree1 = degree2;
            degree2 = _deg;
        }

        int sections = 20;
        double degreePerSection = (degree2 - degree1) / sections;

        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(1);

        GL11.glBegin(GL11.GL_LINE_STRIP);

        GL11.glVertex2d(x, y);
        GL11.glVertex2d(
                x + (radius * Math.sin(toRadians(degree1))),
                y + (radius * Math.cos(toRadians(degree1)))
        );
        for (int i = 0; i < sections; i++) {
            GL11.glVertex2d(
                    x + (radius * Math.sin(toRadians(i * degreePerSection))),
                    y + (radius * Math.cos(toRadians(i * degreePerSection)))
            );
        }
        GL11.glVertex2d(
                x + (radius * Math.sin(toRadians(degree2))),
                y + (radius * Math.cos(toRadians(degree2)))
        );
        GL11.glVertex2d(x, y);

        GL11.glEnd();
    }

    public static void drawFilledFan(double x, double y, double radius, double degree1, double degree2) {
        if (degree1 > degree2) {
            double _deg = degree1;
            degree1 = degree2;
            degree2 = _deg;
        }

        int sections = 20;
        double degreePerSection = (degree2 - degree1) / sections;

        GL11.glBegin(GL11.GL_POLYGON);

        GL11.glVertex2d(x, y);
        GL11.glVertex2d(
                x + (radius * Math.sin(toRadians(degree1))),
                y + (radius * Math.cos(toRadians(degree1)))
        );
        for (int i = 0; i < sections; i++) {
            GL11.glVertex2d(
                    x + (radius * Math.sin(toRadians(i * degreePerSection))),
                    y + (radius * Math.cos(toRadians(i * degreePerSection)))
            );
        }
        GL11.glVertex2d(
                x + (radius * Math.sin(toRadians(degree2))),
                y + (radius * Math.cos(toRadians(degree2)))
        );

        GL11.glEnd();
    }

    public static ByteBuffer readImageToBuffer(BufferedImage bufferedImage) {

        int[] rgb = bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null, 0, bufferedImage.getWidth());
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * rgb.length);

        for (int i : rgb) {
            byteBuffer.putInt(i << 8 | i >> 24 & 255);
        }
        // Linux Open JDK 8 doesn't support byteBuffer.flip();
        ((Buffer) byteBuffer).flip();
        return byteBuffer;
    }

    // 纹理: https://www.jianshu.com/p/1b327789220d
    // 设置纹理包装和过滤的方式
    // GL_CLAMP_TP_EDGE 和 GL_CLAMP_TO_BORDER 和 GL_MIRRORED_REPEAT会报错 1280: Invalid enum
    // May be because the enum is not available in this lwjgl
    public static int uploadTexture(BufferedImage bufferedImage) {

        int textureId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        GL11.glTexImage2D(
                GL11.GL_TEXTURE_2D,
                0,
                GL11.GL_RGBA,
                bufferedImage.getWidth(),
                bufferedImage.getHeight(),
                0,
                GL11.GL_RGBA,
                GL11.GL_UNSIGNED_BYTE,
                readImageToBuffer(bufferedImage)
        );
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        return textureId;
    }

    // https://www.cnblogs.com/1024Planet/p/5667110.html
    // 在 TexCoord 中 (0.0f, 0.0f)、(0.0f, 1.0f)、(1.0f, 1.0f)、(1.0f, 0.0f) 分别对应左下、左上、右上、右下角
    // In TexCoord, (0.0f, 0.0f)、(0.0f, 1.0f)、(1.0f, 1.0f)、(1.0f, 0.0f) correspond to lower left, upper left, upper right, and lower right
    // TexCoord 's position is vertical flipped
    // TODO I don't know how to get the state of glShadeModel and glBlendFunc to restore the state after rendering

    public static void drawImage(int textureId, float x, float y, float x2, float y2) {
        GLCapStack.push(GL11.GL_BLEND, GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

        GL11.glBegin(GL11.GL_QUADS);

        GL11.glTexCoord2f(1, 0);//右下
        GL11.glVertex2f(x2, y);//右上
        GL11.glTexCoord2f(0, 0);//左下
        GL11.glVertex2f(x, y);//左上
        GL11.glTexCoord2f(0, 1);//左上
        GL11.glVertex2f(x, y2);//左下
        GL11.glTexCoord2f(1, 1);//右上
        GL11.glVertex2f(x2, y2);//右下

        GL11.glEnd();
    }
}