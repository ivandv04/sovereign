package legendev.sovereign.util;

import legendev.sovereign.Sovereign;
import net.minecraft.network.packet.CustomPayload;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class PayloadUtil {

    private PayloadUtil() {
    }

    @Contract("_ -> new")
    public static <T extends CustomPayload> CustomPayload.@NotNull Id<T> createId(String name) {
        return CustomPayload.id(Sovereign.ID + ":" + name);
    }

}
