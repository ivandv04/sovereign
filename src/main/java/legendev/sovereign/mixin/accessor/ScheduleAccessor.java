package legendev.sovereign.mixin.accessor;

import net.minecraft.entity.ai.brain.Schedule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Schedule.class)
public interface ScheduleAccessor {

    @Mutable
    @Accessor("VILLAGER_DEFAULT")
    static void setVillagerDefault(Schedule schedule) {
        throw new AssertionError();
    }

}
