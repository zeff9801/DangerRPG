package mixac1.dangerrpg.mixins.common.vanilla.EntityTweaks;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.BaseAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SharedMonsterAttributes.class)
public class MixinSharedMonsterAttributes {
    @Inject(method = "<init>", at = @At("RETURN"))
    public void initialize(CallbackInfo ci) {
        ((BaseAttribute) SharedMonsterAttributes.attackDamage).setShouldWatch(true);
    }
}
