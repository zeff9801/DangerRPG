package mixac1.dangerrpg.asm;

import java.util.*;
import java.util.function.Predicate;

import com.falsepattern.lib.mixin.IMixin;
import com.falsepattern.lib.mixin.ITargetedMod;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import mixac1.dangerrpg.config.DangerConfig;

@RequiredArgsConstructor
public enum Mixin implements IMixin {

    client_vanilla_MixinRenderLiving(Side.CLIENT, m -> DangerConfig.enableMixinRenderLiving,
        "vanilla.compatoptifineshaders.MixinRenderLiving"),
    common_vanilla_MixinEntityArrow(Side.COMMON, m -> DangerConfig.enableVanillaArrowReplacement,
        "vanilla.ArrowReplacement.MixinEntityArrow"),
    common_vanilla_MixinArmorProperties(Side.COMMON, m -> DangerConfig.enableArmorSystemReplacement,
        "vanilla.ArmorSystem.MixinArmorProperties"),

    common_vanilla_MixinEntity(Side.COMMON, m -> DangerConfig.enableEntityTweaking,
        "vanilla.EntityTweaks.MixinEntity"),
    common_vanilla_MixinEntityLivingBase(Side.COMMON, m -> DangerConfig.enableEntityTweaking,
        "vanilla.EntityTweaks.MixinEntityLivingBase"),
    common_vanilla_MixinEntityPlayer(Side.COMMON, m -> DangerConfig.enableEntityTweaking,
        "vanilla.EntityTweaks.MixinEntityPlayer"),
    common_vanilla_MixinSharedMonsterAttributes(Side.COMMON, m -> DangerConfig.enableEntityTweaking,
        "vanilla.EntityTweaks.MixinSharedMonsterAttributes"),

    common_vanilla_MixinEntityPlayer_bow(Side.COMMON, m -> DangerConfig.enableBowSystem,
            "vanilla.BowSystem.MixinEntityPlayer"),
    common_vanilla_MixinEntityPlayerSP(Side.COMMON, m -> DangerConfig.enableBowSystem,
            "vanilla.BowSystem.MixinEntityPlayerSP"),
    common_vanilla_MixinItemBow(Side.COMMON, m -> DangerConfig.enableBowSystem,
            "vanilla.BowSystem.MixinItemBow"),

    common_vanilla_MixinItem(Side.COMMON, m -> DangerConfig.enableItemSystem,
            "vanilla.ItemSystem.MixinItem"),
    common_vanilla_MixinItemStack(Side.COMMON, m -> DangerConfig.enableItemSystem,
            "vanilla.ItemSystem.MixinItemStack"),
    // TODO ENABLE WHEN FIXED AND USING NO REFLECTIONS
    /*common_vanilla_MixinEntitySpawnMessage(Side.COMMON, m -> DangerConfig.enableFixIncorrectMotionsSettings,
        "vanilla.FixIncorrectMotions.MixinEntitySpawnMessage"),
    common_vanilla_MixinEntityTrackerEntry(Side.COMMON, m -> DangerConfig.enableFixIncorrectMotionsSettings,
        "vanilla.FixIncorrectMotions.MixinEntityTrackerEntry"),
    common_vanilla_MixinS12PacketEntityVelocity(Side.COMMON, m -> DangerConfig.enableFixIncorrectMotionsSettings,
        "vanilla.FixIncorrectMotions.MixinS12PacketEntityVelocity"),*/
    ;

    @Getter
    public final Side side;
    @Getter
    public final Predicate<List<ITargetedMod>> filter;
    @Getter
    public final String mixin;

    static Predicate<List<ITargetedMod>> require(TargetedMod in) {
        return modList -> modList.contains(in);
    }

    static Predicate<List<ITargetedMod>> avoid(TargetedMod in) {
        return modList -> !modList.contains(in);
    }
}
