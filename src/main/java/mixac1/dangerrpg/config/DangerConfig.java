package mixac1.dangerrpg.config;

import com.falsepattern.lib.config.Config;

import mixac1.dangerrpg.DangerRPG;

@Config(modid = DangerRPG.MODID)
public class DangerConfig {

    @Config.Comment("Fix issues between Danger Rpg Gui and Optifine shaders (No Wiki)")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinRenderLiving;
    @Config.Comment("Enable Vanilla Arrow/Throw/Projectile Replacement (No Wiki)")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableVanillaArrowReplacement;
    @Config.Comment("Enable Armor System Replacement (No Wiki)")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableArmorSystemReplacement;
    @Config.Comment("Enable Bow System Replacement (No Wiki)")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableBowSystem;
    @Config.Comment("Enable Item System Replacement (No Wiki)")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableItemSystem;
    @Config.Comment("Enable Entity Tweaking (No Wiki)")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableEntityTweaking;
}
