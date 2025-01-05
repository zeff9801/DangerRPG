package mixac1.dangerrpg.config;

import fr.iamacat.api.config.CatConfig;
import mixac1.dangerrpg.client.gui.GuiMode;
import mixac1.dangerrpg.init.RPGCapability;
import mixac1.dangerrpg.util.RPGHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DangerConfig extends CatConfig {
    // TODO FIX NON STATIC FIEDS VALUES ARE NOT CORRECTLY SET IN THE CONFIG
    public static List<String> activeRPGItems = new ArrayList<>();;

    public static boolean enableMixinRenderLiving = true, enableVanillaArrowReplacement = true, enableArmorSystemReplacement = true,
        enableBowSystem = true, enableItemSystem = true, enableEntityTweaking = true
    ,mainEnableInfoLog = true,mainEnableTransferConfig = true,mainEnableGemEventsToChat = true,isAllItemsRPGable = true
    ,canUpInTable = true;

    public static boolean guiEnableHUD = true,guiEnemyHUDIsInvert = true,guiChargeIsCentered = true,showDamageParticles = true;
    public static boolean guiEnableDefaultFoodBar= false,guiPlayerHUDIsInvert= false,guiTwiceHealthManaBar= false, showAlways= false
        ,neiShowShapedRecipe= false;
    public static int guiPlayerHUDOffsetX = 10,guiPlayerHUDOffsetY = 10,guiEnemyHUDOffsetX = 10,guiEnemyHUDOffsetY = 10,
        maxLevel = 15,startMaxExp = 100,gemStartLvl = 5,gemLvlUpStep = 5,guiChargeOffsetX = 0,guiChargeOffsetY = 45
        ,guiDafaultHUDMode = 1,guiDamageForTestArmor = 25;
    private static final String[] acceptedColors = new String[] { "RED", "GREEN", "BLUE", "YELLOW", "ORANGE",
        "WHITE", "BLACK", "PURPLE" };
    public static String damageColor = "RED",healColor = "GREEN";
    public static double size2 = 3.0;
    public static float expMul = 1.20f;

    private static final String MIXIN_CATEGORY = "Mixin",MAIN_CATEGORY = "Main",CLIENT_CATEGORY = "Client"
        ,ITEM_CATEGORY = "Item";


    public DangerConfig(String folderName) {
        super(folderName, Arrays.asList(MIXIN_CATEGORY, MAIN_CATEGORY,CLIENT_CATEGORY,ITEM_CATEGORY));
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
        setConfigCommentHeader(ITEM_CATEGORY,  "FAQ:\n" + "Q: How do activate RPG item?\n"
            + "A: Take name of item frome the 'itemList' and put it to the 'activeRPGItems' list.\n"
            + "Or you can enable flag 'isAllItemsRPGable' for active all items.\n"
            + "\n"
            + "Q: How do congigure any item?\n"
            + "A: Take name of item frome the 'itemList' and put it to the 'needCustomSetting' list.\n"
            + "After this, run the game, exit from game and reopen this config.\n"
            + "You be able find generated element for configure that item.");
    }

    @Override
    protected void registerProperties() {
        pM.registerProperty(MIXIN_CATEGORY, "enableMixinRenderLiving", enableMixinRenderLiving, "Fix issues between Danger Rpg Gui and Optifine shaders (No Wiki)",false);
        pM.registerProperty(MIXIN_CATEGORY, "enableVanillaArrowReplacement", enableVanillaArrowReplacement, "Enable Vanilla Arrow/Throw/Projectile Replacement (No Wiki)",false);
        pM.registerProperty(MIXIN_CATEGORY, "enableArmorSystemReplacement", enableArmorSystemReplacement, "Enable Armor System Replacement (No Wiki)",false);
        pM.registerProperty(MIXIN_CATEGORY, "enableBowSystem", enableBowSystem, "Enable Bow System Replacement (No Wiki)",false);
        pM.registerProperty(MIXIN_CATEGORY, "enableItemSystem", enableItemSystem, "Enable Item System Replacement (No Wiki)",false);
        pM.registerProperty(MIXIN_CATEGORY, "enableEntityTweaking", enableEntityTweaking, "Enable Entity Tweaking (No Wiki)",false);

        pM.registerProperty(MAIN_CATEGORY, "mainEnableInfoLog", mainEnableInfoLog, "Enable printing info message to log (true/false)",false);
        pM.registerProperty(MAIN_CATEGORY, "mainEnableTransferConfig", mainEnableTransferConfig, "Enable transfer config data from server to client (true/false)\nCan be errors. Synchronize the configuration better by other means.",false);
        pM.registerProperty(MAIN_CATEGORY, "mainEnableGemEventsToChat", mainEnableGemEventsToChat, "Enable printing gem's events to chat",false);

        pM.registerProperty(CLIENT_CATEGORY, "guiIsEnableHUD", guiEnableHUD, "Enable RPG HUD (true/false)",false);
        pM.registerProperty(CLIENT_CATEGORY, "guiEnableDefaultFoodBar", guiEnableDefaultFoodBar, "Enable default food bar (true/false)",false);
        pM.registerProperty(CLIENT_CATEGORY, "guiPlayerHUDOffsetX", guiPlayerHUDOffsetX, "Change X offset of player's HUD",false);
        pM.registerProperty(CLIENT_CATEGORY, "guiPlayerHUDOffsetY", guiPlayerHUDOffsetY, "Change Y offset of player's HUD",false);
        pM.registerProperty(CLIENT_CATEGORY, "guiPlayerHUDIsInvert", guiPlayerHUDIsInvert, "Change side of player's HUD (true/false)",false);
        pM.registerProperty(CLIENT_CATEGORY, "guiEnemyHUDOffsetX", guiEnemyHUDOffsetX, "Change X offset of enemy's HUD",false);
        pM.registerProperty(CLIENT_CATEGORY, "guiEnemyHUDOffsetY", guiEnemyHUDOffsetY, "Change Y offset of enemy's HUD",false);
        pM.registerProperty(CLIENT_CATEGORY, "guiEnemyHUDIsInvert", guiEnemyHUDIsInvert, "Change side of enemy's HUD (true/false)",false);
        pM.registerProperty(CLIENT_CATEGORY, "guiChargeOffsetX", guiChargeOffsetX, "Change X offset of charge bar",false);
        pM.registerProperty(CLIENT_CATEGORY, "guiChargeOffsetY", guiChargeOffsetY, "Change Y offset of charge bar",false);
        pM.registerProperty(CLIENT_CATEGORY, "guiChargeIsCentered", guiChargeIsCentered, "Charge bar need centering (true/false)",false);
        pM.registerProperty(CLIENT_CATEGORY, "guiTwiceHealthManaBar", guiTwiceHealthManaBar, "Twice health-mana bar (true/false)",false);
        pM.registerProperty(CLIENT_CATEGORY, "guiDamageForTestArmor", guiDamageForTestArmor, "Default damage value for calculate resistance in armor bar.",false);
        pM.registerProperty(CLIENT_CATEGORY, "guiDafaultHUDMode", guiDafaultHUDMode, "Set default HUD mode:\n[0] - normal\n[1] - normal digital\n[2] - simple\n[3] - simple digital\n",false);
        pM.registerProperty(CLIENT_CATEGORY, "neiShowShapedRecipe", neiShowShapedRecipe, "Is show default recipes in RPG workbench (need NEI) (true/false)",false);
        pM.registerProperty(CLIENT_CATEGORY, "Show Damage Particles", showDamageParticles, "Show Damage Indicators",false);
        pM.registerProperty(CLIENT_CATEGORY, "Show Always Particles", showAlways, "Show Always The Damage Particles",false);
        pM.registerProperty(CLIENT_CATEGORY, "size2", size2, "Particles Size",false);
        pM.registerProperty(CLIENT_CATEGORY, "Heal Color", healColor, "Heal Text Color (accepted value : RED\", \"GREEN\", \"BLUE\", \"YELLOW\", \"ORANGE\")",false);
        pM.registerProperty(CLIENT_CATEGORY, "Damage Color", damageColor, "Damage Text Color (accepted value : RED\", \"GREEN\", \"BLUE\", \"YELLOW\", \"ORANGE\")",false);
        pM.registerProperty(ITEM_CATEGORY, "isAllItemsRPGable", isAllItemsRPGable, "All weapons, tools , armors are RPGable (dangerous) (true/false)",false);
        pM.registerProperty(ITEM_CATEGORY, "canUpInTable", canUpInTable, "Items can be upgrade in LevelUp Table without creative mode (true/false) \nLevelUp Table is invisible now",false);
        pM.registerProperty(ITEM_CATEGORY, "maxLevel", maxLevel, "Set max level of RPG items",false);
        pM.registerProperty(ITEM_CATEGORY, "startMaxExp", startMaxExp, "Set start needed expirience for RPG items",false);
        pM.registerProperty(ITEM_CATEGORY, "expMul", expMul, "Set expirience multiplier for RPG items",false);
        pM.registerProperty(ITEM_CATEGORY, "gemStartLvl", gemStartLvl, "Set default start gem's level",false);
        pM.registerProperty(ITEM_CATEGORY, "gemLvlUpStep", gemLvlUpStep, "Set default level up gem's step",false);
        pM.registerProperty(ITEM_CATEGORY, "activeRPGItems", activeRPGItems, "Set active RPG items (activated if 'isAllItemsRPGable' is false) (true/false)",true);
    }

    @Override
    public void loadConfig() {
        super.loadConfig();
        validateAndFixColors();
        GuiMode.set(guiDafaultHUDMode);
    }

    @Override
    public void postLoadConfig() {
        ArrayList<String> names = RPGHelper.getItemNames(RPGCapability.rpgItemRegistr.keySet(), true, false);
        if (!isAllItemsRPGable) {
            activeRPGItems = new ArrayList<>(names);
            saveConfigForKey(ITEM_CATEGORY, "activeRPGItems", activeRPGItems);
        }
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
