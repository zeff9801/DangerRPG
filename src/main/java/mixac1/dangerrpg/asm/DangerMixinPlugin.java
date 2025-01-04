package mixac1.dangerrpg.asm;

import fr.iamacat.api.asm.CatMixinPlugin;
import mixac1.dangerrpg.DangerRPG;
import mixac1.dangerrpg.config.DangerConfig;

public class DangerMixinPlugin extends CatMixinPlugin {

    @Override
    public void onLoad(String mixinPackage) {
        DangerRPG.config = new DangerConfig("dangerrpgtest");
        DangerRPG.config.loadConfig();
        if (DangerConfig.enableMixinRenderLiving && TargetedMod.OPTIFINE.isModLoaded()) {
            addMixin("client.vanilla.compatoptifineshaders.MixinRenderLiving", CLIENT);
        }
        if (DangerConfig.enableVanillaArrowReplacement) {
            addMixin("common.vanilla.ArrowReplacement.MixinEntityArrow");
        }
        if (DangerConfig.enableArmorSystemReplacement) {
            addMixin("common.vanilla.ArmorSystem.MixinArmorProperties");
        }
        if (DangerConfig.enableEntityTweaking) {
            addMixin("common.vanilla.EntityTweaks.MixinEntity");
            addMixin("common.vanilla.EntityTweaks.MixinEntityLivingBase");
            addMixin("common.vanilla.EntityTweaks.MixinEntityPlayer");
            addMixin("common.vanilla.EntityTweaks.MixinSharedMonsterAttributes");
        }
        if (DangerConfig.enableBowSystem) {
            addMixin("common.vanilla.BowSystem.MixinEntityPlayer");
            addMixin("common.vanilla.BowSystem.MixinEntityPlayerSP");
            addMixin("common.vanilla.BowSystem.MixinItemBow");
        }
        if (DangerConfig.enableItemSystem) {
            addMixin("common.vanilla.ItemSystem.MixinItem");
            addMixin("common.vanilla.ItemSystem.MixinItemStack");
        }
    }
}
