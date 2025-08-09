package legendev.sovereign.item;

import legendev.sovereign.factiondata.FactionCornerstone;
import legendev.sovereign.registry.types.SovereignBlocks;
import legendev.sovereign.factiondata.Faction;
import legendev.sovereign.factiondata.TerritoryType;
import legendev.sovereign.persistent.FactionCodexState;
import legendev.sovereign.util.ChatUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.ChunkPos;
import org.jetbrains.annotations.NotNull;

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
            } else if (corEmp.inDanger()) {
                ChatUtil.sendErrorOverlay(player, "Expel active threats before upgrading");
            } else if (!cor.getType().isDefaultType()) {
                ChatUtil.sendErrorOverlay(player, "There is already a "
                        + cor.getType().toString().toLowerCase() + " here");
            } else if (type == TerritoryType.CAPITAL && corEmp.hasCapital()) {
                ChatUtil.sendErrorOverlay(player, "Your faction already has a capital");
            } else if (!corEmp.fitsConnectReq(getPos)
                    && !corEmp.canPassConnected() && type.requireConnected()) {
                ChatUtil.sendErrorOverlay(player, "Invalid cornerstone adjacency");
            } else {
                if (!player.isCreative()) context.getStack().decrement(1);
                cor.setType(type);
                ChatUtil.sendOverlay(player, "Constructed a " + type.toString().toLowerCase()
                        + " cornerstone for " + playerFac.name);
            }
            return ActionResult.SUCCESS;
        }
        return super.useOnBlock(context);
    }

}