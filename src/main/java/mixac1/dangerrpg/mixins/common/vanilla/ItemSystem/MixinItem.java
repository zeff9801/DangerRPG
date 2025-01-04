package mixac1.dangerrpg.mixins.common.vanilla.ItemSystem;

import mixac1.dangerrpg.capability.ItemAttributes;
import mixac1.dangerrpg.capability.PlayerAttributes;
import mixac1.dangerrpg.capability.RPGItemHelper;
import mixac1.dangerrpg.init.RPGOther.RPGItemRarity;
import mixac1.dangerrpg.item.IMaterialSpecial;
import mixac1.dangerrpg.util.RPGHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class MixinItem {

    @Inject(method = "getItemEnchantability", at = @At("HEAD"), cancellable = true)
    public void getItemEnchantability(CallbackInfoReturnable<Integer> cir) {
        Item item = (Item) (Object) this;
        ItemStack stack = new ItemStack(item);
        if (RPGItemHelper.isRPGable(stack) && ItemAttributes.ENCHANTABILITY.hasIt(stack)) {
            cir.setReturnValue((int) ItemAttributes.ENCHANTABILITY.get(stack));
        }
    }

    @Inject(method = "onEntitySwing", at = @At("HEAD"), cancellable = true, remap = false)
    public void onEntitySwing(EntityLivingBase entity, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof EntityPlayer && RPGItemHelper.isRPGable(stack)) {
            cir.setReturnValue(PlayerAttributes.SPEED_COUNTER.getValue(entity) > 0);
        }
    }

    @Inject(method = "getRarity", at = @At("HEAD"), cancellable = true)
    public void getRarity(ItemStack stack, CallbackInfoReturnable<EnumRarity> cir) {
        EnumRarity returnValue = stack.isItemEnchanted() ? EnumRarity.rare : EnumRarity.common;

        if (RPGItemHelper.isRPGable(stack) && (returnValue == EnumRarity.common || stack.isItemEnchanted() && returnValue == EnumRarity.rare)) {
            IMaterialSpecial mat = RPGHelper.getMaterialSpecial(stack);
            if (mat != null) {
                cir.setReturnValue(mat.getItemRarity());
                return;
            }
        }

        if (returnValue == EnumRarity.uncommon) {
            cir.setReturnValue(RPGItemRarity.uncommon);
        } else if (returnValue == EnumRarity.rare) {
            cir.setReturnValue(RPGItemRarity.rare);
        } else if (returnValue == EnumRarity.epic) {
            cir.setReturnValue(RPGItemRarity.epic);
        }
    }
}
