package legendev.sovereign.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

public interface LivingEntityDeathCallback {

    Event<LivingEntityDeathCallback> EVENT = EventFactory.createArrayBacked(
            LivingEntityDeathCallback.class,
            (listeners) -> (entity, attacker, world) -> {
                for (LivingEntityDeathCallback listener : listeners) {
                    ActionResult result = listener.onEntityDeath(entity, attacker, world);
                    if (result != ActionResult.PASS) return result;
                }
                return ActionResult.PASS;
            });

    ActionResult onEntityDeath(LivingEntity entity, Entity attacker, World world);

}
