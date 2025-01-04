package mixac1.dangerrpg.mixins.common.vanilla;

import net.minecraft.entity.EntityTrackerEntry;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S0FPacketSpawnMob;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityTrackerEntry.class)
public abstract class MixinEntityTrackerEntry {

    // Un drapeau pour éviter la récursion infinie
    @Unique
    private boolean isProcessingVelocityPacket = false;

    @Unique
    private static S12PacketEntityVelocity createVelocityPacket(int entityId, double motionX, double motionY,
                                                                double motionZ) {
        S12PacketEntityVelocity packet = new S12PacketEntityVelocity();
        packet.field_149417_a = entityId;
        packet.field_149415_b = (int) (motionX * 8000.0D);
        packet.field_149416_c = (int) (motionY * 8000.0D);
        packet.field_149414_d = (int) (motionZ * 8000.0D);
        return packet;
    }

    @Shadow
    public void func_151261_b(Packet p_151261_1_)
    {
    }

    @Shadow
    public void func_151259_a(Packet p_151259_1_)
    {
    }

    @Inject(method = "func_151261_b(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void onFunc151261_b(Packet packet, CallbackInfo ci) {
        if (packet instanceof S12PacketEntityVelocity && !isProcessingVelocityPacket) {
            // Drapeau pour éviter la récursion infinie
            isProcessingVelocityPacket = true;

            S12PacketEntityVelocity velocityPacket = (S12PacketEntityVelocity) packet;
            Packet newPacket = createVelocityPacket(velocityPacket.func_149412_c(), velocityPacket.func_149411_d(), velocityPacket.func_149410_e(), velocityPacket.func_149409_f());
            this.func_151261_b(newPacket);

            // Réinitialiser le drapeau après l'envoi
            isProcessingVelocityPacket = false;
            ci.cancel();
        }
    }

    @Inject(method = "func_151259_a(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void onFunc151259_a(Packet packet, CallbackInfo ci) {
        if (packet instanceof S12PacketEntityVelocity && !isProcessingVelocityPacket) {
            // Drapeau pour éviter la récursion infinie
            isProcessingVelocityPacket = true;

            S12PacketEntityVelocity velocityPacket = (S12PacketEntityVelocity) packet;
            Packet newPacket = createVelocityPacket(velocityPacket.func_149412_c(), velocityPacket.func_149411_d(), velocityPacket.func_149410_e(), velocityPacket.func_149409_f());
            this.func_151259_a(newPacket);

            // Réinitialiser le drapeau après l'envoi
            isProcessingVelocityPacket = false;
            ci.cancel();
        }
    }

    @Inject(method = "tryStartWachingThis", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayerMP;sendPacket(Lnet/minecraft/network/Packet;)V", ordinal = 1), cancellable = true)
    public void onTryStartWatchingThis(EntityPlayerMP p_73117_1_, CallbackInfo ci) {
        EntityTrackerEntry tracker = (EntityTrackerEntry) (Object) this;

        if (tracker.sendVelocityUpdates && !(tracker.func_151260_c() instanceof S0FPacketSpawnMob)) {
            p_73117_1_.playerNetServerHandler.sendPacket(
                createVelocityPacket(
                    tracker.myEntity.getEntityId(),
                    tracker.myEntity.motionX,
                    tracker.myEntity.motionY,
                    tracker.myEntity.motionZ)
            );
            ci.cancel();
        }
    }
}
