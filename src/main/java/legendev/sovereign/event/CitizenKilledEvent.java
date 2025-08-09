package legendev.sovereign.event;

import legendev.sovereign.callback.LivingEntityDeathCallback;
import legendev.sovereign.entity.VillagerInfantryEntity;
import legendev.sovereign.factiondata.Faction;
import legendev.sovereign.persistent.FactionCodexState;
import legendev.sovereign.util.ChatUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class CitizenKilledEvent implements LivingEntityDeathCallback {

    @Override
    public ActionResult onEntityDeath(LivingEntity entity, Entity attacker, @NotNull World world) {
        if (!world.isClient && world.getServer() != null
                && (entity instanceof VillagerEntity || entity instanceof VillagerInfantryEntity)) {
            FactionCodexState state = FactionCodexState.getServerState(world.getServer());
            Faction f = state.tryToFindPeasant(entity.getUuid());
            if (f != null) f.tryRemovePeasant(entity.getUuid());
            else f = state.tryToFindInfantry(entity.getUuid());
            if (f != null) f.tryRemoveInfantry(entity.getUuid());
            else return ActionResult.PASS;
            assert entity.getDisplayName() != null;
            assert attacker == null || attacker.getDisplayName() != null;
            String name = entity instanceof VillagerInfantryEntity
                    ? ((VillagerInfantryEntity) entity).getNameWithoutPrefix()
                    : entity.getDisplayName().getString();
            ChatUtil.sendFactionMessage((ServerWorld) world, f, name + " was killed"
                    + (attacker != null ? " by " + attacker.getDisplayName().getString() : ""));
        }
        return ActionResult.PASS;
    }

    public static void register() {
        EVENT.register(new CitizenKilledEvent());
    }

}
