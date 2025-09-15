package legendev.sovereign.factiondata;

public enum CitizenType {

    PEASANT,
    INFANTRY,
    IDEOLOGUE,
    INSURGENT,
    MILITIA;

    /**
     * Is this citizen type assignable to a player or cornerstone
     */
    public boolean isAssignable() {
        return this == PEASANT || this == INFANTRY;
    }

}