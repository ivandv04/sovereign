package legendev.sovereign.factiondata;

import legendev.sovereign.util.FormatStrings;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public enum FactionIdeology {

    ANARCHISM(0,
            "Anarchism",
            "Anarchist",
            "Free Territory of ",
            FormatStrings.DARK_RED,
            0.00f),

    COMMUNALISM(1,
            "Communalism",
            "Communalist",
            "Commune of ",
            FormatStrings.RED,
            0.34f),

    FEUDALISM(2,
            "Feudalism",
            "Feudal",
            "Fiefdom of ",
            FormatStrings.GRAY,
            0.50f),

    OLIGARCHISM(3,
            "Oligarchism",
            "Oligarchic",
            "Federation of ",
            FormatStrings.YELLOW,
            0.67f),

    TOTALITARIANISM(4,
            "Totalitarianism",
            "Totalitarian",
            "Unitary State of ",
            FormatStrings.DARK_YELLOW,
            1.50f);

    public final int id;
    public final String name;
    public final String adj;
    public final String prefix;
    public final String col;

    /*
     * The conscription ratio of a faction is the number of infantry
     * which may be trained or summoned per peasant in that faction.
     * Factions inherit the conscription ratio of whichever ideology
     * they possess. For example, a Communalist faction with a total
     * peasant population of 60 across all cornerstones would be able
     * to hold a standing army of 20 infantry entities. Any attempt at
     * conscripting new infantry at this point would fail for them.
     *
     * Note that this value is only relevant when training or summoning
     * NEW infantry entities. If the Communalist faction mentioned above
     * were to lose all of their peasants, they could still retain and
     * interact with their 20-strong army. However, they would NOT be
     * able to replenish their numbers until their peasant population
     * is back up and restored.
     */
    public final float conscription;
    
    FactionIdeology(int id, String name, String adj, String prefix, String col,
                    float conscription) {
        this.id = id;
        this.name = name;
        this.adj = adj;
        this.prefix = prefix;
        this.col = col;
        this.conscription = conscription;
    }

    public static FactionIdeology defaultIdeology() {
        return FEUDALISM;
    }

    public boolean isDefault() {
        return this == FEUDALISM;
    }

    public boolean isIntermediate() {
        return this == COMMUNALISM || this == OLIGARCHISM;
    }

    public boolean isExtremist() {
        return this == ANARCHISM || this == TOTALITARIANISM;
    }

    public boolean isLeftWing() {
        return this == ANARCHISM || this == COMMUNALISM;
    }

    public boolean isRightWing() {
        return this == OLIGARCHISM || this == TOTALITARIANISM;
    }

    @Contract(pure = true)
    public static @Nullable FactionIdeology fromId(int id) {
        for (FactionIdeology f : values())
            if (f.id == id) return f;
        return null;
    }

}
