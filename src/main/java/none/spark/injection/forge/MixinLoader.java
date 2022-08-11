package none.spark.injection.forge;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import none.spark.injection.transformer.AbstractJavaLinkerTransformer;
import none.spark.injection.transformer.ForgeNetworkTransformer;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Map;

public class MixinLoader implements IFMLLoadingPlugin {

    public MixinLoader() {
        System.out.println("[Spark] Injecting with IFMLLoadingPlugin.");

        MixinBootstrap.init();
        Mixins.addConfiguration("spark.forge.mixins.json");
        MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.CLIENT);
    }

    @Override
    public String[] getASMTransformerClass() {
        // This code in TransformerLoader is not effective :(
        // so I try to place the code there, then it works :)
        return new String[]{
                AbstractJavaLinkerTransformer.class.getName(),
                ForgeNetworkTransformer.class.getName()
        };
    }
    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
