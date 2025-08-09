package legendev.sovereign.payload;

import legendev.sovereign.factiondata.CitizenType;
import legendev.sovereign.util.PayloadUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record CitizenPapersOpenPayload(UUID citizen, CitizenType type, long faction) implements CustomPayload {

    public static final Id<CitizenPapersOpenPayload> ID
            = PayloadUtil.createId("citizen_papers_open_payload");

    public static final PacketCodec<PacketByteBuf, CitizenPapersOpenPayload> CODEC
            = PacketCodec.of(CitizenPapersOpenPayload::deconstruct, CitizenPapersOpenPayload::construct);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    private static void deconstruct(@NotNull CitizenPapersOpenPayload payload, @NotNull PacketByteBuf buf) {
        buf.writeUuid(payload.citizen).writeEnumConstant(payload.type).writeLong(payload.faction);
    }

    @Contract("_ -> new")
    private static @NotNull CitizenPapersOpenPayload construct(@NotNull PacketByteBuf buf) {
        return new CitizenPapersOpenPayload(buf.readUuid(),
                buf.readEnumConstant(CitizenType.class), buf.readLong());
    }

}
