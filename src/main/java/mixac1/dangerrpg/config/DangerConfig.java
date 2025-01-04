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
    private static final String MAIN_CATEGORY = "Main";

    public DangerConfig(String folderName) {
        super(folderName, Arrays.asList(MAIN_CATEGORY));
    }


    public void loadConfig() {
        setProperty(MAIN_CATEGORY, "enableMixinRenderLiving", enableMixinRenderLiving, "Fix issues between Danger Rpg Gui and Optifine shaders (No Wiki)");
        setProperty(MAIN_CATEGORY, "enableVanillaArrowReplacement", enableVanillaArrowReplacement,"Enable Vanilla Arrow/Throw/Projectile Replacement (No Wiki)");
        setProperty(MAIN_CATEGORY, "enableArmorSystemReplacement", enableArmorSystemReplacement,"Enable Armor System Replacement (No Wiki)");
        setProperty(MAIN_CATEGORY, "enableBowSystem", enableBowSystem,"Enable Bow System Replacement (No Wiki)");
        setProperty(MAIN_CATEGORY, "enableItemSystem", enableItemSystem,"Enable Item System Replacement (No Wiki)");
        setProperty(MAIN_CATEGORY, "enableEntityTweaking", enableEntityTweaking,"Enable Entity Tweaking (No Wiki)");
        super.loadConfig();
        enableMixinRenderLiving = getProperty(MAIN_CATEGORY, "enableMixinRenderLiving",true);
        enableVanillaArrowReplacement = getProperty(MAIN_CATEGORY, "enableVanillaArrowReplacement",true);
        enableArmorSystemReplacement = getProperty(MAIN_CATEGORY, "enableArmorSystemReplacement",true);
        enableBowSystem = getProperty(MAIN_CATEGORY, "enableBowSystem", true);
        enableItemSystem = getProperty(MAIN_CATEGORY, "enableItemSystem", true);
        enableEntityTweaking = getProperty(MAIN_CATEGORY, "enableEntityTweaking", true);
    }
}
