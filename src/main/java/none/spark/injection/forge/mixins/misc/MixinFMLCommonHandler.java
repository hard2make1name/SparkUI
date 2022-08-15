package none.spark.injection.forge.mixins.misc;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.IFMLSidedHandler;
import net.minecraftforge.fml.common.Loader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = FMLCommonHandler.class, remap = false)
public class MixinFMLCommonHandler {
    @Shadow
    private List<String> brandings;
    @Shadow
    private List<String> brandingsNoMC;
    @Shadow
    private IFMLSidedHandler sidedDelegate;

    @Inject(cancellable = true, method = "computeBranding", at = @At("HEAD"))
    //@Inject(remap = false, cancellable = true,method = "computeBranding", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList/Builder;add(T)com/google/common/collect/ImmutableList/Builder;"))
    public void computeBrandingMixin(CallbackInfo callbackInfo) {
        if (brandings == null) {
            ImmutableList.Builder<String> brd = ImmutableList.builder();
            brd.add("SparkUI");
            brd.add(Loader.instance().getMCVersionString());
            brd.add(Loader.instance().getMCPVersionString());
            brd.add("Powered by Forge " + ForgeVersion.getVersion());
            if (sidedDelegate != null) {
                brd.addAll(sidedDelegate.getAdditionalBrandingInformation());
            }
            if (Loader.instance().getFMLBrandingProperties().containsKey("fmlbranding")) {
                brd.add(Loader.instance().getFMLBrandingProperties().get("fmlbranding"));
            }
            int tModCount = Loader.instance().getModList().size();
            int aModCount = Loader.instance().getActiveModList().size();
            brd.add(String.format("%d mod%s loaded, %d mod%s active", tModCount, tModCount != 1 ? "s" : "", aModCount, aModCount != 1 ? "s" : ""));
            brandings = brd.build();
            brandingsNoMC = brandings.subList(1, brandings.size());
        }
        callbackInfo.cancel();
    }
}
