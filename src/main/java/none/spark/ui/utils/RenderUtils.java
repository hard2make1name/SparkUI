package none.spark.ui.utils;

import none.spark.manager.GLCapStack;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public final class RenderUtils {

    // In Java, float or double? https://www.cnblogs.com/zzzz76/p/6881805.html

    public static double _PI_divide_180 = (Math.PI / 180);
    public static double _180_divide_PI = (180 / Math.PI);

    public static int getRGB(int r, int g, int b) {
        return ((255 & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF) << 0);
    }

    public static int getARGB(int a, int r, int g, int b) {
        return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF) << 0);
    }

    public static void colorHex(int hex) {
        // RGBA
        GL11.glColor4f(
                (hex >> 16 & 0xFF) / 255f,
                (hex >> 8 & 0xFF) / 255f,
                (hex >> 0 & 0xFF) / 255f,
                (hex >> 24 & 0xFF) / 255f
        );
    }

    public static void drawRect(double x, double y, double x2, double y2) {
        GL11.glBegin(GL11.GL_QUADS);

        GL11.glColor3f(1.0f, 0, 0);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glColor3f(0, 0, 1.0f);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);

        GL11.glEnd();
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
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

    public static @NotNull ByteBuffer readImageToBuffer(@NotNull BufferedImage bufferedImage) {

        int[] rgb = bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null, 0, bufferedImage.getWidth());
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * rgb.length);

        for (int i : rgb) {
            byteBuffer.putInt(i << 8 | i >> 24 & 255);
        }
        byteBuffer.flip();
        return byteBuffer;
    }

    // 纹理: https://www.jianshu.com/p/1b327789220d
    // 设置纹理包装和过滤的方式
    // GL_CLAMP_TP_EDGE 和 GL_CLAMP_TO_BORDER 和 GL_MIRRORED_REPEAT会报错 1280: Invalid enum
    // May be because the enum is not available in this lwjgl
    public static int uploadTexture(@NotNull BufferedImage bufferedImage) {

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
    // (0.0f, 0.0f)、(0.0f, 1.0f)、(1.0f, 1.0f)、(1.0f, 0.0f) 分别对应左下、左上、右上、右下角
    // TexCoord 是上下翻转的
    // TODO 有些东西不会获取状态
    public static void drawImage(int textureId, float x, float y, float x2, float y2) {
        GLCapStack.push(GL11.GL_TEXTURE_2D,GL11.GL_BLEND);

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

        GLCapStack.pop(2);
    }
}