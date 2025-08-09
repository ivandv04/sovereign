package legendev.sovereign.event;

import legendev.sovereign.block.CornerstoneBlock;
import legendev.sovereign.registry.types.SovereignBlocks;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CornerstoneRemoveEvent implements PlayerBlockBreakEvents.Before {

    @Override
    public boolean beforeBlockBreak(@NotNull World world, PlayerEntity player, BlockPos pos, BlockState state,
                                    @Nullable BlockEntity blockEntity) {
        if (!world.isClient && player != null
                && world.getBlockState(pos).isOf(SovereignBlocks.CORNERSTONE_BLOCK)) {
            return CornerstoneBlock.validateBreak(world, player, pos, state);
        }
        return true;
    }

    public static void register() {
        PlayerBlockBreakEvents.BEFORE.register(new CornerstoneRemoveEvent());
    }
}
