package none.spark;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import none.spark.injection.remapper.Remapper;
import none.spark.manager.FileManager;
import none.spark.manager.FontManager;
import none.spark.manager.GLCapStack;
import none.spark.ui.font.FontRenderer;
import none.spark.ui.font.GlyphPool;
import none.spark.utils.JarUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Mod(modid = SparkUI.MODID, name = SparkUI.NAME, version = SparkUI.VERSION)
public class SparkUI {
    public static final String MODID = "sparkui";
    public static final String NAME = "SparkUI";
    public static final String VERSION = "1.0.0";
    public Minecraft mc;
    public File root;
    public FileManager fileManager;
    public FontManager fontManager;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println("[SparkUI] FML Initialization Event!");
    }

    public void run() {
        System.out.println("[SparkUI] Client start running!");
        this.mc = Minecraft.getMinecraft();
        this.root = new File(this.mc.mcDataDir, "SparkUI");

        this.fileManager = new FileManager(this.root);
        this.fileManager.addDir("fonts");
        this.fileManager.setupDirs();

        // remap
        try {
            Remapper.parseSrg(JarUtils.getResourceInputStream("/mcp-srg.srg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Copy fonts to local config directory
        try {
            BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(JarUtils.getResourceInputStream("/fonts/list"), StandardCharsets.UTF_8));

            String name;
            while ((name = reader.readLine()) != null) {
                File localFont = new File(this.fileManager.getDir("fonts"), name);
                if (!localFont.exists()) {
                    // copy
                    InputStream is = JarUtils.getResourceInputStream("/fonts/" + name);
                    FileOutputStream fos = new FileOutputStream(localFont);
                    byte[] b = new byte[1024];
                    int length;
                    while ((length = is.read(b)) != -1) {
                        fos.write(b, 0, length);
                    }
                    fos.close();
                    is.close();
                    System.out.println("[SparkUI] " + name + " has copy to " + localFont.getAbsolutePath());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.fontManager = new FontManager(this.fileManager.getDir("fonts"));
        this.fontManager.font50 = new FontRenderer(this.fontManager.getFont("WenQuanYiMicroHeiMono.ttf"));
        this.fontManager.addSubstituteGlyphPool(new GlyphPool(this.fontManager.getFont("NotoEmoji-VariableFont_wght.ttf")));
        // 也不用判断是不是Emoji了，你canDisplay你就上
        Statics.sparkUI = this;
        Statics.font50 = this.fontManager.font50;
        Statics.fontManager = this.fontManager;
    }
}
