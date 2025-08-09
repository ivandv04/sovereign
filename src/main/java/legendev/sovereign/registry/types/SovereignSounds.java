package legendev.sovereign.registry.types;

import legendev.sovereign.Sovereign;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class SovereignSounds {

    private SovereignSounds() {
    }

    public static SoundEvent CITIZEN_ASSIGN = create("citizen_assign");

    @Contract("_ -> new")
    private static @NotNull SoundEvent create(String path) {
        return SoundEvent.of(new Identifier(Sovereign.ID, path));
    }

}
