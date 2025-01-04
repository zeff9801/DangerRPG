package mixac1.dangerrpg.mixins.common.vanilla.ItemSystem;

import mixac1.dangerrpg.capability.RPGItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class MixinItemStack {
    @Inject(method = "<init>", at = @At("RETURN"))
    public void initialize(CallbackInfo ci) {
        ItemStack stack = (ItemStack) (Object) this;
        if (RPGItemHelper.isRPGable(stack)) {
            RPGItemHelper.initRPGItem(stack);
        }
    }

    @Inject(method = "readFromNBT", at = @At("RETURN"))
    public void readFromNBT(NBTTagCompound nbt, CallbackInfo ci) {
        ItemStack stack = (ItemStack) (Object) this;
        if (RPGItemHelper.isRPGable(stack) && nbt != null) {
            RPGItemHelper.reinitRPGItem(stack);
        }
    }
}
