package mixac1.dangerrpg.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import mixac1.dangerrpg.api.event.ItemStackEvent.DealtDamageEvent;
import mixac1.dangerrpg.api.event.ItemStackEvent.HitEntityEvent;
import mixac1.dangerrpg.capability.ItemAttributes;
import mixac1.dangerrpg.entity.projectile.core.EntityMaterial;

public class EntityRPGArrow extends EntityMaterial {

    private ItemStack bowStack;

    public EntityRPGArrow(World world) {
        super(world);
    }

    public EntityRPGArrow(World world, ItemStack stack) {
        super(world, new ItemStack(Items.arrow, 1));
        this.bowStack = stack;
    }

    public EntityRPGArrow(World world, ItemStack stack, double x, double y, double z) {
        super(world, new ItemStack(Items.arrow, 1), x, y, z);
        this.bowStack = stack;
    }

    public EntityRPGArrow(World world, ItemStack stack, EntityLivingBase thrower, float speed, float deviation) {
        super(world, thrower, new ItemStack(Items.arrow, 1), speed, deviation);
        this.bowStack = stack;
    }

    public EntityRPGArrow(World world, ItemStack stack, EntityLivingBase thrower, EntityLivingBase target, float speed,
                          float deviation) {
        super(world, thrower, target, new ItemStack(Items.arrow, 1), speed, deviation);
        this.bowStack = stack;
    }

    @Override
    public void applyEntityHitEffects(EntityLivingBase entity, float dmgMul) {
        float points = entity.getHealth();

        if (bowStack != null && bowStack.getItem() != Items.arrow) {
            if (ItemAttributes.SHOT_DAMAGE.hasIt(bowStack)) {
                phisicDamage = ItemAttributes.SHOT_DAMAGE.get(bowStack);
            } else if (ItemAttributes.MELEE_DAMAGE.hasIt(bowStack)) {
                phisicDamage = ItemAttributes.MELEE_DAMAGE.get(bowStack);
            }
            HitEntityEvent event = new HitEntityEvent(bowStack, entity, thrower, phisicDamage, 0, true);
            MinecraftForge.EVENT_BUS.post(event);
            phisicDamage = event.newDamage;
        }

        super.applyEntityHitEffects(entity, dmgMul);

        points -= entity.getHealth();
        if (thrower instanceof EntityPlayer) {
            MinecraftForge.EVENT_BUS.post(new DealtDamageEvent((EntityPlayer) thrower, entity, bowStack, points));
        }
    }

    @Override
    public float getDamageMul() {
        return MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
    }

    @Override
    public boolean dieAfterEntityHit() {
        return true;
    }
}
