package legendev.sovereign.block;

import legendev.sovereign.factiondata.FactionCornerstone;
import legendev.sovereign.factiondata.TerritoryType;
import legendev.sovereign.persistent.FactionCodexState;
import legendev.sovereign.registry.types.SovereignBlocks;
import legendev.sovereign.factiondata.Faction;
import legendev.sovereign.util.ChatUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CornerstoneBlock extends Block {

    public CornerstoneBlock(@NotNull Settings settings) {
        super(settings.strength(3f, 100f));
    }

    @Override
    protected ActionResult onUse(BlockState state, @NotNull World world, BlockPos pos,
                                 PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient && world.getServer() != null && player.getStackInHand(Hand.MAIN_HAND).isEmpty()) {
            // print territory data
            FactionCodexState codex = FactionCodexState.getServerState(world.getServer());
            ChunkPos cPos = world.getChunk(pos).getPos();
            Faction chunkFac = codex.tryToFindCornerstone(cPos);
            assert chunkFac != null;
            FactionCornerstone cornerstone = chunkFac.getCornerstoneAt(cPos);
            String type = cornerstone.getType().isDefaultType() ? "???" : cornerstone.getType().toString();
            ChatUtil.sendOverlay(player, type + " (" + chunkFac.name + ")");
            player.swingHand(Hand.MAIN_HAND);
            // show chunk border
            spawnParticleBorder((ServerWorld) world, pos);
        }
        return super.onUse(state, world, pos, player, hit);
    }

    /*
     * (This needs to be executed in a PlayerBlockBreakEvents.Before instance)
     *
     *  A player may break a cornerstone in a chunk under these conditions:
     * - The player is a member of the faction that occupies the chunk
     * - The chunk is not currently housing any peasants
     * - The faction that occupies the chunk is not currently in a raid
     * - The faction that occupies the chunk is not currently in an uprising
     *
     * If the above conditions are met, the following will occur:
     * - The chunk will become unoccupied by its respective faction
     * - The chunk will lose all territory-related abilities and effects
     */
    public static boolean validateBreak(@NotNull World world, PlayerEntity player,
                                        BlockPos pos, BlockState state) {
        if (!world.isClient && player != null && player.getServer() != null) {
            FactionCodexState codex = FactionCodexState.getServerState(player.getServer());
            ChunkPos cPos = world.getChunk(pos).getPos();
            Faction playerFac = codex.tryToFindPlayer(player.getUuid());
            Faction chunkFac = codex.tryToFindCornerstone(cPos);
            assert chunkFac != null;
            FactionCornerstone cornerstone = chunkFac.getCornerstoneAt(cPos);
            assert cornerstone != null;
            if (!chunkFac.is(playerFac)) {
                ChatUtil.sendErrorOverlay(player, "Your faction does not control this chunk");
            } else if (cornerstone.hasUnits()) {
                ChatUtil.sendErrorOverlay(player, "Peasants still reside here");
            } else if (chunkFac.inDanger()) {
                ChatUtil.sendErrorOverlay(player, "Expel active threats before removing");
            } else {
                // success!
                chunkFac.tryRemoveCornerstoneAt(cPos);
                if (!player.isCreative()) Block.dropStack(world, pos,
                        SovereignBlocks.CORNERSTONE_BLOCK.asItem().getDefaultStack());
                spawnParticleBorder((ServerWorld) world, pos);
                assert playerFac != null;
                ChatUtil.sendOverlay(player, "Dismantled a cornerstone in " + playerFac.name);
                return true;
            }
        }
        // if we get here it's an error...
        return false;
    }

    /*
     * (Works without an event call, so we can just override)
     *
     * A player may place a cornerstone in a chunk under these conditions:
     * - The player is a member of a faction
     * - The chunk is not currently occupied by any faction
     * - The faction of the player is not currently in a raid
     * - The faction of the player is not currently in an uprising
     *
     * If the above conditions are met, the following will occur:
     * - The chunk will become occupied by the faction of the player
     * - The chunk will inherit the default territory type
     */
    @Override
    public void onPlaced(@NotNull World world, BlockPos pos, BlockState state,
                         @Nullable LivingEntity placer, ItemStack itemStack) {
        if (!world.isClient && placer instanceof PlayerEntity player && placer.getServer() != null) {
            FactionCodexState codex = FactionCodexState.getServerState(player.getServer());
            ChunkPos cPos = world.getChunk(pos).getPos();
            Faction playerFac = codex.tryToFindPlayer(player.getUuid());
            Faction chunkFac = codex.tryToFindCornerstone(cPos);
            if (playerFac == null) {
                ChatUtil.sendErrorOverlay(player, "You are not in a faction");
            } else if (chunkFac != null) {
                ChatUtil.sendErrorOverlay(player, chunkFac.name + " already occupies this chunk");
            } else if (playerFac.inDanger()) {
                ChatUtil.sendErrorOverlay(player, "Expel active threats before expanding");
            } else {
                // success!
                playerFac.tryAddCornerstoneAt(cPos, TerritoryType.defaultType());
                spawnParticleBorder((ServerWorld) world, pos);
                ChatUtil.sendOverlay(player, "Claimed a chunk for " + playerFac.name);
                return;
            }
            // Error result - break block
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
            if (!player.isCreative()) Block.dropStack(world, pos,
                    SovereignBlocks.CORNERSTONE_BLOCK.asItem().getDefaultStack());
        }
    }

    private static void spawnParticleBorder(@NotNull ServerWorld world, BlockPos origin) {
        ChunkPos cPos = world.getChunk(origin).getPos();
        int xL = cPos.getStartX(), xH = cPos.getEndX();
        int zL = cPos.getStartZ(), zH = cPos.getEndZ();
        for (int x = xL; x <= xH; x++)
            for (int z = zL; z <= zH; z += (x == xL || x == xH ? 1 : zH - zL)) {
                // summon particles on chunk edge
                world.spawnParticles(
                        ParticleTypes.END_ROD,
                        x + 0.5d, origin.getY() + 0.5d, z + 0.5d,
                        1, 0d, 0d, 0d, 0d);
            }
    }

}