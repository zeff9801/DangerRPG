package mixac1.dangerrpg.mixins.common.vanilla.BowSystem;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mixac1.dangerrpg.capability.ItemAttributes;
import mixac1.dangerrpg.capability.RPGItemHelper;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends EntityLivingBase implements ICommandSender {
    public MixinEntityPlayer(World p_i1594_1_) {
        super(p_i1594_1_);
    }

    @SideOnly(Side.CLIENT)
    @Overwrite
    public IIcon getItemIcon(ItemStack stack, int par) {
        IIcon iicon = super.getItemIcon(stack, par);
        EntityPlayer player = ((EntityPlayer) (Object) this);
        if (RPGItemHelper.isRPGable(stack) && player.getItemInUse() != null
            && stack.getItem() instanceof ItemBow
            && ItemAttributes.SHOT_SPEED.hasIt(stack)) {
            int ticks = stack.getMaxItemUseDuration() - player.getItemInUseCount();
            float speed = ItemAttributes.SHOT_SPEED.get(stack, player);
            ItemBow bow = (ItemBow) stack.getItem();
            try {
                if (ticks >= speed) {
                    iicon =  bow.getItemIconForUseDuration(2);
                } else if (ticks > speed / 2) {
                    iicon =  bow.getItemIconForUseDuration(1);
                } else if (ticks > 0) {
                    iicon =  bow.getItemIconForUseDuration(0);
                }
            } catch (NullPointerException e) {}
        }
        return iicon;
    }
}
