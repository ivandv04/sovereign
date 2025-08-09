package legendev.sovereign.payload;

import legendev.sovereign.util.PayloadUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record CodexOpenPayload() implements CustomPayload {

    public static final CustomPayload.Id<CodexOpenPayload> ID
            =  PayloadUtil.createId("codex_open_payload");

    public static final PacketCodec<PacketByteBuf, CodexOpenPayload> CODEC
            = PacketCodec.of(CodexOpenPayload::deconstruct, CodexOpenPayload::construct);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    private static void deconstruct(CodexOpenPayload payload, PacketByteBuf buf) {
    }

    @Contract("_ -> new")
    private static @NotNull CodexOpenPayload construct(PacketByteBuf buf) {
        return new CodexOpenPayload();
    }

}
