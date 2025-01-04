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
        pM.setProperty(MAIN_CATEGORY, "enableMixinRenderLiving", enableMixinRenderLiving, "Fix issues between Danger Rpg Gui and Optifine shaders (No Wiki)");
        pM.setProperty(MAIN_CATEGORY, "enableVanillaArrowReplacement", enableVanillaArrowReplacement,"Enable Vanilla Arrow/Throw/Projectile Replacement (No Wiki)");
        pM.setProperty(MAIN_CATEGORY, "enableArmorSystemReplacement", enableArmorSystemReplacement,"Enable Armor System Replacement (No Wiki)");
        pM.setProperty(MAIN_CATEGORY, "enableBowSystem", enableBowSystem,"Enable Bow System Replacement (No Wiki)");
        pM.setProperty(MAIN_CATEGORY, "enableItemSystem", enableItemSystem,"Enable Item System Replacement (No Wiki)");
        pM.setProperty(MAIN_CATEGORY, "enableEntityTweaking", enableEntityTweaking,"Enable Entity Tweaking (No Wiki)");
        super.loadConfig();
        enableMixinRenderLiving = pM.getProperty(MAIN_CATEGORY, "enableMixinRenderLiving",true);
        enableVanillaArrowReplacement = pM.getProperty(MAIN_CATEGORY, "enableVanillaArrowReplacement",true);
        enableArmorSystemReplacement = pM.getProperty(MAIN_CATEGORY, "enableArmorSystemReplacement",true);
        enableBowSystem = pM.getProperty(MAIN_CATEGORY, "enableBowSystem", true);
        enableItemSystem = pM.getProperty(MAIN_CATEGORY, "enableItemSystem", true);
        enableEntityTweaking = pM.getProperty(MAIN_CATEGORY, "enableEntityTweaking", true);
    }
}
