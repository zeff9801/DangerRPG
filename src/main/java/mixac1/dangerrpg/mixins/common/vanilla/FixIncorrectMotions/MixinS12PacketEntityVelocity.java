package mixac1.dangerrpg.mixins.common.vanilla;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.io.IOException;

@Mixin(S12PacketEntityVelocity.class)
public class MixinS12PacketEntityVelocity {

    @Overwrite
    public void readPacketData(PacketBuffer buf) throws IOException {
        System.out.println("TESTTTTTTTTTTTTTTTTT called FIXED readPacketData");
        ((S12PacketEntityVelocity) (Object) this).field_149417_a = buf.readInt();
        ((S12PacketEntityVelocity) (Object) this).field_149415_b = buf.readInt();
        ((S12PacketEntityVelocity) (Object) this).field_149416_c = buf.readInt();
        ((S12PacketEntityVelocity) (Object) this).field_149414_d = buf.readInt();
    }

    @Overwrite
    public  void writePacketData(PacketBuffer buf) throws IOException {
        System.out.println("TESTTTTTTTTTTTTTTTTT called FIXED writePacketData");
        buf.writeInt(((S12PacketEntityVelocity) (Object) this).field_149417_a);
        buf.writeInt(((S12PacketEntityVelocity) (Object) this).field_149415_b);
        buf.writeInt(((S12PacketEntityVelocity) (Object) this).field_149416_c);
        buf.writeInt(((S12PacketEntityVelocity) (Object) this).field_149414_d);
    }
}
