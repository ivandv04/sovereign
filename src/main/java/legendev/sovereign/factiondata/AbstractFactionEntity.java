package legendev.sovereign.factiondata;

import java.util.HashMap;
import java.util.UUID;

public abstract class AbstractFactionEntity<K> {

    protected final K key;
    protected final HashMap<UUID, String> contained;

    protected AbstractFactionEntity(K key, HashMap<UUID, String> units) {
        this.key = key;
        this.contained = units;
    }

    public K getKey() {
        return key;
    }

    public boolean isOf(K key) {
        return this.key.equals(key);
    }

    public void addUnit(UUID unit, String name) {
        contained.put(unit, name);
    }

    public boolean tryRemoveUnit(UUID unit) {
        if (hasUnit(unit)) {
            contained.remove(unit);
            return true;
        }
        return false;
    }

    public void clearUnits() {
        contained.clear();
    }

    public String tryGetUnitName(UUID unit) {
        return contained.get(unit);
    }

    public int currentUnits() {
        return contained.size();
    }

    public boolean hasUnit(UUID unit) {
        for (UUID u : contained.keySet())
            if (u.equals(unit)) return true;
        return false;
    }

    public boolean hasUnits() {
        return currentUnits() > 0;
    }

}
