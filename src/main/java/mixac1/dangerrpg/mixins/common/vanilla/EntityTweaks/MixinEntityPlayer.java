package mixac1.dangerrpg.mixins.common.vanilla.EntityTweaks;

import mixac1.dangerrpg.api.event.ItemStackEvent;
import mixac1.dangerrpg.capability.PlayerAttributes;
import mixac1.dangerrpg.capability.RPGItemHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayer.class)
public class MixinEntityPlayer {
    @Overwrite
    public float getAIMoveSpeed() {
        EntityPlayer player = (EntityPlayer) (Object) this;
        if (player.isSneaking()) {
            return (float)player.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue() + PlayerAttributes.SNEAK_SPEED.getSafe(player, 0f) * 3;
        }
        return (float)player.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue() + PlayerAttributes.MOVE_SPEED.getSafe(player, 0f);
    }

    @Inject(method = "onLivingUpdate", at = @At("RETURN"))
    public void onLivingUpdate(CallbackInfo ci) {
        ((EntityPlayer) (Object) this).jumpMovementFactor += PlayerAttributes.JUMP_RANGE.getSafe((EntityPlayer) (Object) this, 0f);
    }
    @Overwrite
    public void attackTargetEntityWithCurrentItem(Entity entity) {
        EntityPlayer player = (EntityPlayer) (Object) this;
        if (MinecraftForge.EVENT_BUS.post(new AttackEntityEvent(player, entity))) {
            return;
        }

        ItemStack stack = player.getCurrentEquippedItem();
        if (stack != null) {
            if (stack.getItem()
                .onLeftClickEntity(stack, player, entity)) {
                return;
            }
        }

        if (entity.canAttackWithItem() && !entity.hitByEntity(player)) {
            if (entity instanceof EntityLivingBase && stack != null && RPGItemHelper.isRPGable(stack)) {
                if (PlayerAttributes.SPEED_COUNTER.getValue(player) > 0) {
                    return;
                }
            }

            float dmg = (float) player.getEntityAttribute(SharedMonsterAttributes.attackDamage)
                .getAttributeValue();
            float knockback = 0;
            float moreDmg = 0.0F;

            if (entity instanceof EntityLivingBase) {
                moreDmg = EnchantmentHelper.getEnchantmentModifierLiving(player, (EntityLivingBase) entity);
                knockback += EnchantmentHelper.getKnockbackModifier(player, (EntityLivingBase) entity);
            }

            if (player.isSprinting()) {
                ++knockback;
            }

            if (dmg > 0.0F || moreDmg > 0.0F) {
                boolean crit = player.fallDistance > 0.0F && !player.onGround
                    && !player.isOnLadder()
                    && !player.isInWater()
                    && !player.isPotionActive(Potion.blindness)
                    && player.ridingEntity == null
                    && entity instanceof EntityLivingBase;

                if (crit && dmg > 0.0F) {
                    dmg *= 1.5F;
                }

                dmg += moreDmg;

                boolean isFire = false;
                int fire = EnchantmentHelper.getFireAspectModifier(player);
                if (entity instanceof EntityLivingBase && fire > 0 && !entity.isBurning()) {
                    isFire = true;
                    entity.setFire(1);
                }

                float points = 0;
                if (entity instanceof EntityLivingBase) {
                    points = ((EntityLivingBase) entity).getHealth();
                }

                if (stack != null && entity instanceof EntityLivingBase) {
                    ItemStackEvent.HitEntityEvent event = new ItemStackEvent.HitEntityEvent(
                        stack,
                        (EntityLivingBase) entity,
                        player,
                        dmg,
                        knockback,
                        false);
                    MinecraftForge.EVENT_BUS.post(event);
                    dmg = event.newDamage;
                    knockback = event.knockback;
                }

                if (entity.attackEntityFrom(DamageSource.causePlayerDamage(player), dmg)) {
                    if (entity instanceof EntityLivingBase) {
                        points -= ((EntityLivingBase) entity).getHealth();
                        MinecraftForge.EVENT_BUS
                            .post(new ItemStackEvent.DealtDamageEvent(player, (EntityLivingBase) entity, stack, points));
                    }

                    if (knockback > 0) {
                        entity.addVelocity(
                            -MathHelper.sin(player.rotationYaw * (float) Math.PI / 180.0F) * knockback * 0.5F,
                            0.1D,
                            MathHelper.cos(player.rotationYaw * (float) Math.PI / 180.0F) * knockback * 0.5F);
                        player.motionX *= 0.6D;
                        player.motionZ *= 0.6D;
                        player.setSprinting(false);
                    }

                    if (crit) {
                        player.onCriticalHit(entity);
                    }

                    if (moreDmg > 0.0F) {
                        player.onEnchantmentCritical(entity);
                    }

                    if (dmg >= 18.0F) {
                        player.triggerAchievement(AchievementList.overkill);
                    }

                    player.setLastAttacker(entity);

                    if (entity instanceof EntityLivingBase) {
                        EnchantmentHelper.func_151384_a((EntityLivingBase) entity, player);
                    }

                    EnchantmentHelper.func_151385_b(player, entity);
                    ItemStack itemstack = player.getCurrentEquippedItem();
                    Object object = entity;

                    if (entity instanceof EntityDragonPart) {
                        IEntityMultiPart ientitymultipart = ((EntityDragonPart) entity).entityDragonObj;

                        if (ientitymultipart != null && ientitymultipart instanceof EntityLivingBase) {
                            object = ientitymultipart;
                        }
                    }

                    if (itemstack != null && object instanceof EntityLivingBase) {
                        itemstack.hitEntity((EntityLivingBase) object, player);

                        if (itemstack.stackSize <= 0) {
                            player.destroyCurrentEquippedItem();
                        }
                    }

                    if (entity instanceof EntityLivingBase) {
                        player.addStat(StatList.damageDealtStat, Math.round(dmg * 10.0F));

                        if (fire > 0) {
                            entity.setFire(fire * 4);
                        }
                    }

                    player.addExhaustion(0.3F);
                } else if (isFire) {
                    entity.extinguish();
                }
            }
        }
    }
}
