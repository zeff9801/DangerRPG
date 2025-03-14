package mixac1.dangerrpg.proxy;

import mixac1.dangerrpg.DangerRPG;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import mixac1.dangerrpg.init.*;
import mixac1.dangerrpg.util.RPGTicks;
import mixac1.dangerrpg.world.RPGEntityFXManager.IEntityFXType;

public class CommonProxy {

    protected RPGTicks serverTicks = new RPGTicks();

    public void displayDamageDealt(EntityLivingBase entityLiving) {}

    public void preInit(FMLPreInitializationEvent e) {
        RPGAnotherMods.load(e);

        RPGConfig.load(e);

        RPGNetwork.load(e);

        RPGItems.load(e);

        RPGGems.load();

        RPGBlocks.load(e);

        RPGRecipes.load(e);
    }

    public void init(FMLInitializationEvent e) {
        RPGEntities.load(e);

        RPGGuiHandlers.load(e);

        RPGEvents.load(e);
    }

    public void postInit(FMLPostInitializationEvent e) {
        RPGCapability.preLoad(e);

        RPGConfig.postLoadPre(e);

        RPGCapability.load(e);

        RPGConfig.postLoadPost(e);

        RPGCapability.postLoad(e);
    }

    public Side getSide() {
        return this instanceof ClientProxy ? Side.CLIENT : Side.SERVER;
    }

    public EntityPlayer getClientPlayer() {
        return null;
    }

    public EntityPlayer getPlayer(MessageContext ctx) {
        return ctx.getServerHandler().playerEntity;
    }

    public Entity getEntityByID(MessageContext ctx, int entityId) {
        return getPlayer(ctx).worldObj.getEntityByID(entityId);
    }

    protected RPGTicks getRPGTicks(Side side) {
        return serverTicks;
    }

    public void fireTick(Side side) {
        getRPGTicks(side).fireTick();
    }

    public int getTick(Side side) {
        return getRPGTicks(side).getTick();
    }

    public void spawnEntityFX(IEntityFXType fx, double x, double y, double z, double motionX, double motionY,
        double motionZ) {

    }

    public void spawnEntityFX(IEntityFXType fx, double x, double y, double z, double motionX, double motionY,
        double motionZ, int color) {

    }

    public void spawnEntityFX(IEntityFXType fx, double x, double y, double z, double motionX, double motionY,
        double motionZ, int color, int maxAge) {

    }
}
