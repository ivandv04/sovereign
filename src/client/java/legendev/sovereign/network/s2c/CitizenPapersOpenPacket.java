package legendev.sovereign.network.s2c;

import legendev.sovereign.factiondata.CitizenType;
import legendev.sovereign.payload.CitizenPapersOpenPayload;
import legendev.sovereign.screen.ConscriptPapersScreen;
import legendev.sovereign.screen.InteractiveBookScreen;
import legendev.sovereign.screen.ResidentPapersScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class CitizenPapersOpenPacket {

    private CitizenPapersOpenPacket() {
    }

    public static void receive(CitizenPapersOpenPayload payload, ClientPlayNetworking.@NotNull Context context) {
        context.client().execute(() -> {
            if (context.player() != null) {
                InteractiveBookScreen toOpen = null;
                CitizenType type = payload.type();
                assert type.isAssignable();
                UUID c = payload.citizen();
                long f = payload.faction();
                if (type == CitizenType.PEASANT) toOpen = new ResidentPapersScreen(c, f);
                else if (type == CitizenType.INFANTRY) toOpen = new ConscriptPapersScreen(c, f);
                context.player().playSoundToPlayer(
                        SoundEvents.ITEM_BOOK_PAGE_TURN, SoundCategory.AMBIENT,
                        1.0f, 1.0f);
                context.client().setScreenAndRender(toOpen);
            }
        });
    }
}
