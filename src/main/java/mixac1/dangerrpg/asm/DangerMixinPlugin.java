package mixac1.dangerrpg.asm;

import fr.iamacat.api.asm.CatMixinPlugin;
import mixac1.dangerrpg.DangerRPG;
import mixac1.dangerrpg.init.RPGConfig;

public class DangerMixinPlugin extends CatMixinPlugin {

    @Override
    public void onLoad(String mixinPackage) {
        RPGConfig.mixinConfig.load();
        if (RPGConfig.MixinConfig.d.enableVanillaArrowReplacement) {
            addMixin("common.vanilla.ArrowReplacement.MixinEntityArrow");
        }
        if (RPGConfig.MixinConfig.d.enableArmorSystemReplacement) {
            addMixin("common.vanilla.ArmorSystem.MixinArmorProperties");
        }
        if (RPGConfig.MixinConfig.d.enableEntityTweaking) {
            addMixin("common.vanilla.EntityTweaks.MixinEntity");
            addMixin("common.vanilla.EntityTweaks.MixinEntityLivingBase");
            addMixin("common.vanilla.EntityTweaks.MixinEntityPlayer");
            addMixin("common.vanilla.EntityTweaks.MixinSharedMonsterAttributes");
        }
        if (RPGConfig.MixinConfig.d.enableBowSystem) {
            addMixin("common.vanilla.BowSystem.MixinEntityPlayer");
            addMixin("common.vanilla.BowSystem.MixinEntityPlayerSP");
            addMixin("common.vanilla.BowSystem.MixinItemBow");
        }
        if (RPGConfig.MixinConfig.d.enableItemSystem) {
            addMixin("common.vanilla.ItemSystem.MixinItem");
            addMixin("common.vanilla.ItemSystem.MixinItemStack");
        }
        /*if (RPGConfig.MixinConfig.d.enableFixIncorrectMotions) {
            addMixin("common.vanilla.FixIncorrectMotions.MixinEntitySpawnMessage");
            addMixin("common.vanilla.FixIncorrectMotions.MixinEntityTrackerEntry");
            addMixin("common.vanilla.FixIncorrectMotions.MixinS12PacketEntityVelocity");
        }*/
    }
}
