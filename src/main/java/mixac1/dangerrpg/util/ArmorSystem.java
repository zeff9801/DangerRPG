package mixac1.dangerrpg.util;
import java.util.ArrayList;
import java.util.Arrays;

import mixac1.dangerrpg.init.RPGConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.ISpecialArmor.ArmorProperties;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mixac1.dangerrpg.capability.ItemAttributes;
import mixac1.dangerrpg.capability.PlayerAttributes;
import mixac1.dangerrpg.init.RPGOther.RPGDamageSource;

public class ArmorSystem {
    public static final float MAX_PHISICAL_ARMOR = 40;

    public static float convertPhisicArmor(float armor) {
        return Utils.alignment(armor * 100 / MAX_PHISICAL_ARMOR, 0, 100);
    }

    private static float getArmorValue(ItemStack stack, DamageSource source) {
        if (stack != null) {
            if (source.isMagicDamage()) {
                return ItemAttributes.MAGIC_ARMOR.hasIt(stack) ? ItemAttributes.MAGIC_ARMOR.get(stack) : 0;
            } else if (!source.isUnblockable()) {
                return ItemAttributes.PHYSIC_ARMOR.hasIt(stack) ? ItemAttributes.PHYSIC_ARMOR.get(stack)
                    : stack.getItem() instanceof ItemArmor
                    ? convertPhisicArmor(((ItemArmor) stack.getItem()).damageReduceAmount)
                    : 0;
            }
        }
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public static float getTotalArmor(DamageSource source) {
        float value = 0;
        Minecraft mc = Minecraft.getMinecraft();
        float damage = RPGConfig.ClientConfig.d.guiDamageForTestArmor * MAX_PHISICAL_ARMOR;

        ArrayList<ArmorProperties> list = getArrayArmorProperties(
            mc.thePlayer,
            mc.thePlayer.inventory.armorInventory,
            source,
            damage);
        if (!list.isEmpty()) {
            ArmorProperties[] props = list.toArray(new ArmorProperties[0]);
            standardizeList(props, damage);

            for (ArmorProperties prop : props) {
                value += (float) prop.AbsorbRatio;
            }
        }

        value += getPassiveResist(mc.thePlayer, source);
        return Utils.alignment(value, -Float.MAX_VALUE, 1) * 100;
    }

    @SideOnly(Side.CLIENT)
    public static float getTotalPhisicArmor() {
        return getTotalArmor(RPGDamageSource.phisic);
    }

    @SideOnly(Side.CLIENT)
    public static float getTotalMagicArmor() {
        return getTotalArmor(RPGDamageSource.magic);
    }

    @SideOnly(Side.CLIENT)
    public static float getArmor(ItemStack stack, DamageSource source) {
        return (float) (getArmorProperties(Minecraft.getMinecraft().thePlayer, stack, 0, source, 5).AbsorbRatio * 100);
    }

    public static float getPassiveResist(EntityLivingBase entity, DamageSource source) {
        if (entity instanceof EntityPlayer) {
            if (source == DamageSource.lava) {
                return PlayerAttributes.LAVA_RESIST.getSafe(entity, 0f);
            } else if (source == DamageSource.inFire || source == DamageSource.onFire) {
                return PlayerAttributes.FIRE_RESIST.getSafe(entity, 0f);
            } else if (source.isMagicDamage()) {
                return PlayerAttributes.MAGIC_RESIST.getSafe(entity, 0f);
            } else if (!source.isUnblockable()) {
                return PlayerAttributes.PHISIC_RESIST.getSafe(entity, 0f);
            }
        }
        return 0;
    }

    private static ArmorProperties getArmorProperties(EntityLivingBase entity, ItemStack stack, int slot,
                                                      DamageSource source, double damage) {
        if (stack.getItem() instanceof ISpecialArmor) {
            return ((ISpecialArmor) stack.getItem()).getProperties(entity, stack, source, damage, slot)
                .copy();
        } else if (stack.getItem() instanceof ItemArmor) {
            return new ArmorProperties(
                0,
                getArmorValue(stack, source) / 100,
                stack.getItem().getMaxDamage() + 1 - stack.getItemDamage());
        }
        return null;
    }

    public static ArrayList<ArmorProperties> getArrayArmorProperties(EntityLivingBase entity, ItemStack[] inventory,
                                                                     DamageSource source, double damage) {
        ArrayList<ArmorProperties> dmgVals = new ArrayList<>();

        for (int x = 0; x < inventory.length; x++) {
            ItemStack stack = inventory[x];
            if (stack == null) {
                continue;
            }

            ArmorProperties prop = getArmorProperties(entity, inventory[x], x, source, damage);

            if (prop != null) {
                prop.Slot = x;
                dmgVals.add(prop);
            }
        }

        return dmgVals;
    }

    public static void standardizeList(ArmorProperties[] armor, double damage) {
        Arrays.sort(armor);

        int start = 0;
        double total = 0;
        int priority = armor[0].Priority;
        int pStart = 0;
        boolean pChange = false;
        boolean pFinished = false;

        for (int x = 0; x < armor.length; x++) {
            total += armor[x].AbsorbRatio;
            if (x == armor.length - 1 || armor[x].Priority != priority) {
                if (armor[x].Priority != priority) {
                    total -= armor[x].AbsorbRatio;
                    x--;
                    pChange = true;
                }
                if (total > 1) {
                    for (int y = start; y <= x; y++) {
                        double newRatio = armor[y].AbsorbRatio / total;
                        if (newRatio * damage > armor[y].AbsorbMax) {
                            armor[y].AbsorbRatio = armor[y].AbsorbMax / damage;
                            total = 0;
                            for (int z = pStart; z <= y; z++) {
                                total += armor[z].AbsorbRatio;
                            }
                            start = y + 1;
                            x = y;
                            break;
                        } else {
                            armor[y].AbsorbRatio = newRatio;
                            pFinished = true;
                        }
                    }
                    if (pChange && pFinished) {
                        damage -= (damage * total);
                        total = 0;
                        start = x + 1;
                        priority = armor[start].Priority;
                        pStart = start;
                        pChange = false;
                        pFinished = false;
                        if (damage <= 0) {
                            for (int y = x + 1; y < armor.length; y++) {
                                armor[y].AbsorbRatio = 0;
                            }
                            break;
                        }
                    }
                } else {
                    for (int y = start; y <= x; y++) {
                        total -= armor[y].AbsorbRatio;
                        if (damage * armor[y].AbsorbRatio > armor[y].AbsorbMax) {
                            armor[y].AbsorbRatio = armor[y].AbsorbMax / damage;
                        }
                        total += armor[y].AbsorbRatio;
                    }
                    damage -= (damage * total);
                    total = 0;
                    if (x != armor.length - 1) {
                        start = x + 1;
                        priority = armor[start].Priority;
                        pStart = start;
                        pChange = false;
                        if (damage <= 0) {
                            for (int y = x + 1; y < armor.length; y++) {
                                armor[y].AbsorbRatio = 0;
                            }
                            break;
                        }
                    }
                }
            }
        }
    }
}
