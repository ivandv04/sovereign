package legendev.sovereign.factiondata;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public enum TerritoryType {

    CLAIM // Default type, no features or effects
            (0, 0, 1),
    FARM // Permits the growing of crops and livestock
            (1, 1, 4),
    RESIDENCE // Primary housing unit for your population
            (2, 8, 16),
    TEMPLE // Used for advanced ideology-related tasks
            (3, 0, 128),
    CAPITAL // Can be couped to switch ideologies
            (4, 1, 256),
    BARRACKS // Provides various buffs to your infantry
            (5, 0, 128),
    CAMP // Permits army organization in far away lands
            (6, 0, 128),
    MINE // Permits harvesting of precious ores
            (7, 0, 4);

    public final int id;
    public final int housing;
    public final int resilience;

    TerritoryType(int id, int housing, int resilience) {
        this.id = id;
        this.housing = housing;
        this.resilience = resilience;
    }

    public static TerritoryType defaultType() {
        return CLAIM;
    }

    public boolean isDefaultType() {
        return this == CLAIM;
    }

    public boolean permitsFarming() {
        return this == FARM;
    }

    public boolean permitsMining() {
        return this == MINE;
    }

    public boolean permitsConscripting() {
        return this == BARRACKS || this == CAMP;
    }

    public boolean requireConnected() {
        return this != CAMP && !this.isDefaultType();
    }

    public boolean canStoreCitizens() {
        return housing > 0;
    }

    public boolean canStartCoup() {
        return this == CAPITAL;
    }

    public boolean mustBeUnique() {
        return this == CAPITAL;
    }

    @Contract(pure = true)
    public static @Nullable TerritoryType fromId(int id) {
        for (TerritoryType t : values())
            if (t.id == id) return t;
        return null;
    }

}
