package mixac1.dangerrpg.mixins.common.vanilla.BowSystem;

import mixac1.dangerrpg.api.item.IRPGItem;
import mixac1.dangerrpg.capability.RPGItemHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemBow.class)
public class MixinItemBow {
    @Overwrite
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int par) {
        ItemBow bow = ((ItemBow) (Object) this);
        int useDuration = bow.getMaxItemUseDuration(stack) - par;
        ArrowLooseEvent event = new ArrowLooseEvent(player, stack, useDuration);
        MinecraftForge.EVENT_BUS.post(new ArrowLooseEvent(player, stack, useDuration));
        if (event.isCanceled()) {
            return;
        }
        useDuration = event.charge;

        if (RPGItemHelper.isRPGable(stack)) {
            if (bow instanceof IRPGItem.IRPGItemBow) {
                ((IRPGItem.IRPGItemBow) bow).onStoppedUsing(stack, world, player, useDuration);
            } else {
                IRPGItem.DEFAULT_BOW.onStoppedUsing(stack, world, player, useDuration);
            }
        } else {
            dangerRPG_Continuation$onPlayerStoppedUsingDefault(bow, stack, world, player, useDuration);
        }
    }

    @Unique
    public void dangerRPG_Continuation$onPlayerStoppedUsingDefault(ItemBow bow, ItemStack stack, World world, EntityPlayer player,
                                                                           float useDuration) {
        boolean flag = player.capabilities.isCreativeMode
            || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack) > 0;

        if (flag || player.inventory.hasItem(Items.arrow)) {
            float f = useDuration / 20.0F;
            f = (f * f + f * 2.0F) / 3.0F;

            if (f < 0.1D) {
                return;
            }

            if (f > 1.0F) {
                f = 1.0F;
            }

            EntityArrow entityarrow = new EntityArrow(world, player, f * 2.0F);

            if (f == 1.0F) {
                entityarrow.setIsCritical(true);
            }

            int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);

            if (k > 0) {
                entityarrow.setDamage(entityarrow.getDamage() + k * 0.5D + 0.5D);
            }

            int l = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);

            if (l > 0) {
                entityarrow.setKnockbackStrength(l);
            }

            if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack) > 0) {
                entityarrow.setFire(100);
            }

            stack.damageItem(1, player);
            world.playSoundAtEntity(
                player,
                "random.bow",
                1.0F,
                1.0F / (bow.itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

            if (flag) {
                entityarrow.canBePickedUp = 2;
            } else {
                player.inventory.consumeInventoryItem(Items.arrow);
            }

            if (!world.isRemote) {
                world.spawnEntityInWorld(entityarrow);
            }
        }
    }
}
