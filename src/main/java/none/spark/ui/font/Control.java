package none.spark.ui.font;

public class Control extends Glyph {

    public final int codePoint;

    public Control(int width, int height, int textureId, int codePoint) {
        super(width, height, textureId);
        this.codePoint = codePoint;
    }
}
