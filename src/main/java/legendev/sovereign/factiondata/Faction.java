package legendev.sovereign.factiondata;

import legendev.sovereign.persistent.FactionCodexState;
import legendev.sovereign.util.FormatStrings;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.ChunkPos;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Faction {

    /*
     * NOTE:
     * Factions with the same codex key should be considered equivalent.
     */

    public long codexKey;

    public String name, passcode;
    public FactionIdeology ideology;

    public float raidProgress;
    public float paranoiaProgress;

    private final HashMap<ChunkPos, Integer> claimHistory;

    private final ArrayList<FactionMember> members;
    private final ArrayList<FactionCornerstone> cornerstones;

    private Faction(long codexKey, String name, String passcode, FactionIdeology ideology,
                    ArrayList<FactionMember> members, ArrayList<FactionCornerstone> cornerstones,
                    float raidProgress, float paranoiaProgress, HashMap<ChunkPos, Integer> claimHistory) {
        this.codexKey = codexKey;
        this.name = name;
        this.passcode = passcode;
        this.ideology = ideology;
        this.members = members;
        this.cornerstones = cornerstones;
        this.raidProgress = raidProgress;
        this.paranoiaProgress = paranoiaProgress;
        this.claimHistory = claimHistory;
    }

    @Contract("_, _, _, _ -> new")
    public static @NotNull Faction createNewDefault(@NotNull FactionCodexState codex,
                                                    String name, String passcode, UUID creator) {
        ArrayList<FactionMember> defM = new ArrayList<>();
        ArrayList<FactionCornerstone> defC = new ArrayList<>();
        HashMap<ChunkPos, Integer> h = new HashMap<>();
        defM.add(FactionMember.createDefault(creator));
        return new Faction(
                codex.getNewFactionKey(), name, passcode, defaultIdeology(),
                defM, defC, 0, 0, h);
    }

    public void copyStatsFrom(@NotNull Faction ref) {
        raidProgress = ref.raidProgress;
        paranoiaProgress = ref.paranoiaProgress;
        for (ChunkPos pos : ref.claimHistory.keySet()) {
            claimHistory.put(pos, ref.claimHistory.get(pos));
        }
    }

    public static FactionIdeology defaultIdeology() {
        return FactionIdeology.FEUDALISM;
    }

    public boolean hasOpenBorders() {
        return passcode.isEmpty();
    }

    public boolean canTrainNewUnits() {
        return totalInfantry() < getArmyCapacity();
    }

    public boolean tryAddMember(UUID uuid) {
        if (!hasMember(uuid)) {
            members.add(FactionMember.createDefault(uuid));
            return true;
        }
        return false;
    }

    public boolean tryRemoveMember(UUID uuid) {
        if (hasMember(uuid)) {
            for (FactionMember m : members) {
                if (m.isOf(uuid)) {
                    members.remove(m);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasMember(UUID uuid) {
        for (FactionMember m : members)
            if (m.isOf(uuid)) return true;
        return false;
    }

    public boolean tryAddCornerstoneAt(ChunkPos pos, TerritoryType type) {
        if (!hasCornerstoneAt(pos)) {
            cornerstones.add(FactionCornerstone.createDefault(pos, type));
            return true;
        }
        return false;
    }

    public boolean tryRemoveCornerstoneAt(ChunkPos pos) {
        if (hasCornerstoneAt(pos)) {
            for (FactionCornerstone c : cornerstones) {
                if (c.isOf(pos)) {
                    cornerstones.remove(c);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean tryTransferCornerstone(@NotNull Faction target, ChunkPos pos) {
        if (!hasCornerstoneAt(pos)) return false;
        target.cornerstones.add(getCornerstoneAt(pos));
        tryRemoveCornerstoneAt(pos);
        return true;
    }

    public boolean hasCornerstoneAt(ChunkPos pos) {
        for (FactionCornerstone c : cornerstones)
            if (c.isOf(pos)) return true;
        return false;
    }

    public boolean hasPeasant(UUID peasant) {
        for (FactionCornerstone c : cornerstones)
            if (c.hasUnit(peasant)) return true;
        return false;
    }

    public boolean tryRemovePeasant(UUID peasant) {
        for (FactionCornerstone c : cornerstones)
            if (c.tryRemoveUnit(peasant)) return true;
        return false;
    }

    public boolean hasInfantry(UUID infantry) {
        for (FactionMember m : members)
            if (m.hasUnit(infantry)) return true;
        return false;
    }

    public boolean tryRemoveInfantry(UUID infantry) {
        for (FactionMember m : members)
            if (m.tryRemoveUnit(infantry)) return true;
        return false;
    }

    /**
     * Checks if there is an upgraded cornerstone in the chunk
     * directly above, below, left, or right of the given one.
     *
     * @param pos the Chunk position to investigate
     * @return true if upgraded cornerstone found, otherwise false
     */
    public boolean fitsConnectReq(ChunkPos pos) {
        for (FactionCornerstone c : cornerstones) {
            if (!c.getType().isDefaultType() && c.getType().requireConnected()) {
                int distX = Math.abs(c.getKey().x - pos.x);
                int distZ = Math.abs(c.getKey().z - pos.z);
                if (distX + distZ == 1) return true;
            }
        }
        return false;
    }

    public boolean hasCapital() {
        for (FactionCornerstone c : cornerstones)
            if (c.getType() == TerritoryType.CAPITAL) return true;
        return false;
    }

    public boolean hasArmy() {
        for (FactionMember m : members)
            if (m.hasUnits()) return true;
        return false;
    }

    public FactionCornerstone getCornerstoneAt(ChunkPos pos) {
        for (FactionCornerstone c : cornerstones)
            if (c.isOf(pos)) return c;
        return null;
    }

    public FactionCornerstone getCornerstoneWith(UUID peasant) {
        for (FactionCornerstone c : cornerstones)
            if (c.hasUnit(peasant)) return c;
        return null;
    }

    public FactionMember getMember(UUID member) {
        for (FactionMember m : members)
            if (m.isOf(member)) return m;
        return null;
    }

    public FactionMember getMemberWith(UUID infantry) {
        for (FactionMember m : members)
            if (m.hasUnit(infantry)) return m;
        return null;
    }

    public boolean isRemovable() {
        return !hasArmy() && cornerstones.isEmpty() && totalMembers() == 1;
    }

    public boolean canPassConnected() {
        boolean noNonReq = true;
        for (FactionCornerstone c : cornerstones)
            if (c.getType().requireConnected()) noNonReq = false;
        return noNonReq;
    }

    public boolean inRaid() {
        return raidProgress > 0;
    }

    public boolean inUprising() {
        return paranoiaProgress >= 1;
    }

    public boolean inDanger() {
        return inRaid() || inUprising();
    }

    public int totalCornerstones() {
        return cornerstones.size();
    }

    public int totalMembers() {
        return members.size();
    }

    public int totalPeasants() {
        int p = 0;
        for (FactionCornerstone c : cornerstones) p += c.currentUnits();
        return p;
    }

    public int totalInfantry() {
        int i = 0;
        for (FactionMember m : members) i += m.currentUnits();
        return i;
    }

    public int getArmyCapacity() {
        return (int) (totalPeasants() * ideology.conscription);
    }

    public static @NotNull NbtCompound deconstruct(@NotNull Faction faction) {
        NbtCompound nbt = new NbtCompound();
        // basic fields
        nbt.putLong("key", faction.codexKey);
        nbt.putString("name", faction.name);
        nbt.putString("passcode", faction.passcode);
        nbt.putInt("ideology", faction.ideology.id);
        nbt.putFloat("raid", faction.raidProgress);
        nbt.putFloat("paranoia", faction.paranoiaProgress);
        // deconstruct members
        NbtCompound mems = new NbtCompound();
        int memId = 0;
        for (FactionMember m : faction.members)
            mems.put("mem" + memId++, FactionMember.deconstruct(m));
        nbt.put("members", mems);
        // deconstruct cornerstones
        NbtCompound crns = new NbtCompound();
        int crnId = 0;
        for (FactionCornerstone c : faction.cornerstones)
            crns.put("crn" + crnId++, FactionCornerstone.deconstruct(c));
        nbt.put("cornerstones", crns);
        // deconstruct claim history
        NbtCompound his = new NbtCompound();
        int hisId = 0;
        for (ChunkPos pos : faction.claimHistory.keySet()) {
            NbtCompound c = new NbtCompound();
            c.putInt("x", pos.x);
            c.putInt("z", pos.z);
            c.putInt("time", faction.claimHistory.get(pos));
            his.put("h" + hisId++, c);
        }
        nbt.put("history", his);
        // return compound
        return nbt;
    }

    @Contract("_ -> new")
    public static @NotNull Faction construct(@NotNull NbtCompound nbt) {
        // build members array
        ArrayList<FactionMember> mems = new ArrayList<>();
        NbtCompound memsRaw = nbt.getCompound("members");
        for (String k : memsRaw.getKeys())
            mems.add(FactionMember.construct(memsRaw.getCompound(k)));
        // build cornerstones array
        ArrayList<FactionCornerstone> crns = new ArrayList<>();
        NbtCompound crnsRaw = nbt.getCompound("cornerstones");
        for (String k : crnsRaw.getKeys())
            crns.add(FactionCornerstone.construct(crnsRaw.getCompound(k)));
        // build history map
        HashMap<ChunkPos, Integer> his = new HashMap<>();
        NbtCompound hisRaw = nbt.getCompound("history");
        for (String k : hisRaw.getKeys()) {
            NbtCompound h = hisRaw.getCompound(k);
            his.put(new ChunkPos(h.getInt("x"), h.getInt("z")), h.getInt("time"));
        }
        // build full
        return new Faction(
                nbt.getLong("key"), nbt.getString("name"), nbt.getString("passcode"),
                FactionIdeology.fromId(nbt.getInt("ideology")),
                mems, crns, nbt.getFloat("raid"), nbt.getFloat("paranoia"), his
        );
    }

    public String fullName() {
        return ideology.prefix + name;
    }

    public String baseName() {
        return name;
    }

    public String fullNameColoured(boolean baseOnly) {
        return baseOnly
                ? (ideology.col + ideology.prefix + name + FormatStrings.WHITE)
                : (ideology.prefix + ideology.col + name + FormatStrings.WHITE);
    }

    public String baseNameColoured() {
        return ideology.col + name + FormatStrings.WHITE;
    }

    public boolean is(Faction faction) {
        if (faction == null) return false;
        return codexKey == faction.codexKey;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Faction)) return false;
        return codexKey == ((Faction) obj).codexKey;
    }

    @Override
    public String toString() {
        String w = FormatStrings.WHITE;
        String g = FormatStrings.GRAY;
        return fullName()
                + " (" + ideology.col + ideology.adj + w
                + ", " + g + totalMembers() + "m" + w + "/"
                + g + totalInfantry() + "i" + w + ", "
                + g + totalCornerstones() + "c" + w + "/"
                + g + totalPeasants() + "p" + w + ")";
    }

}
