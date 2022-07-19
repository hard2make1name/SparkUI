package none.spark;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = SparkUI.MODID, name = SparkUI.NAME, version = SparkUI.VERSION)
public class SparkUI {
    public static final String MODID = "sparkui";
    public static final String NAME = "SparkUI";
    public static final String VERSION = "1.0";

    @EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.print("[SparkUI FML]");
        for(int i=0;i<50;i++){
            System.out.print("LO");
        }
    }
}
