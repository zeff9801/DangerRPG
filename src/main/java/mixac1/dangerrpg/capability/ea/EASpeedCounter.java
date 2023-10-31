package mixac1.dangerrpg.capability.ea;

import mixac1.dangerrpg.api.entity.*;
import net.minecraft.entity.*;

public class EASpeedCounter extends EntityAttribute.EAFloat
{
    public EASpeedCounter(final String name) {
        super(name);
    }
    
    public void setValue(Float value, final EntityLivingBase entity) {
        if (!this.isValid((Object)value, entity)) {
            value = 0.0f;
        }
        if (this.setValueRaw((Object)value, entity)) {
            this.sync(entity);
        }
    }
    
    public void sync(final EntityLivingBase entity) {
        if ((float)this.getValueRaw(entity) == 0.0f) {
            super.sync(entity);
        }
    }
}
