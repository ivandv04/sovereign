package legendev.sovereign.payload;

import legendev.sovereign.factiondata.CitizenType;
import legendev.sovereign.util.PayloadUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record CitizenAssignPayload(UUID citizen, CitizenType type, String name, long faction)
        implements CustomPayload {

    public static final Id<CitizenAssignPayload> ID
            = PayloadUtil.createId("citizen_assign_payload");

    public static final PacketCodec<PacketByteBuf, CitizenAssignPayload> CODEC
            = PacketCodec.of(CitizenAssignPayload::deconstruct, CitizenAssignPayload::construct);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    private static void deconstruct(@NotNull CitizenAssignPayload payload, @NotNull PacketByteBuf buf) {
        buf.writeUuid(payload.citizen).writeEnumConstant(payload.type)
                .writeString(payload.name).writeLong(payload.faction);
    }

    @Contract("_ -> new")
    private static @NotNull CitizenAssignPayload construct(@NotNull PacketByteBuf buf) {
        return new CitizenAssignPayload(buf.readUuid(),buf.readEnumConstant(CitizenType.class),
                buf.readString(), buf.readLong());
    }

}
