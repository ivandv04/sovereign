package legendev.sovereign.factiondata;

import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class FactionMember extends AbstractFactionEntity<UUID> {

    private FactionMember(UUID key, HashMap<UUID, String> units) {
        super(key, units);
    }

    public static @NotNull FactionMember createDefault(UUID member) {
        return new FactionMember(member, new HashMap<>());
    }

    @Contract("_ -> new")
    public static @NotNull FactionMember construct(@NotNull NbtCompound nbt) {
        UUID pos = nbt.getUuid("pId");
        HashMap<UUID, String> res = new HashMap<>();
        NbtCompound resRaw = nbt.getCompound("infantry");
        for (String k : resRaw.getKeys()) {
            NbtCompound rRaw = resRaw.getCompound(k);
            res.put(rRaw.getUuid("id"), rRaw.getString("name"));
        }
        return new FactionMember(pos, res);
    }

    public static @NotNull NbtCompound deconstruct(@NotNull FactionMember member) {
        NbtCompound nbt = new NbtCompound();
        nbt.putUuid("pId", member.key);
        NbtCompound res = new NbtCompound();
        int resId = 0;
        for (UUID u : member.contained.keySet()) {
            NbtCompound r = new NbtCompound();
            r.putUuid("id", u);
            r.putString("name", member.contained.get(u));
            res.put("r" + resId++, r);
        }
        nbt.put("infantry", res);
        return nbt;
    }

}
