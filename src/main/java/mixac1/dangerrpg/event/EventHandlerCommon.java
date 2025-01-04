package mixac1.dangerrpg.event;

import mixac1.dangerrpg.api.event.ProjectileHitEvent;
import mixac1.dangerrpg.util.RPGHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import mixac1.dangerrpg.init.RPGConfig.MainConfig;
import mixac1.dangerrpg.init.RPGNetwork;
import mixac1.dangerrpg.network.MsgSyncConfig;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class EventHandlerCommon {

    @SubscribeEvent
    public void onPlayerLoggedInEvent(PlayerLoggedInEvent e) {
        if (MainConfig.d.mainEnableTransferConfig) {
            RPGNetwork.net.sendTo(new MsgSyncConfig(), (EntityPlayerMP) e.player);
        }
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        if (event.source.getSourceOfDamage() instanceof EntityLivingBase) {
            EntityLivingBase attacker = (EntityLivingBase) event.source.getSourceOfDamage();
            float modifiedDamage;

            if (event.source.isProjectile()) {
                modifiedDamage = RPGHelper.getRangeDamageHook(attacker, event.ammount);
            } else {
                modifiedDamage = RPGHelper.getMeleeDamageHook(attacker, event.ammount);
            }
            event.ammount = modifiedDamage;
        }
    }
    //TODO SUPPORT MORE PROJECTILES
    @SubscribeEvent
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.arrow == null || event.arrow.shootingEntity == null) {
            return;
        }
        EntityLivingBase attacker = (EntityLivingBase) event.arrow.shootingEntity;
        float modifiedDamage = RPGHelper.getRangeDamageHook(attacker, (float) event.arrow.getDamage());
        if (event.target == null) {
            return;
        }
        event.target.attackEntityFrom(DamageSource.causeArrowDamage(event.arrow, attacker), modifiedDamage);
    }
}
