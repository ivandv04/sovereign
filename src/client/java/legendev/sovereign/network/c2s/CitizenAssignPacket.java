package legendev.sovereign.network.c2s;

import legendev.sovereign.entity.VillagerInfantryEntity;
import legendev.sovereign.factiondata.*;
import legendev.sovereign.payload.CitizenAssignPayload;
import legendev.sovereign.persistent.FactionCodexState;
import legendev.sovereign.registry.types.SovereignItems;
import legendev.sovereign.registry.types.SovereignSounds;
import legendev.sovereign.util.ChatUtil;
import legendev.sovereign.util.FormatStrings;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class CitizenAssignPacket {

    private CitizenAssignPacket() {
    }

    public static void receive(@NotNull CitizenAssignPayload payload, ServerPlayNetworking.@NotNull Context context) {
        ServerPlayerEntity player = context.player();
        MinecraftServer server = Objects.requireNonNull(player.getServer());
        boolean forPeasant = payload.type() == CitizenType.PEASANT;
        boolean forInfantry = payload.type() == CitizenType.INFANTRY;
        assert forPeasant || forInfantry;
        server.execute(() -> {
            // get the codex and check for villager
            FactionCodexState state = FactionCodexState.getServerState(server);
            Faction faction = state.tryToFindFaction(payload.faction());
            assert faction != null;
            Entity fromUuid = ((ServerWorld) player.getWorld()).getEntity(payload.citizen());
            if (fromUuid == null) {
                sendError(player, "This villager has disappeared");
                restorePaper(player, payload.type());
                return;
            }
            // villager found - continue
            assert fromUuid instanceof VillagerEntity;
            VillagerEntity villager = (VillagerEntity) fromUuid;
            FactionCornerstone cornerstone = faction.getCornerstoneAt(
                    player.getWorld().getChunk(villager.getBlockPos()).getPos());
            // check for nearby cornerstone
            if (cornerstone == null) {
                ChatUtil.sendErrorOverlay(player, "Not near any faction cornerstone");
                restorePaper(player, payload.type());
                return;
            }
            // check cornerstone type (for infantry training)
            if (forInfantry && !cornerstone.getType().permitsConscripting()) {
                ChatUtil.sendErrorOverlay(player, "Cornerstone unable to train units");
                restorePaper(player, payload.type());
                return;
            }
            // ensure vacancy remains and assign (for peasants)
            if (forPeasant) {
                boolean attemptAdd = cornerstone.tryAdd(payload.citizen(), payload.name());
                if (!attemptAdd) {
                    sendError(player, "Cornerstone has no vacancy");
                    restorePaper(player, payload.type());
                    return;
                }
                villager.setCustomName(Text.literal(payload.name()));
                sendChatOverlay(player, payload.name() + " has entered " + faction.name);
            }
            // ensure conscript rate satisfied (for infantry)
            if (forInfantry) {
                if (!faction.canTrainNewUnits()) {
                    sendError(player, faction.ideology == FactionIdeology.ANARCHISM
                            ? (faction.name + " is " + faction.ideology.adj + " and cannot train infantry")
                            : (faction.name + " cannot support more infantry"));
                    restorePaper(player, payload.type());
                    return;
                }
                VillagerInfantryEntity unit = VillagerInfantryEntity.createFromIdeology(
                        faction.ideology, player.getWorld(),
                        VillagerInfantryEntity.DEFAULT_HP + faction.ideology.col
                                + payload.name() + FormatStrings.WHITE);
                unit.setPosition(villager.getPos());
                unit.setVelocity(villager.getVelocity());
                unit.setYaw(villager.getYaw());
                villager.remove(Entity.RemovalReason.DISCARDED);
                player.getWorld().spawnEntity(unit);
                player.getWorld().playSound(unit, unit.getBlockPos(),
                        SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE.value(), SoundCategory.AMBIENT,
                        2.5f, 0.75f);
                // conscript unit and confirm
                FactionMember getMember = faction.getMember(player.getUuid());
                assert getMember != null;
                getMember.addUnit(unit.getUuid(), payload.name());
                sendChatOverlay(player, payload.name() + " has been conscripted into " + faction.name);
            }
            player.playSoundToPlayer(
                    SovereignSounds.CITIZEN_ASSIGN, SoundCategory.AMBIENT,
                    0.75f, 1.25f);
        });
    }

    private static void restorePaper(@NotNull ServerPlayerEntity player, @NotNull CitizenType type) {
        assert type.isAssignable();
        Item item = null;
        if (type == CitizenType.PEASANT) item = SovereignItems.RESIDENT_PAPERS_ITEM;
        else if (type == CitizenType.INFANTRY) item = SovereignItems.CONSCRIPT_PAPERS_ITEM;
        assert item != null;
        if (!player.isCreative()) player.giveItemStack(item.getDefaultStack());
    }

    private static void sendChatOverlay(ServerPlayerEntity player, String msg) {
        ChatUtil.sendOverlay(player, msg);
    }

    private static void sendError(ServerPlayerEntity player, String err) {
        ChatUtil.sendErrorOverlay(player, err);
    }

}