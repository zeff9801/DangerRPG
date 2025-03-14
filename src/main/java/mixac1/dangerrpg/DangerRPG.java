package mixac1.dangerrpg;

import cpw.mods.fml.common.event.*;

import mixac1.dangerrpg.init.RPGConfig;
import mixac1.dangerrpg.util.DangerLogger;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import mixac1.dangerrpg.proxy.CommonProxy;
import mixac1.dangerrpg.util.Utils;

@Mod(
    modid = DangerRPG.MODID,
    name = DangerRPG.MODNAME,
    version = DangerRPG.VERSION,
    acceptedMinecraftVersions = DangerRPG.ACCEPTED_VERSION,
    //dependencies = "required-after:Forge" + "required-after:iamacat_api")
    dependencies = "required-after:Forge" )
public class DangerRPG {
    public static final String MODNAME = "DangerRPG";
    public static final String MODID = "dangerrpg";
    public static final String VERSION = "1.1.4";
    public static final String ACCEPTED_VERSION = "[1.7.10]";

    @Instance(DangerRPG.MODID)
    public static DangerRPG instance = new DangerRPG();

    @SidedProxy(clientSide = "mixac1.dangerrpg.proxy.ClientProxy", serverSide = "mixac1.dangerrpg.proxy.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // TODO GET RID OF THIS
        if (Loader.isModLoaded("torohealthmod")) {
            throw new RuntimeException(
                "Custom Damage Particle mod is installed! Crashing the game Because DangerRPG add a thing like Custom Damage Particles mod, so pls remove DangerRPG or Custom Damage Particle");
        }
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    public static void log(Object... objs) {
        StringBuilder buf = new StringBuilder();
        for (Object obj : objs) {
            buf.append(obj != null ? obj.toString() : "(null)")
                .append(" ");
        }
        DangerLogger.logger.info(buf.toString());
    }

    public static void infoLog(Object... objs) {
        if (RPGConfig.MainConfig.d.mainEnableInfoLog) {
            DangerLogger.logger.info(Utils.toString(objs));
        }
    }

    public static String trans(String s) {
        return StatCollector.translateToLocal(s);
    }
}
