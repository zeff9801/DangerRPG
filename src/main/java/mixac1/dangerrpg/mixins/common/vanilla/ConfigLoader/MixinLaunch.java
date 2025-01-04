package mixac1.dangerrpg.mixins.common.vanilla.ConfigLoader;

import mixac1.dangerrpg.DangerRPG;
import mixac1.dangerrpg.config.DangerConfig;
import net.minecraft.launchwrapper.Launch;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Launch.class)
public class MixinLaunch {

    @Inject(method = "launch", at = @At("HEAD"),remap = false)
    private void onLaunch(String[] args,CallbackInfo ci) {
        DangerRPG.config = new DangerConfig("dangerrpgtest");
        DangerRPG.config.loadConfig();
    }
}
