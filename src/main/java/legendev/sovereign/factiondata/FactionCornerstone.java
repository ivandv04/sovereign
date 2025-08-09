package legendev.sovereign.factiondata;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.ChunkPos;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class FactionCornerstone extends AbstractFactionEntity<ChunkPos> {

    private TerritoryType type;
    private int resilience;

    private FactionCornerstone(ChunkPos key, HashMap<UUID, String> units,
                               TerritoryType type, int resilience) {
        super(key, units);
        this.type = type;
        this.resilience = resilience;
    }

    @Contract("_, _ -> new")
    public static @NotNull FactionCornerstone createDefault(ChunkPos pos, TerritoryType type) {
        return new FactionCornerstone(pos, new HashMap<>(), type, type.resilience);
    }

    public void setType(TerritoryType type) {
        this.type = type;
    }

    public TerritoryType getType() {
        return type;
    }

    public boolean tryAdd(UUID peasant, String name) {
        if (!type.canStoreCitizens() || type.housing == currentUnits()) return false;
        addUnit(peasant, name);
        return true;
    }

    public boolean tryDamage(int amount) {
        if (resilience == 0) return false;
        resilience -= amount;
        if (resilience < 0) resilience = 0;
        return true;
    }

    public boolean tryHeal(int amount) {
        if (resilience == maxHP()) return false;
        resilience += amount;
        if (resilience > maxHP()) resilience = maxHP();
        return true;
    }

    public int maxHP() {
        return type.resilience;
    }

    @Contract("_ -> new")
    public static @NotNull FactionCornerstone construct(@NotNull NbtCompound nbt) {
        ChunkPos pos = new ChunkPos(nbt.getInt("posX"), nbt.getInt("posZ"));
        TerritoryType type = TerritoryType.fromId(nbt.getInt("type"));
        int hp = nbt.getInt("hp");
        HashMap<UUID, String> res = new HashMap<>();
        NbtCompound resRaw = nbt.getCompound("peasants");
        for (String k : resRaw.getKeys()) {
            NbtCompound rRaw = resRaw.getCompound(k);
            res.put(rRaw.getUuid("id"), rRaw.getString("name"));
        }
        return new FactionCornerstone(pos, res, type, hp);
    }

    public static @NotNull NbtCompound deconstruct(@NotNull FactionCornerstone territory) {
        NbtCompound nbt = new NbtCompound();
        nbt.putInt("posX", territory.key.x);
        nbt.putInt("posZ", territory.key.z);
        nbt.putInt("type", territory.type.id);
        nbt.putInt("hp", territory.resilience);
        NbtCompound res = new NbtCompound();
        int resId = 0;
        for (UUID u : territory.contained.keySet()) {
            NbtCompound r = new NbtCompound();
            r.putUuid("id", u);
            r.putString("name", territory.contained.get(u));
            res.put("r" + resId++, r);
        }
        nbt.put("peasants", res);
        return nbt;
    }

}
