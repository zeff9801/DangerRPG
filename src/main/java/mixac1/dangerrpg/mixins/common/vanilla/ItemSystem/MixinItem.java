package mixac1.dangerrpg.mixins.common.vanilla.ItemSystem;

import mixac1.dangerrpg.capability.PlayerAttributes;
import mixac1.dangerrpg.capability.RPGItemHelper;
import mixac1.dangerrpg.init.RPGOther;
import mixac1.dangerrpg.item.IMaterialSpecial;
import mixac1.dangerrpg.util.RPGHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Item.class)
public class MixinItem {
    @Overwrite(remap = false)
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        if (entityLiving instanceof EntityPlayer && RPGItemHelper.isRPGable(stack)) {
            return PlayerAttributes.SPEED_COUNTER.getValue(entityLiving) > 0;
        }
        return false;
    }

    @Overwrite
    public EnumRarity getRarity(ItemStack stack) {
        EnumRarity returnValue = stack.isItemEnchanted() ? EnumRarity.rare : EnumRarity.common;

        if (RPGItemHelper.isRPGable(stack)
            && (returnValue == EnumRarity.common || stack.isItemEnchanted() && returnValue == EnumRarity.rare)) {
            IMaterialSpecial mat = RPGHelper.getMaterialSpecial(stack);
            if (mat != null) {
                return mat.getItemRarity();
            }
        }

        if (returnValue == EnumRarity.uncommon) {
            return RPGOther.RPGItemRarity.uncommon;
        } else if (returnValue == EnumRarity.rare) {
            return RPGOther.RPGItemRarity.rare;
        } else if (returnValue == EnumRarity.epic) {
            return RPGOther.RPGItemRarity.epic;
        }
        return returnValue;
    }

    // disabled due to making bugs + it seem that's unneeded , fix
    // https://github.com/quentin452/DangerRPG-Continuation/issues/57 + fix
    // https://github.com/quentin452/DangerRPG-Continuation/issues/59
    /*
     * @Hook(injectOnExit = true, returnCondition = ReturnCondition.ALWAYS)
     * public static Multimap getAttributeModifiers(Item item, ItemStack stack, @ReturnValue Multimap returnValue) {
     * if (RPGItemHelper.isRPGable(stack)) {
     * if (ItemAttributes.MELEE_DAMAGE.hasIt(stack)) {
     * returnValue.removeAll(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName());
     * returnValue.put(
     * SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(),
     * new AttributeModifier(
     * RPGUUIDs.DEFAULT_DAMAGE,
     * "Weapon modifier",
     * ItemAttributes.MELEE_DAMAGE.get(stack),
     * 0));
     * }
     * }
     * return returnValue;
     * }
     */

}
