package mixac1.dangerrpg.mixins.common.vanilla.EntityTweaks;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.BaseAttribute;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.nbt.NBTTagList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SharedMonsterAttributes.class)
public class MixinSharedMonsterAttributes {

    @Inject(method = "func_151475_a", at = @At("TAIL"))
    private static void initialize(BaseAttributeMap p_151475_0_, NBTTagList p_151475_1_,CallbackInfo ci) {
        ((BaseAttribute) SharedMonsterAttributes.attackDamage).setShouldWatch(true);
    }
}
