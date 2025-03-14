package mixac1.dangerrpg.capability.ea;

import net.minecraft.entity.EntityLivingBase;

import mixac1.dangerrpg.api.entity.EntityAttribute.EAInteger;

public class EALvl extends EAInteger {

    public EALvl(String name) {
        super(name);
    }

    public boolean isInitedEntity(EntityLivingBase entity) {
        return getValueRaw(entity) > 0;
    }

    @Override
    public boolean isValid(Integer value) {
        return value >= 0;
    }

    @Override
    public boolean isConfigurable() {
        return false;
    }
}
