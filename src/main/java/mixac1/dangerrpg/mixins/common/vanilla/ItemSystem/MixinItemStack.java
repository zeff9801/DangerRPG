package mixac1.dangerrpg.mixins.common.vanilla.ItemSystem;

import mixac1.dangerrpg.capability.ItemAttributes;
import mixac1.dangerrpg.capability.RPGItemHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class MixinItemStack {
    @Shadow
    private cpw.mods.fml.common.registry.RegistryDelegate<Item> delegate;
    @Inject(method = "<init>", at = @At("RETURN"))
    public void initialize(CallbackInfo ci) {
        ItemStack stack = (ItemStack) (Object) this;
        if (RPGItemHelper.isRPGable(stack)) {
            RPGItemHelper.initRPGItem(stack);
        }
    }

    @Inject(method = "readFromNBT", at = @At("RETURN"))
    public void readFromNBT(NBTTagCompound nbt,CallbackInfo ci) {
        ItemStack stack = (ItemStack) (Object) this;
        if (RPGItemHelper.isRPGable(stack)) {
            RPGItemHelper.reinitRPGItem(stack);
        }
    }

    @Overwrite
    public int getMaxDamage() {
        ItemStack stack = (ItemStack) (Object) this;
        if (RPGItemHelper.isRPGable(stack) && ItemAttributes.MAX_DURABILITY.hasIt(stack)) {
            return (int) ItemAttributes.MAX_DURABILITY.get(stack);
        }
        return getItem().getMaxDamage(stack);
    }

    @Shadow
    public Item getItem()
    {
        return this.delegate != null ? this.delegate.get() : null;
    }
}
