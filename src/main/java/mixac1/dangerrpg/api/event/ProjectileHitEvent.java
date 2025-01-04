package mixac1.dangerrpg.api.event;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;

public class ProjectileHitEvent extends Event {
    public final EntityLivingBase target;
    public final EntityArrow arrow;

    public ProjectileHitEvent(EntityLivingBase target, EntityArrow arrow) {
        this.target = target;
        this.arrow = arrow;
    }
}
