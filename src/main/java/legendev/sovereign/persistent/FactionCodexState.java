package legendev.sovereign.persistent;

import legendev.sovereign.Sovereign;
import legendev.sovereign.factiondata.Faction;
import legendev.sovereign.factiondata.FactionCornerstone;
import legendev.sovereign.factiondata.FactionMember;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class FactionCodexState extends PersistentState {

    /*
     * REMEMBER THAT FACTIONS OF THE SAME KEY ARE TREATED AS
     * THOUGH THEY ARE THE SAME, REGARDLESS OF CONTENTS!
     */

    /*
     * REMEMBER THAT THE CODEX FACTION ARRAY (FOR SAFETY
     * PURPOSES) SHOULD ONLY EVER BE ACCESSED IN THIS CLASS!
     */

    private final ArrayList<Faction> factions = new ArrayList<>();

    private long factionKeyUpper = 1L;

    public static final String CHAT_REFER = "CODEX";

    public long getNewFactionKey() {
        return factionKeyUpper++;
    }

    public int getFactionAmount() {
        return factions.size();
    }

    /**
     * Tries to add the given faction to the world codex under the UUID of the given player.
     * Fails if a faction of the same name or codex key already exists.
     *
     * @param faction the faction to be added
     * @param creator the creator of the faction
     * @return true if the faction is added, false otherwise
     */
    public boolean tryToAddFaction(Faction faction, UUID creator) {
        for (Faction f : factions) {
            if (f.name.equals(faction.name) || f.is(faction)) return false;
        }
        tryToPickPlayer(creator);
        factions.add(faction);
        return true;
    }

    /**
     * Tries to remove the faction of the given name from the world codex.
     * Suceeds if a faction of the given name is found (and removed).
     *
     * @param name the name of the faction to be removed
     * @return true if the faction is removed, false otherwise
     */
    public boolean tryToRemoveFaction(String name) {
        for (Faction f : factions) {
            if (f.name.equals(name)) {
                factions.remove(f);
                return true;
            }
        }
        return false;
    }

    /**
     * Tries to remove the faction of the given codex key from the world codex.
     * Suceeds if a faction of the given key is found (and removed).
     *
     * @param key the codex key of the faction to be removed
     * @return true if the faction is removed, false otherwise
     */
    public boolean tryToRemoveFaction(long key) {
        for (Faction f : factions) {
            if (f.codexKey == key) {
                factions.remove(f);
                return true;
            }
        }
        return false;
    }

    /**
     * Tries to find a faction in the codex with the given name.
     *
     * @param name the name of the faction to find
     * @return the faction if found, otherwise null
     */
    public @Nullable Faction tryToFindFaction(String name) {
        for (Faction f : factions) {
            if (f.name.equals(name)) return f;
        }
        return null;
    }

    /**
     * Tries to find a faction in the codex with the given codex key.
     *
     * @param key the codex key of the faction to find
     * @return the faction if found, otherwise null
     */
    public @Nullable Faction tryToFindFaction(long key) {
        for (Faction f : factions) {
            if (f.codexKey == key) return f;
        }
        return null;
    }

    /**
     * Tries to find the faction a player is in and remove them.
     *
     * @param uuid the UUID of the player
     * @return true if the player is found and removed, false otherwise
     */
    public boolean tryToPickPlayer(UUID uuid) {
        for (Faction f : factions) {
            if (f.hasMember(uuid)) {
                f.tryRemoveMember(uuid);
                return true;
            }
        }
        return false;
    }

    /**
     * Tries to find the faction a player is in.
     *
     * @param uuid the UUID of the player
     * @return the faction if found, otherwise null
     */
    public @Nullable Faction tryToFindPlayer(UUID uuid) {
        for (Faction f : factions) {
            if (f.hasMember(uuid)) return f;
        }
        return null;
    }

    /**
     * Tries to find the faction member data of the given player.
     *
     * @param uuid the UUID of the player
     * @return the member data if found, otherwise null
     */
    public @Nullable FactionMember tryToGetMember(UUID uuid) {
        for (Faction f : factions) {
            FactionMember m = f.getMember(uuid);
            if (m != null) return m;
        }
        return null;
    }

    /**
     * Tries to find the faction a villager peasant is in.
     *
     * @param peasant the UUID of the villager peasant
     * @return the faction if found, otherwise null
     */
    public @Nullable Faction tryToFindPeasant(UUID peasant) {
        for (Faction f : factions)
            if (f.hasPeasant(peasant)) return f;
        return null;
    }

    /**
     * Tries to find the faction a villager infantry is in.
     *
     * @param infantry the UUID of the villager infantry
     * @return the faction if found, otherwise null
     */
    public @Nullable Faction tryToFindInfantry(UUID infantry) {
        for (Faction f : factions)
            if (f.hasInfantry(infantry)) return f;
        return null;
    }

    /**
     * Tries to find the faction which has a cornerstone
     * at the given chunk position.
     *
     * @param pos the chunk pos to search
     * @return the faction if found, otherwise null
     */
    public @Nullable Faction tryToFindCornerstone(ChunkPos pos) {
        for (Faction f : factions) {
            if (f.hasCornerstoneAt(pos)) return f;
        }
        return null;
    }

    /**
     * Tries to find the cornerstone at the given chunk position.
     *
     * @param pos the chunk pos to search
     * @return the cornerstone if found, otherwise null
     */
    public @Nullable FactionCornerstone tryToGetCornerstone(ChunkPos pos) {
        for (Faction f : factions) {
            FactionCornerstone c = f.getCornerstoneAt(pos);
            if (c != null) return c;
        }
        return null;
    }

    public String[] getStringCollectionAs(UUID player) {
        String[] out = new String[factions.size()];
        int i = 0;
        for (Faction e : factions) {
            String code = e.passcode.isBlank() ? "  (no passcode)" : ("  passcode: " + e.passcode);
            String suffix = !e.hasMember(player) ? "" : code;
            out[i++] = e + suffix;
        }
        return out;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        NbtCompound facs = new NbtCompound();
        int facId = 0;
        for (Faction e : factions)
            facs.put("fac" + facId++, Faction.deconstruct(e));
        nbt.put("factions", facs);
        nbt.putLong("fku", factionKeyUpper);
        return nbt;
    }

    public static @NotNull FactionCodexState constructFrom(@NotNull NbtCompound nbt,
                                                           RegistryWrapper.WrapperLookup registryLookup) {
        FactionCodexState state = new FactionCodexState();
        NbtCompound facs = nbt.getCompound("factions");
        for (String k : facs.getKeys())
            state.factions.add(Faction.construct(facs.getCompound(k)));
        state.factionKeyUpper = nbt.getLong("fku");
        return state;
    }

    private static final Type<FactionCodexState> TYPE = new Type<>(
            FactionCodexState::new,
            FactionCodexState::constructFrom,
            null
    );

    public static @NotNull FactionCodexState getServerState(@NotNull MinecraftServer server) {
        FactionCodexState state = Objects.requireNonNull(server.getWorld(World.OVERWORLD))
                .getPersistentStateManager().getOrCreate(TYPE, Sovereign.ID);
        state.markDirty();
        return state;
    }

}
