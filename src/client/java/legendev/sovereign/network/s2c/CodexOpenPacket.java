package legendev.sovereign.network.s2c;

import legendev.sovereign.payload.CodexOpenPayload;
import legendev.sovereign.screen.WorldCodexScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.jetbrains.annotations.NotNull;

public final class CodexOpenPacket {

    private CodexOpenPacket() {
    }

    public static void receive(CodexOpenPayload payload, ClientPlayNetworking.@NotNull Context context) {
        context.client().execute(() -> {
            if (context.player() != null) {
                context.player().playSoundToPlayer(
                        SoundEvents.ITEM_BOOK_PAGE_TURN, SoundCategory.AMBIENT,
                        1.0f, 1.0f);
                context.client().setScreenAndRender(new WorldCodexScreen());
            }
        });
    }
}
