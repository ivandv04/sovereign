package legendev.sovereign.util;

import legendev.sovereign.Sovereign;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class GeoUtil {

    private GeoUtil() {
    }

    @Contract("_ -> new")
    public static @NotNull Identifier getGeoModel(String name) {
        return new Identifier(Sovereign.ID, "geo/" + name + ".geo.json");
    }

    @Contract("_ -> new")
    public static @NotNull Identifier getAnimation(String name) {
        return new Identifier(Sovereign.ID, "animations/" + name + ".animation.json");
    }

}
