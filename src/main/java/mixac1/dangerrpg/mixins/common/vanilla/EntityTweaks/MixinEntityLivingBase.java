package mixac1.dangerrpg.mixins.common.vanilla.EntityTweaks;

import mixac1.dangerrpg.capability.PlayerAttributes;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLivingBase.class)
public class MixinEntityLivingBase {
    @Inject(method = "moveEntityWithHeading", at = @At("RETURN"))
    public void moveEntityWithHeading(float par1, float par2,CallbackInfo ci) {
        EntityLivingBase entity = ((EntityLivingBase)(Object) this);
        if (entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isFlying
            && entity.ridingEntity == null) {
            entity.jumpMovementFactor += PlayerAttributes.FLY_SPEED.getSafe(entity, 0f);
        }
    }
}
