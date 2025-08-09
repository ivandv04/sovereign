package legendev.sovereign.network.c2s;

import legendev.sovereign.entity.VillagerInfantryEntity;
import legendev.sovereign.factiondata.*;
import legendev.sovereign.payload.CoupPayload;
import legendev.sovereign.persistent.FactionCodexState;
import legendev.sovereign.util.ChatUtil;
import legendev.sovereign.util.FormatStrings;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.ChunkPos;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public final class CoupPacket {

    private CoupPacket() {
    }

    public static void receive(@NotNull CoupPayload payload, ServerPlayNetworking.@NotNull Context context) {
        ServerPlayerEntity player = context.player();
        MinecraftServer server = Objects.requireNonNull(player.getServer());
        server.execute(() -> {
            // get the codex and find player/cornerstone
            FactionCodexState state = FactionCodexState.getServerState(server);
            Faction faction = state.tryToFindPlayer(player.getUuid());
            ChunkPos pos = player.getChunkPos();
            FactionCornerstone cornerstone = state.tryToGetCornerstone(pos);
            assert faction != null;
            if (cornerstone == null || !faction.hasCornerstoneAt(cornerstone.getKey())) {
                sendError(player, "Coup must be launched within " + faction.name);
                return;
            }
            if (faction.inDanger()) {
                sendError(player, "Expel active threats before perform coup");
                return;
            }
            if (!cornerstone.getType().canStartCoup()) {
                sendError(player, "Coup cannot be launched from here");
                return;
            }
            // create faction and transfer player/cornerstone
            Faction ideoF = Faction.createNewDefault(state, payload.newName(), payload.newCode(), player.getUuid());
            FactionMember getMem = state.tryToGetMember(player.getUuid());
            assert getMem != null;
            boolean tryAdd = state.tryToAddFaction(ideoF, player.getUuid());
            if (!tryAdd) {
                sendError(player, "That name is taken by another faction");
                return;
            }
            boolean trans = faction.tryTransferCornerstone(ideoF, pos);
            assert trans;
            FactionIdeology i = payload.type();
            ideoF.ideology = i;
            ideoF.copyStatsFrom(faction);
            // transfer spawned infantry
            FactionMember ideoMem = state.tryToGetMember(player.getUuid()); // should be new and empty
            assert ideoMem != null;
            HashMap<UUID, String> transferredIds = new HashMap<>();
            for (VillagerInfantryEntity v : ((ServerWorld) player.getWorld()).getEntitiesByType(
                    TypeFilter.instanceOf(VillagerInfantryEntity.class), EntityPredicates.VALID_ENTITY)) {
                // check ownership and transfer
                if (getMem.hasUnit(v.getUuid())) {
                    String name = v.getNameWithoutPrefix();
                    VillagerInfantryEntity newV = VillagerInfantryEntity.createFromIdeology(i, player.getWorld(),
                            VillagerInfantryEntity.DEFAULT_HP + i.col + name + FormatStrings.WHITE);
                    newV.setPosition(v.getPos());
                    newV.setVelocity(v.getVelocity());
                    newV.setYaw(v.getYaw());
                    v.remove(Entity.RemovalReason.DISCARDED);
                    player.getWorld().spawnEntity(newV);
                    transferredIds.put(newV.getUuid(), name);
                    ((ServerWorld) player.getWorld()).spawnParticles(ParticleTypes.HAPPY_VILLAGER,
                            newV.getX(), newV.getY() + 1, newV.getZ(),
                            25, 0.5d, 1.0d, 0.5d, 0);
                }
            }
            // remove un-transferred units from player control
            // they will remain leaderless in the wild
            ideoMem.clearUnits();
            for (UUID u : transferredIds.keySet()) {
                ideoMem.addUnit(u, transferredIds.get(u));
            }
            // send broadcast
            player.getWorld().playSound(null, player.getBlockPos(),
                    SoundEvents.ENTITY_WITHER_HURT, SoundCategory.AMBIENT,
                    3.0f, 0.5f);
            assert player.getDisplayName() != null;
            announce(server, player.getDisplayName().getString() + " is leading " + ideoF.baseNameColoured()
                    + " in revolt against " + faction.baseNameColoured());
        });
    }

    private static void announce(MinecraftServer server, String msg) {
        ChatUtil.codexBroadcast(server, msg);
    }

    private static void sendError(ServerPlayerEntity player, String err) {
        ChatUtil.sendErrorOverlay(player, err);
    }

}