package none.spark;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import none.spark.event.Event;
import none.spark.event.EventCallback;
import none.spark.event.events.CommandEvent;
import none.spark.injection.remapper.Remapper;
import none.spark.manager.EventManager;
import none.spark.manager.FileManager;
import none.spark.manager.FontManager;
import none.spark.manager.ScriptManager;
import none.spark.ui.font.FontRenderer;
import none.spark.ui.font.GlyphPool;
import none.spark.util.JarUtils;

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
    public EventManager eventManager;
    public ScriptManager scriptManager;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println("[SparkUI] FML Initialization Event!");
    }

    public void run() {
        // 顺序不能乱调
        System.out.println("[SparkUI] Client start running!");

        this.mc = Minecraft.getMinecraft();
        this.root = new File(this.mc.mcDataDir, "SparkUI");

        this.fileManager = new FileManager(this.root);
        this.fileManager.addDir("fonts");
        this.fileManager.addDir("scripts");
        this.fileManager.setupDirs();

        // remap
        try {
            Remapper.parseSrg(JarUtils.getResourceInputStream("/mcp-srg.srg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.copyFontsToLocal();

        this.fontManager = new FontManager(this.fileManager.getDir("fonts"));
        this.fontManager.addSubstituteGlyphPool(new GlyphPool(this.fontManager.getFont("NotoEmoji-VariableFont_wght.ttf")));

        FontRenderer font1 = new FontRenderer(this.fontManager.getFont("WenQuanYiMicroHeiMono.ttf"));
        FontRenderer font2 = new FontRenderer(this.fontManager.getFont("MicrosoftYaHeiMono.ttf"));

        this.eventManager = new EventManager();

        this.configureEvents();

        Statics.sparkUI = this;
        Statics.font1 = font1;
        Statics.font2 = font2;
        Statics.fontManager = this.fontManager;
        Statics.eventManager = this.eventManager;
        this.scriptManager = new ScriptManager(this.fileManager.getDir("scripts"));
        Statics.scriptManager = this.scriptManager;
        this.scriptManager.loadScripts();
        this.scriptManager.runScripts();
    }

    public void copyFontsToLocal(){
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
    }
    public void configureEvents(){
        this.eventManager.addListener("cmd", new EventCallback() {
            @Override
            public <T extends Event> void callback(T _event) {
                CommandEvent event = (CommandEvent) _event;
                switch(event.commandName){
                    case".reloadScripts":
                        Statics.eventManager.clearEventListeners();
                        Statics.sparkUI.configureEvents();
                        Statics.scriptManager.loadScripts();
                        Statics.scriptManager.runScripts();
                        break;
                }
            }
        });
    }
}
