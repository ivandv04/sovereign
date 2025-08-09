package legendev.sovereign.event;

import legendev.sovereign.persistent.FactionCodexState;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RenameEntityEvent implements UseEntityCallback {

    @Override
    public ActionResult interact(PlayerEntity player, @NotNull World world, Hand hand,
                                 Entity entity, @Nullable EntityHitResult hitResult) {
        if (!world.isClient && !player.isSpectator() && hitResult == null
                && player.getStackInHand(hand).isOf(Items.NAME_TAG)) {
            // Do not permit renaming for peasants and infantry
            assert world.getServer() != null;
            FactionCodexState state = FactionCodexState.getServerState(world.getServer());
            if (state.tryToFindPeasant(entity.getUuid()) != null
                    || state.tryToFindInfantry(entity.getUuid()) != null)
                return ActionResult.FAIL;
        }
        return ActionResult.PASS;
    }

    public static void register() {
        EVENT.register(new RenameEntityEvent());
    }
}
