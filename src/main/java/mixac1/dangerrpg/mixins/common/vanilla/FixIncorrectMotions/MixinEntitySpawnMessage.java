package mixac1.dangerrpg.mixins.common.vanilla;

import cpw.mods.fml.common.network.internal.FMLMessage;
import cpw.mods.fml.common.registry.IThrowableEntity;
import cpw.mods.fml.relauncher.ReflectionHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FMLMessage.EntitySpawnMessage.class)
public class MixinEntitySpawnMessage {

    /**
     * This method fixes incorrect motion setting for {@link IThrowableEntity}
     * when it spawns by modifying its motion values.
     */
    @Inject(method = "toBytes", at = @At("RETURN"),remap = false)
    public void fixThrowableEntityMotion(ByteBuf buf, CallbackInfo ci) {
        Entity entity = ReflectionHelper.getPrivateValue(FMLMessage.EntityMessage.class,(FMLMessage.EntitySpawnMessage) (Object) this, "entity"); // TODO GET RID OF THIS
        System.out.println("TESTTTTTTTTTTTTTTTTT called FIXED TOBYTES");
        if (entity instanceof IThrowableEntity) {
            // Fix the motion of throwable entities.
            buf.writeInt((int) (entity.motionX * 8000D));
            buf.writeInt((int) (entity.motionY * 8000D));
            buf.writeInt((int) (entity.motionZ * 8000D));
        }
    }
}
