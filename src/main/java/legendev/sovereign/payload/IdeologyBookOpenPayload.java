package legendev.sovereign.payload;

import legendev.sovereign.factiondata.FactionIdeology;
import legendev.sovereign.util.PayloadUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record IdeologyBookOpenPayload(FactionIdeology type, String name, String code) implements CustomPayload {

    public static final Id<IdeologyBookOpenPayload> ID
            = PayloadUtil.createId("ideology_book_open_payload");

    public static final PacketCodec<PacketByteBuf, IdeologyBookOpenPayload> CODEC
            = PacketCodec.of(IdeologyBookOpenPayload::deconstruct, IdeologyBookOpenPayload::construct);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    private static void deconstruct(@NotNull IdeologyBookOpenPayload payload, @NotNull PacketByteBuf buf) {
        buf.writeEnumConstant(payload.type)
                .writeString(payload.name).writeString(payload.code);
    }

    @Contract("_ -> new")
    private static @NotNull IdeologyBookOpenPayload construct(@NotNull PacketByteBuf buf) {
        return new IdeologyBookOpenPayload(buf.readEnumConstant(FactionIdeology.class),
                buf.readString(), buf.readString());
    }

}
