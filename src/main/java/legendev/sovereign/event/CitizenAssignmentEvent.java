package legendev.sovereign.event;

import legendev.sovereign.factiondata.CitizenType;
import legendev.sovereign.factiondata.Faction;
import legendev.sovereign.payload.CitizenPapersOpenPayload;
import legendev.sovereign.persistent.FactionCodexState;
import legendev.sovereign.registry.types.SovereignItems;
import legendev.sovereign.registry.types.SovereignSounds;
import legendev.sovereign.util.ChatUtil;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CitizenAssignmentEvent implements UseEntityCallback {

    @Override
    public ActionResult interact(PlayerEntity player, @NotNull World world, Hand hand, Entity entity,
                                 @Nullable EntityHitResult hitResult) {
        if (!world.isClient && !player.isSpectator() && hand == Hand.MAIN_HAND
                && hitResult == null && entity instanceof VillagerEntity) {
            ItemStack stack = player.getStackInHand(hand);
            boolean rp = stack.isOf(SovereignItems.RESIDENT_PAPERS_ITEM);
            boolean cp = stack.isOf(SovereignItems.CONSCRIPT_PAPERS_ITEM);
            boolean pp = stack.isOf(SovereignItems.PAPERS_PLEASE_ITEM);
            if (rp || cp || pp) {
                // Check player faction - only faction members may use item
                assert world.getServer() != null;
                FactionCodexState state = FactionCodexState.getServerState(world.getServer());
                Faction faction = state.tryToFindPlayer(player.getUuid());
                if (faction == null) {
                    ChatUtil.sendErrorOverlay(player, "You must be in a faction to do this");
                } else if (rp) {
                    // ASSIGN PEASANT
                    tryOpenResidentPapers(state, (ServerPlayerEntity) player,
                            faction, (VillagerEntity) entity, stack);
                } else if (cp) {
                    // ASSIGN INFANTRY
                    tryOpenConscriptPapers(state, (ServerPlayerEntity) player,
                            faction, (VillagerEntity) entity, stack);
                } else { // (if pp)
                    // UNASSIGN PEASANT
                    tryUnassignPeasant((ServerPlayerEntity) player, faction, (VillagerEntity) entity, stack);
                }
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

    private static void tryOpenResidentPapers(@NotNull FactionCodexState codex,
                                              ServerPlayerEntity player, Faction playerFaction,
                                              @NotNull VillagerEntity peasantPick, ItemStack source) {
        Faction find = codex.tryToFindPeasant(peasantPick.getUuid());
        if (find != null) {
            ChatUtil.sendErrorOverlay(player, "They are already a peasant in " + find.name);
        } else {
            if (!player.isCreative()) source.decrement(1);
            ServerPlayNetworking.send(player,
                    new CitizenPapersOpenPayload(peasantPick.getUuid(),
                            CitizenType.PEASANT, playerFaction.codexKey));
        }
    }

    private static void tryOpenConscriptPapers(@NotNull FactionCodexState codex,
                                               ServerPlayerEntity player, Faction playerFaction,
                                               @NotNull VillagerEntity infantryPick, ItemStack source) {
        Faction find = codex.tryToFindInfantry(infantryPick.getUuid());
        if (find != null) {
            ChatUtil.sendErrorOverlay(player, "They are already conscripted in " + find.name);
        } else {
            if (!player.isCreative()) source.decrement(1);
            ServerPlayNetworking.send(player,
                    new CitizenPapersOpenPayload(infantryPick.getUuid(),
                            CitizenType.INFANTRY, playerFaction.codexKey));
        }
    }

    private static void tryUnassignPeasant(@NotNull ServerPlayerEntity player, @NotNull Faction playerFaction,
                                           @NotNull VillagerEntity villager, ItemStack source) {
        if (!playerFaction.tryRemovePeasant(villager.getUuid())) {
            ChatUtil.sendErrorOverlay(player, "They are not a peasant in your faction");
        } else {
            if (!player.isCreative()) source.decrement(1);
            player.giveItemStack(SovereignItems.RESIDENT_PAPERS_ITEM.getDefaultStack());
            assert villager.getCustomName() != null;
            ChatUtil.sendOverlay(player, "Expelled " + villager.getCustomName().getString()
                    + " from " + playerFaction.name);
            villager.setCustomName(null);
            player.playSoundToPlayer(
                    SovereignSounds.CITIZEN_ASSIGN, SoundCategory.AMBIENT,
                    0.75f, 1.25f);
        }
    }

    public static void register() {
        EVENT.register(new CitizenAssignmentEvent());
    }

}
