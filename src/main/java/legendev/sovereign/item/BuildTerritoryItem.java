package legendev.sovereign.item;

import legendev.sovereign.block.CornerstoneBlock;
import legendev.sovereign.factiondata.FactionCornerstone;
import legendev.sovereign.registry.types.SovereignBlocks;
import legendev.sovereign.factiondata.Faction;
import legendev.sovereign.factiondata.TerritoryType;
import legendev.sovereign.persistent.FactionCodexState;
import legendev.sovereign.registry.types.SovereignSounds;
import legendev.sovereign.util.ChatUtil;
import legendev.sovereign.util.FormatStrings;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BuildTerritoryItem extends Item {

    private final TerritoryType type;

    public BuildTerritoryItem(TerritoryType type) {
        super(new Settings().maxCount(16));
        this.type = type;
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return type == TerritoryType.CAPITAL;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (this.type.requireConnected()) {
            tooltip.add(Text.literal(FormatStrings.RED + "Non-removable"));
        }
    }

    /*
     * A player may upgrade a cornerstone in a chunk under these conditions:
     * - The player and cornerstone are in the same faction
     * - The chunk has not already been upgraded
     * - The faction of the player is not currently in a raid
     * - The faction of the player is not currently in an uprising
     * - If the upgrade is to a capital, there are no other capitals
     * - If the upgrade requires adjacency, its placement is valid
     *
     * If the above conditions are met, the following will occur:
     * - The chunk will inherit the features of the desired territory type
     */
    @Override
    public ActionResult useOnBlock(@NotNull ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        if (!context.getWorld().isClient && player != null && player.getServer() != null
                && context.getHand() == Hand.MAIN_HAND && !player.isSneaking()) {
            BlockState state = context.getWorld().getBlockState(context.getBlockPos());
            if (!state.isOf(SovereignBlocks.CORNERSTONE_BLOCK)) {
                return ActionResult.PASS;
            }
            FactionCodexState codex = FactionCodexState.getServerState(player.getServer());
            ChunkPos getPos = context.getWorld().getChunk(context.getBlockPos()).getPos();
            Faction playerFac = codex.tryToFindPlayer(player.getUuid());
            Faction corEmp = codex.tryToFindCornerstone(getPos);
            assert corEmp != null;
            FactionCornerstone cor = corEmp.getCornerstoneAt(getPos);
            if (!corEmp.is(playerFac)) {
                ChatUtil.sendErrorOverlay(player,
                        "Cornerstone belongs to " + corEmp.name);
                upgradeFailure(context);
            } else if (corEmp.inDanger()) {
                ChatUtil.sendErrorOverlay(player, "Expel active threats before upgrading");
                upgradeFailure(context);
            } else if (!cor.getType().isDefaultType()) {
                ChatUtil.sendErrorOverlay(player, "There is already a "
                        + cor.getType().toString().toLowerCase() + " here");
                upgradeFailure(context);
            } else if (type == TerritoryType.CAPITAL && corEmp.hasCapital()) {
                ChatUtil.sendErrorOverlay(player, "Your faction already has a capital");
                upgradeFailure(context);
            } else if (!corEmp.fitsConnectReq(getPos)
                    && !corEmp.canPassConnected() && type.requireConnected()) {
                ChatUtil.sendErrorOverlay(player, "Invalid cornerstone adjacency");
                upgradeFailure(context);
            } else {
                assert playerFac != null;
                if (!player.isCreative()) context.getStack().decrement(1);
                cor.setType(type);
                context.getWorld().setBlockState(context.getBlockPos(), state.with(CornerstoneBlock.TYPE, type.id));
                ChatUtil.sendOverlay(player, "Constructed a " + type.toString().toLowerCase()
                        + " cornerstone for " + playerFac.name);
                CornerstoneBlock.spawnParticleBorder((ServerWorld) context.getWorld(),
                        context.getBlockPos(), ParticleTypes.HEART);
                player.playSoundToPlayer(
                        SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.AMBIENT,
                        0.75f, 1.25f);
            }
            return ActionResult.SUCCESS;
        }
        return super.useOnBlock(context);
    }

    private static void upgradeFailure(@NotNull ItemUsageContext context) {
        CornerstoneBlock.spawnParticleBorder((ServerWorld) context.getWorld(),
                context.getBlockPos(), ParticleTypes.RAID_OMEN);
    }
}