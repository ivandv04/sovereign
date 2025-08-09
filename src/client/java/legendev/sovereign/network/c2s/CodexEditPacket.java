package legendev.sovereign.network.c2s;

import legendev.sovereign.factiondata.Faction;
import legendev.sovereign.payload.CodexEditPayload;
import legendev.sovereign.persistent.FactionCodexState;
import legendev.sovereign.util.ChatUtil;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public final class CodexEditPacket {

    private CodexEditPacket() {
    }

    public static void receive(CodexEditPayload payload, ServerPlayNetworking.@NotNull Context context) {
        ServerPlayerEntity player = context.player();
        MinecraftServer server = Objects.requireNonNull(player.getServer());
        server.execute(() -> {
            FactionCodexState state = FactionCodexState.getServerState(server);
            switch (payload.task()) {
                case CREATE -> createFaction(player, state, payload.name(), payload.passcode());
                case JOIN -> joinFaction(player, state, payload.name(), payload.passcode());
                case EDIT -> editFaction(player, state, payload.name(), payload.passcode());
                case DESTROY -> destroyFaction(player, state);
                case LEAVE -> leaveFaction(player, state);
                case LIST -> listFactions(player, state);
            }
        });
    }

    private static void createFaction(@NotNull ServerPlayerEntity player, @NotNull FactionCodexState state,
                                      String name, String passcode) {
        String pName = Objects.requireNonNull(player.getDisplayName()).getString();
        Faction f = Faction.createNewDefault(state, name, passcode, player.getUuid());
        boolean pass = state.tryToAddFaction(f, player.getUuid());
        if (!pass)
            sendError(player, "That faction already exists");
        else {
            sendBroadcast(player, pName + " formed the faction of " + f.baseNameColoured());
        }
    }

    private static void joinFaction(@NotNull ServerPlayerEntity player, @NotNull FactionCodexState state,
                                    String name, String passcode) {
        UUID uuid = player.getUuid();
        String pName = Objects.requireNonNull(player.getDisplayName()).getString();
        Faction current = state.tryToFindPlayer(uuid);
        Faction target = state.tryToFindFaction(name);
        if (target == null)
            sendError(player, "That faction does not exist");
        else if (current != null && current.equals(target))
            sendError(player, "You are already in that faction");
        else if (!target.hasOpenBorders() && !passcode.equals(target.passcode))
            sendError(player, "Incorrect passcode for " + target.name);
        else {
            boolean picked = state.tryToPickPlayer(uuid);
            String type = picked ? " defected to" : " joined";
            target.tryAddMember(uuid);
            sendBroadcast(player, pName + type + " the "
                    + target.fullNameColoured(true));
        }
    }

    private static void editFaction(@NotNull ServerPlayerEntity player, @NotNull FactionCodexState state,
                                    String newName, String newPasscode) {
        Faction current = state.tryToFindPlayer(player.getUuid());
        Faction find = state.tryToFindFaction(newName);
        String pName = Objects.requireNonNull(player.getDisplayName()).getString();
        if (current == null)
            sendError(player, "You are not in a faction");
        else if (find != null && !find.equals(current))
            sendError(player, "That faction name already is taken");
        else {
            String oldName = current.name, oldCode = current.passcode;
            boolean changedName = !oldName.equals(newName), changedCode = !oldCode.equals(newPasscode);
            if (!changedName && !changedCode)
                sendError(player, "Name and passcode are unchanged");
            else {
                if (changedName) {
                    current.name = newName;
                    sendBroadcast(player, pName + " has reformed " + oldName
                            + " into the " + current.fullNameColoured(true));
                }
                if (changedCode) {
                    current.passcode = newPasscode;
                    sendBroadcast(player, pName
                            + " changed the border code of " + current.baseNameColoured());
                }
            }
        }
    }

    private static void destroyFaction(@NotNull ServerPlayerEntity player, @NotNull FactionCodexState state) {
        Faction current = state.tryToFindPlayer(player.getUuid());
        String pName = Objects.requireNonNull(player.getDisplayName()).getString();
        if (current == null)
            sendError(player, "You are not in a faction");
        else if (!current.isRemovable())
            sendError(player, "Your faction is not empty");
        else {
            state.tryToRemoveFaction(current.name); // <- will succeed if ran
            sendBroadcast(player, pName + " has dismantled the "
                    + current.fullNameColoured(true));
        }
    }

    private static void leaveFaction(@NotNull ServerPlayerEntity player, @NotNull FactionCodexState state) {
        UUID uuid = player.getUuid();
        Faction current = state.tryToFindPlayer(uuid);
        String pName = Objects.requireNonNull(player.getDisplayName()).getString();
        if (current == null)
            sendError(player, "You are not in a faction");
        else {
            current.tryRemoveMember(uuid); // <- will succeed if ran
            sendBroadcast(player, pName + " has left the "
                    + current.fullNameColoured(true));
        }
    }

    private static void listFactions(ServerPlayerEntity player, @NotNull FactionCodexState state) {
        sendChat(player, "You begin reading the contents of the world codex");
        for (String s : state.getStringCollectionAs(player.getUuid()))
            sendChat(player, s);
        sendChat(player, "The world codex contains " + state.allFactions()
                + " factions in total, with a combined record of " + state.allCornerstones() + " cornerstones, "
                + state.allMembers() + " players, and " + state.allPeasants() + " peasants");
    }

    private static void sendBroadcast(@NotNull ServerPlayerEntity player, String msg) {
        assert player.getServer() != null;
        ChatUtil.codexBroadcast(player.getServer(), msg);
    }

    private static void sendChat(ServerPlayerEntity player, String msg) {
        ChatUtil.sendChat(player, msg);
    }

    private static void sendError(ServerPlayerEntity player, String err) {
        ChatUtil.sendErrorOverlay(player, err);
    }

}