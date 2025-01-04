package mixac1.dangerrpg.mixins.common.vanilla.ArrowReplacement;

import lombok.Getter;
import lombok.Setter;
import mixac1.dangerrpg.capability.ItemAttributes;
import mixac1.dangerrpg.api.event.ItemStackEvent.DealtDamageEvent;
import mixac1.dangerrpg.api.event.ItemStackEvent.HitEntityEvent;
import mixac1.dangerrpg.api.event.ProjectileHitEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.util.DamageSource;

@Mixin(EntityArrow.class)
public abstract class MixinEntityArrow extends Entity implements IProjectile {

    @Shadow public Entity shootingEntity;
    @Setter
    @Getter
    @Unique
    protected ItemStack bowStack;
    @Unique
    protected float phisicDamage;
    @Unique
    private boolean hasHitEntity = false;
    @Unique
    private boolean initialized = false;

    public MixinEntityArrow(World worldIn) {
        super(worldIn);
    }

    @Inject(method = "onUpdate", at = @At("HEAD"))
    public void onUpdate(CallbackInfo ci) {
         if (this.worldObj.isRemote){
             return;
         }
         if (!initialized){
            initialized = true;
            if (shootingEntity instanceof EntityPlayer) {
                ItemStack stack = ((EntityPlayer) shootingEntity).getHeldItem();
                if (stack != null && stack.getItem() instanceof ItemBow) {
                    setBowStack(stack.copy());
                }
            } else if (shootingEntity instanceof EntityLivingBase) {
                ItemStack stack = ((EntityLivingBase) shootingEntity).getHeldItem();
                if (stack != null && stack.getItem() instanceof ItemBow) {
                    setBowStack(stack.copy());
                }
            }
        }
        if (!this.onGround && !hasHitEntity) {
            Entity entity = this.worldObj.findNearestEntityWithinAABB(EntityLivingBase.class, this.boundingBox.expand(1.0D, 1.0D, 1.0D), this);

            if (entity instanceof EntityLivingBase && entity != this.shootingEntity) {
                EntityLivingBase target = (EntityLivingBase) entity;
                hasHitEntity = true;
                float points = target.getHealth();
                if (bowStack != null) {
                    if (ItemAttributes.SHOT_DAMAGE.hasIt(bowStack)) {
                        phisicDamage = ItemAttributes.SHOT_DAMAGE.get(bowStack);
                    } else if (ItemAttributes.MELEE_DAMAGE.hasIt(bowStack)) {
                        phisicDamage = ItemAttributes.MELEE_DAMAGE.get(bowStack);
                    }
                    if (shootingEntity instanceof EntityLivingBase) {
                        EntityLivingBase shooter = (EntityLivingBase) shootingEntity;
                        HitEntityEvent event = new HitEntityEvent(bowStack, target, shooter, phisicDamage, 0, true);
                        MinecraftForge.EVENT_BUS.post(event);
                        phisicDamage = event.newDamage;
                    }
                }
                points -= target.getHealth();
                if (shootingEntity instanceof EntityPlayer) {
                    MinecraftForge.EVENT_BUS.post(new DealtDamageEvent(
                            (EntityPlayer) shootingEntity,
                            target,
                            bowStack,
                            points
                    ));
                }
                target.attackEntityFrom(DamageSource.causeArrowDamage((EntityArrow) (Object) this, this.shootingEntity), phisicDamage);
                MinecraftForge.EVENT_BUS.post(new ProjectileHitEvent(target, (EntityArrow) (Object) this));
                this.setDead();
            }
        }
    }
}
