package legendev.sovereign.payload;

import legendev.sovereign.factiondata.FactionIdeology;
import legendev.sovereign.util.PayloadUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record CoupPayload(FactionIdeology type, String newName, String newCode) implements CustomPayload {

    public static final Id<CoupPayload> ID = PayloadUtil.createId("coup_payload");

    public static final PacketCodec<PacketByteBuf, CoupPayload> CODEC
            = PacketCodec.of(CoupPayload::deconstruct, CoupPayload::construct);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    private static void deconstruct(@NotNull CoupPayload payload, @NotNull PacketByteBuf buf) {
        buf.writeEnumConstant(payload.type).writeString(payload.newName).writeString(payload.newCode);
    }

    @Contract("_ -> new")
    private static @NotNull CoupPayload construct(@NotNull PacketByteBuf buf) {
        return new CoupPayload(buf.readEnumConstant(FactionIdeology.class), buf.readString(), buf.readString());
    }

}
