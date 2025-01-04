package mixac1.dangerrpg.mixins.common.vanilla.ArmorSystem;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.ArrayList;

import static mixac1.dangerrpg.util.ArmorSystem.*;

@Mixin(ISpecialArmor.ArmorProperties.class)
public class MixinArmorProperties {
    @Overwrite(remap = false)
    public static float ApplyArmor(EntityLivingBase entity, ItemStack[] inventory, DamageSource source, double damage) {
        ArrayList<ISpecialArmor.ArmorProperties> dmgVals = getArrayArmorProperties(entity, inventory, source, damage);
        damage *= MAX_PHISICAL_ARMOR;
        damage *= 1 - getPassiveResist(entity, source);

        if (dmgVals.size() > 0 && damage > 0) {
            ISpecialArmor.ArmorProperties[] props = dmgVals.toArray(new ISpecialArmor.ArmorProperties[dmgVals.size()]);
            standardizeList(props, damage);
            int level = props[0].Priority;
            double ratio = 0;
            for (ISpecialArmor.ArmorProperties prop : props) {
                if (level != prop.Priority) {
                    damage -= (damage * ratio);
                    ratio = 0;
                    level = prop.Priority;
                }
                ratio += prop.AbsorbRatio;

                double absorb = damage * prop.AbsorbRatio;
                if (absorb > 0) {
                    ItemStack stack = inventory[prop.Slot];
                    int itemDamage = (int) (absorb / MAX_PHISICAL_ARMOR < 1 ? 1 : absorb / MAX_PHISICAL_ARMOR);
                    if (stack.getItem() instanceof ISpecialArmor) {
                        ((ISpecialArmor) stack.getItem()).damageArmor(entity, stack, source, itemDamage, prop.Slot);
                    } else {
                        stack.damageItem(itemDamage, entity);
                    }
                    if (stack.stackSize <= 0) {
                        inventory[prop.Slot] = null;
                    }
                }
            }
            damage *= 1 - ratio;
        }
        return (float) (damage / MAX_PHISICAL_ARMOR);
    }
}
