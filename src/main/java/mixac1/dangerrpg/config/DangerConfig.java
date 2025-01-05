package mixac1.dangerrpg.config;

import fr.iamacat.api.config.CatConfig;
import java.util.Arrays;

public class DangerConfig extends CatConfig {

    public static boolean enableMixinRenderLiving = true;
    public static boolean enableVanillaArrowReplacement = true;
    public static boolean enableArmorSystemReplacement = true;
    public static boolean enableBowSystem = true;
    public static boolean enableItemSystem = true;
    public static boolean enableEntityTweaking = true;
    private static final String MIXIN_CATEGORY = "Mixin";

    public DangerConfig(String folderName) {
        super(folderName, Arrays.asList(MIXIN_CATEGORY));
        CONFIG_VERSION = "2.1";
        this.config_comment_header =   "GENERAL INFO:\n" + "\n"
            + "This config file contain config for mixins configurations\n"
            + "\n";
    }

    @Override
    protected void registerProperties() {
        pM.registerProperty(MIXIN_CATEGORY, "enableMixinRenderLiving", enableMixinRenderLiving, "Fix issues between Danger Rpg Gui and Optifine shaders (No Wiki)");
        pM.registerProperty(MIXIN_CATEGORY, "enableVanillaArrowReplacement", enableVanillaArrowReplacement, "Enable Vanilla Arrow/Throw/Projectile Replacement (No Wiki)");
        pM.registerProperty(MIXIN_CATEGORY, "enableArmorSystemReplacement", enableArmorSystemReplacement, "Enable Armor System Replacement (No Wiki)");
        pM.registerProperty(MIXIN_CATEGORY, "enableBowSystem", enableBowSystem, "Enable Bow System Replacement (No Wiki)");
        pM.registerProperty(MIXIN_CATEGORY, "enableItemSystem", enableItemSystem, "Enable Item System Replacement (No Wiki)");
        pM.registerProperty(MIXIN_CATEGORY, "enableEntityTweaking", enableEntityTweaking, "Enable Entity Tweaking (No Wiki)");
    }
}
