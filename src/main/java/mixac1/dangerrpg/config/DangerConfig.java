package mixac1.dangerrpg.config;

import fr.iamacat.api.config.CatConfig;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mixac1.dangerrpg.client.gui.GuiMode;

import java.util.Arrays;

public class DangerConfig extends CatConfig {
// TODO FIX STRING PROPERTIES ARE SET TO null in the config
    // TODO FIX IF A CONFIG NAME has spaces , he don't add "" to put it has a string
    // TODO FIX NON STATIC FIEDS VALUES ARE NOT CORRECTLY SET IN THE CONFIG
    public static boolean enableMixinRenderLiving = true, enableVanillaArrowReplacement = true, enableArmorSystemReplacement = true,
        enableBowSystem = true, enableItemSystem = true, enableEntityTweaking = true
    ,mainEnableInfoLog = true,mainEnableTransferConfig = true,mainEnableGemEventsToChat = true;

    public static boolean guiEnableHUD = true,guiEnemyHUDIsInvert = true,guiChargeIsCentered = true,showDamageParticles = true;
    public static boolean guiEnableDefaultFoodBar= false,guiPlayerHUDIsInvert= false,guiTwiceHealthManaBar= false, showAlways= false
        ,neiShowShapedRecipe= false;
    public static int guiPlayerHUDOffsetX = 10,guiPlayerHUDOffsetY = 10,guiEnemyHUDOffsetX = 10,guiEnemyHUDOffsetY = 10;
    public static int guiChargeOffsetX = 0,guiChargeOffsetY = 45,guiDafaultHUDMode = 1,guiDamageForTestArmor = 25;
    private static final String[] acceptedColors = new String[] { "RED", "GREEN", "BLUE", "YELLOW", "ORANGE",
        "WHITE", "BLACK", "PURPLE" };
    public static String damageColor = "RED",healColor = "GREEN";
    public static double size2 = 3.0;

    private static final String MIXIN_CATEGORY = "Mixin",MAIN_CATEGORY = "Main",CLIENT_CATEGORY = "Client";

    public DangerConfig(String folderName) {
        super(folderName, Arrays.asList(MIXIN_CATEGORY, MAIN_CATEGORY,CLIENT_CATEGORY));
        CONFIG_VERSION = "2.1";
        setConfigCommentHeader(MIXIN_CATEGORY, "MIXIN INFO:\n\nThis config file contains configuration for mixins.\n");
        setConfigCommentHeader(MAIN_CATEGORY, "GENERAL INFO:\n" + "\n"
            + "How do config multipliers ('.mul')\n"
            + "You can use three types of multiplier:\n"
            + "ADD  'value'    - 'input parameter' + 'value'\n"
            + "MUL  'value'    - 'input parameter' * 'value'\n"
            + "SQRT 'value'    - 'input parameter' + sqrt('input parameter' * 'value')\n"
            + "HARD - not for using. There is a hard expression, but you can change it using other multipliers\n"
            + "\n");
        setConfigCommentHeader(CLIENT_CATEGORY, "CLIENT INFO:\n");
    }

    @Override
    protected void registerProperties() {
        pM.registerProperty(MIXIN_CATEGORY, "enableMixinRenderLiving", enableMixinRenderLiving, "Fix issues between Danger Rpg Gui and Optifine shaders (No Wiki)");
        pM.registerProperty(MIXIN_CATEGORY, "enableVanillaArrowReplacement", enableVanillaArrowReplacement, "Enable Vanilla Arrow/Throw/Projectile Replacement (No Wiki)");
        pM.registerProperty(MIXIN_CATEGORY, "enableArmorSystemReplacement", enableArmorSystemReplacement, "Enable Armor System Replacement (No Wiki)");
        pM.registerProperty(MIXIN_CATEGORY, "enableBowSystem", enableBowSystem, "Enable Bow System Replacement (No Wiki)");
        pM.registerProperty(MIXIN_CATEGORY, "enableItemSystem", enableItemSystem, "Enable Item System Replacement (No Wiki)");
        pM.registerProperty(MIXIN_CATEGORY, "enableEntityTweaking", enableEntityTweaking, "Enable Entity Tweaking (No Wiki)");

        pM.registerProperty(MAIN_CATEGORY, "mainEnableInfoLog", mainEnableInfoLog, "Enable printing info message to log (true/false)");
        pM.registerProperty(MAIN_CATEGORY, "mainEnableTransferConfig", mainEnableTransferConfig, "Enable transfer config data from server to client (true/false)\nCan be errors. Synchronize the configuration better by other means.");
        pM.registerProperty(MAIN_CATEGORY, "mainEnableGemEventsToChat", mainEnableGemEventsToChat, "Enable printing gem's events to chat");

        pM.registerProperty(CLIENT_CATEGORY, "guiIsEnableHUD", guiEnableHUD, "Enable RPG HUD (true/false)");
        pM.registerProperty(CLIENT_CATEGORY, "guiEnableDefaultFoodBar", guiEnableDefaultFoodBar, "Enable default food bar (true/false)");
        pM.registerProperty(CLIENT_CATEGORY, "guiPlayerHUDOffsetX", guiPlayerHUDOffsetX, "Change X offset of player's HUD");
        pM.registerProperty(CLIENT_CATEGORY, "guiPlayerHUDOffsetY", guiPlayerHUDOffsetY, "Change Y offset of player's HUD");
        pM.registerProperty(CLIENT_CATEGORY, "guiPlayerHUDIsInvert", guiPlayerHUDIsInvert, "Change side of player's HUD (true/false)");
        pM.registerProperty(CLIENT_CATEGORY, "guiEnemyHUDOffsetX", guiEnemyHUDOffsetX, "Change X offset of enemy's HUD");
        pM.registerProperty(CLIENT_CATEGORY, "guiEnemyHUDOffsetY", guiEnemyHUDOffsetY, "Change Y offset of enemy's HUD");
        pM.registerProperty(CLIENT_CATEGORY, "guiEnemyHUDIsInvert", guiEnemyHUDIsInvert, "Change side of enemy's HUD (true/false)");
        pM.registerProperty(CLIENT_CATEGORY, "guiChargeOffsetX", guiChargeOffsetX, "Change X offset of charge bar");
        pM.registerProperty(CLIENT_CATEGORY, "guiChargeOffsetY", guiChargeOffsetY, "Change Y offset of charge bar");
        pM.registerProperty(CLIENT_CATEGORY, "guiChargeIsCentered", guiChargeIsCentered, "Charge bar need centering (true/false)");
        pM.registerProperty(CLIENT_CATEGORY, "guiTwiceHealthManaBar", guiTwiceHealthManaBar, "Twice health-mana bar (true/false)");
        pM.registerProperty(CLIENT_CATEGORY, "guiDamageForTestArmor", guiDamageForTestArmor, "Default damage value for calculate resistance in armor bar.");
        pM.registerProperty(CLIENT_CATEGORY, "guiDafaultHUDMode", guiDafaultHUDMode, "Set default HUD mode:\n[0] - normal\n[1] - normal digital\n[2] - simple\n[3] - simple digital\n");
        pM.registerProperty(CLIENT_CATEGORY, "neiShowShapedRecipe", neiShowShapedRecipe, "Is show default recipes in RPG workbench (need NEI) (true/false)");
        pM.registerProperty(CLIENT_CATEGORY, "Show Damage Particles", showDamageParticles, "Show Damage Indicators");
        pM.registerProperty(CLIENT_CATEGORY, "Show Always Particles", showAlways, "Show Always The Damage Particles");
        pM.registerProperty(CLIENT_CATEGORY, "size2", size2, "Particles Size");
        pM.registerProperty(CLIENT_CATEGORY, "Heal Color", healColor, "Heal Text Color (accepted value : RED\", \"GREEN\", \"BLUE\", \"YELLOW\", \"ORANGE\")");
        pM.registerProperty(CLIENT_CATEGORY, "Damage Color", damageColor, "Damage Text Color (accepted value : RED\", \"GREEN\", \"BLUE\", \"YELLOW\", \"ORANGE\")");
    }

    @Override
    public void loadConfig() {
        super.loadConfig();
        validateAndFixColors();
        GuiMode.set(guiDafaultHUDMode);
    }

    private void validateAndFixColors() {
        if (!isColorAccepted(damageColor)) {
            damageColor = "RED";
        }
        if (!isColorAccepted(healColor)) {
            healColor = "GREEN";
        }
    }

    private boolean isColorAccepted(String color) {
        for (String acceptedColor : acceptedColors) {
            if (acceptedColor.equalsIgnoreCase(color)) {
                return true;
            }
        }
        return false;
    }
}
