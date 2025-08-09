package legendev.sovereign.factiondata;

public enum CitizenType {

    PEASANT,
    INFANTRY,
    IDEOLOGUE,
    INSURGENT,
    MILITIA;

    public boolean isAssignable() {
        return this == PEASANT || this == INFANTRY;
    }



}