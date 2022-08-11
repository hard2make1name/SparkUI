package none.spark.ui.font;

public class Glyph {
    public final int width;
    public final int height;
    public final int textureId;

    private Glyph() {
        this.width = 0;
        this.height = 0;
        this.textureId = 0;
    }

    public Glyph(int width, int height, int textureId) {
        this.width = width;
        this.height = height;
        this.textureId = textureId;
    }

}
