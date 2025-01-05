package mixac1.dangerrpg.init;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import lombok.Getter;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.FMLInjectionData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mixac1.dangerrpg.DangerRPG;
import mixac1.dangerrpg.api.entity.EntityAttribute;
import mixac1.dangerrpg.api.entity.LvlEAProvider;
import mixac1.dangerrpg.api.item.GemType;
import mixac1.dangerrpg.api.item.ItemAttribute;
import mixac1.dangerrpg.capability.data.RPGEntityRegister.EntityAttrParams;
import mixac1.dangerrpg.capability.data.RPGEntityRegister.RPGEntityData;
import mixac1.dangerrpg.capability.data.RPGItemRegister.ItemAttrParams;
import mixac1.dangerrpg.capability.data.RPGItemRegister.RPGItemData;
import mixac1.dangerrpg.client.gui.GuiMode;
import mixac1.dangerrpg.util.IMultiplier.MulType;
import mixac1.dangerrpg.util.IMultiplier.Multiplier;
import mixac1.dangerrpg.util.RPGHelper;
import mixac1.dangerrpg.util.Tuple.Stub;
import mixac1.dangerrpg.util.Utils;

public class RPGConfig {

    public static File dir;

    static {
        dir = new File((File) FMLInjectionData.data()[6], "config/".concat(DangerRPG.MODID));
        if (dir.exists()) {
            if (!dir.isDirectory()) {
                dir.delete();
            }
        } else {
            dir.mkdir();
        }

    }

   // @SideOnly(Side.CLIENT)
   // public static ClientConfig clientConfig;
  //  public static MainConfig mainConfig = new MainConfig("MainConfig");
    public static ItemConfig itemConfig = new ItemConfig("ItemConfig");
    public static EntityConfig entityConfig = new EntityConfig("EntityConfig");

    public static void load(FMLPreInitializationEvent e) {
        //mainConfig.load();
        itemConfig.load();
        entityConfig.load();

        //if (DangerConfig.mainEnableTransferConfig) {
           // mainConfig.createTransferData();
            itemConfig.createTransferData();
            entityConfig.createTransferData();
        //}
    }

    @SideOnly(Side.CLIENT)
    public static void loadClient(FMLPreInitializationEvent e) {
       // clientConfig = new ClientConfig("ClientConfig");
        //clientConfig.load();
    }

    public static void postLoadPre(FMLPostInitializationEvent e) {
       // mainConfig.postLoadPre();
        itemConfig.postLoadPre();
        entityConfig.postLoadPre();
    }

    public static void postLoadPost(FMLPostInitializationEvent e) {
        //mainConfig.postLoadPost();
        itemConfig.postLoadPost();
        entityConfig.postLoadPost();
    }


    public static class ItemConfig extends RPGConfigCommon {


        public static HashSet<String> activeRPGItems = new HashSet<>();

        public ItemConfig(String fileName) {
            super(fileName);
        }

        @Override
        public void postLoadPost() {
            HashMap<Item, RPGItemData> map = (HashMap<Item, RPGItemData>) RPGCapability.rpgItemRegistr
                .getActiveElements();

            customConfig(map);

            ArrayList<String> names = RPGHelper.getItemNames(map.keySet(), true, false);
            getPropertyStrings(
                "activeRPGItems",
                names.toArray(new String[0]),
                "Set active RPG items (activated if 'isAllItemsRPGable' is false) (true/false)",
                true);

            names = RPGHelper.getItemNames(RPGCapability.rpgItemRegistr.keySet(), true, true);
            getPropertyStrings(
                "itemList",
                names.toArray(new String[0]),
                "List of all items, which can be RPGable",
                true);

            save();
        }

        @Override
        public void createTransferData() {

        }

        @Override
        public void extractTransferData(byte[] transferData) {

        }

        protected void customConfig(HashMap<Item, RPGItemData> map) {
            String str = "customSetting";

            Property prop = getPropertyStrings(
                "needCustomSetting",
                new String[] { Items.diamond_sword.delegate.name() },
                "Set items, which needs customization",
                true);
            HashSet<String> needCustomSetting = new HashSet<>(Arrays.asList(prop.getStringList()));

            if (!needCustomSetting.isEmpty()) {
                for (Entry<Item, RPGItemData> item : map.entrySet()) {
                    if (needCustomSetting.contains(item.getKey().delegate.name())) {
                        ConfigCategory cat = config
                            .getCategory(Utils.toString(str, ".", item.getKey().delegate.name()));
                        if (!item.getValue().isSupported) {
                            cat.setComment("Warning: it isn't support from mod");
                        }
                        for (Entry<ItemAttribute, ItemAttrParams> ia : item.getValue().attributes.entrySet()) {
                            if (ia.getKey()
                                .isConfigurable()) {
                                ia.getValue().value = getIAValue(cat.getQualifiedName(), ia);
                                if (ia.getValue().mul != null) {
                                    ia.getValue().mul = getIAMultiplier(cat.getQualifiedName(), ia);
                                }
                            }
                        }

                        for (Entry<GemType, Stub<Integer>> gt : item.getValue().gems.entrySet()) {
                            if (gt.getKey()
                                .isConfigurable()) {
                                gt.getValue().value1 = config.getInt(
                                    gt.getKey().name,
                                    cat.getQualifiedName(),
                                    gt.getValue().value1,
                                    0,
                                    Integer.MAX_VALUE,
                                    "");
                            }
                        }
                    }
                }
            }
        }

        protected float getIAValue(String category, Entry<ItemAttribute, ItemAttrParams> attr) {
            Property prop = config.get(category, attr.getKey().name, attr.getValue().value);
            prop.comment = " [default: " + attr.getValue().value + "]";
            float value = (float) prop.getDouble();
            if (attr.getKey()
                .isValid(value)) {
                return value;
            } else {
                prop.set(attr.getValue().value);
                return attr.getValue().value;
            }
        }

        protected Multiplier getIAMultiplier(String category, Entry<ItemAttribute, ItemAttrParams> attr) {
            String defStr = attr.getValue().mul.toString();
            Property prop = config.get(category, attr.getKey().name.concat(".mul"), defStr);
            prop.comment = " [default: " + defStr + "]";
            String str = prop.getString();

            if (!defStr.equals(str)) {
                Multiplier mul = MulType.getMul(str);
                if (mul != null) {
                    return mul;
                }
            }

            prop.set(defStr);
            return attr.getValue().mul;
        }
    }

    public static class EntityConfig extends RPGConfigCommon {

        public static class Data implements Serializable {

            public boolean isAllEntitiesRPGable = true;

            public int playerLoseLvlCount = 3;
            public int playerStartManaValue = 10;
            public int playerStartManaRegenValue = 1;
            public boolean playerCanLvlDownAttr = true;
            public float playerPercentLoseExpPoints = 0.5f;

            public int entityLvlUpFrequency = 50;
            public float entityLvlUpHealthMul = 0.1f;
            public float entityLvlUpDamageMul = 0.1f;
        }

        public static Data d = new Data();

        public static HashSet<String> activeRPGEntities = new HashSet<>();

        public EntityConfig(String fileName) {
            super(fileName);
        }

        @Override
        protected void init() {
            category.setComment(
                "FAQ:\n" + "Q: How do activate RPG entity?\n"
                    + "A: Take name of entity frome the 'entityList' and put it to the 'activeRPGEntities' list.\n"
                    + "Or you can enable flag 'isAllEntitiesRPGable' for active all entities.\n"
                    + "\n"
                    + "Q: How do congigure any entity?\n"
                    + "A: Take name of entity frome the 'entityList' and put it to the 'needCustomSetting' list.\n"
                    + "After this, run the game, exit from game and reopen this config.\n"
                    + "You be able find generated element for configure that entity.");

            save();
        }

        @Override
        public void load() {
            d.isAllEntitiesRPGable = config.getBoolean(
                "isAllEntitiesRPGable",
                category.getName(),
                d.isAllEntitiesRPGable,
                "All entities are RPGable (true/false)");

            d.playerLoseLvlCount = config.getInt(
                "playerLoseLvlCount",
                category.getName(),
                d.playerLoseLvlCount,
                0,
                Integer.MAX_VALUE,
                "Set number of lost points of level when player die");

            d.playerStartManaValue = config.getInt(
                "playerStartManaValue",
                category.getName(),
                d.playerStartManaValue,
                0,
                Integer.MAX_VALUE,
                "Set start mana value");

            d.playerStartManaRegenValue = config.getInt(
                "playerStartManaRegenValue",
                category.getName(),
                d.playerStartManaRegenValue,
                0,
                Integer.MAX_VALUE,
                "Set start mana regeneration value");

            d.playerCanLvlDownAttr = config.getBoolean(
                "playerCanLvlDownAttr",
                category.getName(),
                d.playerCanLvlDownAttr,
                "Can player decrease own stats without creative mode? (true/false)");

            d.playerPercentLoseExpPoints = config.getFloat(
                "playerPercentLoseExpPoints",
                category.getName(),
                d.playerPercentLoseExpPoints,
                0f,
                1f,
                "Set percent of lose experience points when level down player's stat");

            d.entityLvlUpFrequency = config.getInt(
                "entityLvlUpFrequency",
                category.getName(),
                d.entityLvlUpFrequency,
                1,
                Integer.MAX_VALUE,
                "Set frequency of RPG entity level up");

            d.entityLvlUpHealthMul = config.getFloat(
                "entityLvlUpHealthMul",
                category.getName(),
                d.entityLvlUpHealthMul,
                1,
                Float.MAX_VALUE,
                "Set multiplier of health per level");

            d.entityLvlUpDamageMul = config.getFloat(
                "entityLvlUpDamageMul",
                category.getName(),
                d.entityLvlUpDamageMul,
                1,
                Float.MAX_VALUE,
                "Set multiplier of damage per level");

            save();
        }

        @Override
        public void postLoadPre() {
            ArrayList<String> names = RPGHelper.getEntityNames(RPGCapability.rpgEntityRegistr.keySet(), true);
            Property prop = getPropertyStrings(
                "activeRPGEntities",
                names.toArray(new String[0]),
                "Set active RPG entities (activated if 'isAllEntitiesRPGable' is false) (true/false)",
                false);
            if (!d.isAllEntitiesRPGable) {
                activeRPGEntities = new HashSet<>(Arrays.asList(prop.getStringList()));
            }
            save();
        }

        @Override
        public void postLoadPost() {
            playerConfig();

            HashMap<Class<? extends EntityLivingBase>, RPGEntityData> map = (HashMap<Class<? extends EntityLivingBase>, RPGEntityData>) RPGCapability.rpgEntityRegistr
                .getActiveElements();

            customConfig(map);

            ArrayList<String> names = RPGHelper.getEntityNames(map.keySet(), true);
            getPropertyStrings(
                "activeRPGEntities",
                names.toArray(new String[0]),
                "Set active RPG entities (activated if 'isAllEntitiesRPGable' is false) (true/false)",
                true);

            names = RPGHelper.getEntityNames(RPGCapability.rpgEntityRegistr.keySet(), true);
            getPropertyStrings(
                "entityList",
                names.toArray(new String[0]),
                "List of all entities, which can be RPGable",
                true);

            save();
        }

        public void playerConfig() {
            String str = "customPlayerSetting";

            for (LvlEAProvider lvlProv : RPGCapability.rpgEntityRegistr.get(EntityPlayer.class).lvlProviders) {
                if (lvlProv.attr.isConfigurable()) {
                    String catStr = Utils.toString(str, ".", lvlProv.attr.name);
                    lvlProv.maxLvl = config.getInt("maxLvl", catStr, lvlProv.maxLvl, 0, Integer.MAX_VALUE, "");
                    lvlProv.startExpCost = config
                        .getInt("startExpCost", catStr, lvlProv.startExpCost, 0, Integer.MAX_VALUE, "");
                    if (lvlProv.mulValue instanceof Multiplier) {
                        lvlProv.mulValue = getEAMultiplier(
                            catStr,
                            "value",
                            lvlProv.attr,
                            (Multiplier) lvlProv.mulValue);
                    }
                    lvlProv.mulExpCost = getEAMultiplier(catStr, "expCost", lvlProv.attr, lvlProv.mulExpCost);
                }
            }
        }

        protected void customConfig(HashMap<Class<? extends EntityLivingBase>, RPGEntityData> map) {
            String str = "customSetting";

            Property prop = getPropertyStrings(
                "needCustomSetting",
                new String[] { (String) EntityList.classToStringMapping.get(EntityZombie.class) },
                "Set entities, which needs customization",
                true);
            HashSet<String> needCustomSetting = new HashSet<>(Arrays.asList(prop.getStringList()));

            if (!needCustomSetting.isEmpty()) {
                String entityName;
                for (Entry<Class<? extends EntityLivingBase>, RPGEntityData> entity : map.entrySet()) {
                    if (!EntityPlayer.class.isAssignableFrom(entity.getKey()) && needCustomSetting
                        .contains(entityName = (String) EntityList.classToStringMapping.get(entity.getKey()))) {
                        ConfigCategory cat = config.getCategory(Utils.toString(str, ".", entityName));
                        if (!entity.getValue().isSupported) {
                            cat.setComment("Warning: it isn't support from mod");
                        }
                        for (Entry<EntityAttribute, EntityAttrParams> ea : entity.getValue().attributes.entrySet()) {
                            if (ea.getKey()
                                .isConfigurable()) {
                                ea.getValue().mulValue = getEAMultiplier(
                                    cat.getQualifiedName(),
                                    ea.getKey().name,
                                    ea.getKey(),
                                    ea.getValue().mulValue);
                            }
                        }
                    }
                }
            }
        }

        protected Multiplier getEAMultiplier(String category, String name, EntityAttribute attr, Multiplier mul) {
            String defStr = mul.toString();
            Property prop = config.get(category, name.concat(".mul"), defStr);
            prop.comment = " [default: " + defStr + "]";
            String str = prop.getString();

            if (!defStr.equals(str)) {
                Multiplier mul1 = MulType.getMul(str);
                if (mul1 != null) {
                    return mul1;
                }
            }

            prop.set(defStr);
            return mul;
        }

        @Override
        public void createTransferData() {
            transferData = Utils.serialize(d);
        }

        @Override
        public void extractTransferData(byte[] transferData) {
            d = Utils.deserialize(transferData);
        }
    }

    public abstract static class RPGConfigCommon {

        protected Configuration config;
        protected ConfigCategory category;

        @Getter
        protected byte[] transferData;

        protected RPGConfigCommon(String fileName) {
            config = new Configuration(new File(dir, fileName.concat(".cfg")), DangerRPG.VERSION, true);

            category = config.getCategory(fileName);

            init();
        }

        protected void init() {}

        protected void load() {}

        protected void postLoadPre() {}

        protected void postLoadPost() {}

        public void save() {
            if (config.hasChanged()) {
                config.save();
            }
        }

        public abstract void createTransferData();

        public abstract void extractTransferData(byte[] transferData);

        protected Property getPropertyStrings(String categoryName, String[] defValue, String comment,
            boolean needClear) {
            ConfigCategory cat = config.getCategory(categoryName);
            if (needClear) {
                cat.clear();
            }

            Property prop = config.get(cat.getQualifiedName(), "list", defValue);
            prop.comment = comment != null ? comment : "";
            return prop;
        }
    }
}
