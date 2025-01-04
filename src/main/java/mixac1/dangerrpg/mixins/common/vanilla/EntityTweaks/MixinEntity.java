package mixac1.dangerrpg.mixins.common.vanilla.EntityTweaks;

import mixac1.dangerrpg.capability.PlayerAttributes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class MixinEntity {
    @Overwrite
    public void moveFlying(float par1, float par2, float speed) {
        Entity entity = (Entity) (Object) this;
        float f3 = par1 * par1 + par2 * par2;

        if (f3 >= 1.0E-4F) {
            if (entity instanceof EntityPlayer) {
                if (!((EntityPlayer) entity).capabilities.isFlying) {
                    if (entity.isInWater()) {
                        speed += PlayerAttributes.SWIM_SPEED.getSafe((EntityLivingBase) entity, 0f);
                    } else if (entity.handleLavaMovement()) {
                        speed += PlayerAttributes.SWIM_SPEED.getSafe((EntityLivingBase) entity, 0f) / 2;
                    }
                }
            }

            f3 = MathHelper.sqrt_float(f3);

            if (f3 < 1.0F) {
                f3 = 1.0F;
            }

            f3 = speed / f3;
            par1 *= f3;
            par2 *= f3;
            float f4 = MathHelper.sin(entity.rotationYaw * (float) Math.PI / 180.0F);
            float f5 = MathHelper.cos(entity.rotationYaw * (float) Math.PI / 180.0F);
            entity.motionX += par1 * f5 - par2 * f4;
            entity.motionZ += par2 * f5 + par1 * f4;
        }
    }
}
