package mixac1.dangerrpg.item;

import net.minecraft.item.Item;

import mixac1.dangerrpg.DangerRPG;
import mixac1.dangerrpg.init.RPGItems;
import mixac1.dangerrpg.init.RPGOther.RPGCreativeTabs;
import mixac1.dangerrpg.util.Utils;

public class ItemE extends Item {

    public ItemE(String name) {
        setUnlocalizedName(name);
        setTextureName(Utils.toString(DangerRPG.MODID, ":", name));
        setCreativeTab(RPGCreativeTabs.tabRPGItems);

        RPGItems.registerItem(this);
    }
}
