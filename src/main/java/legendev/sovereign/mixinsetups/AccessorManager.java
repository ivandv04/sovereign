package legendev.sovereign.mixinsetups;

import legendev.sovereign.mixin.accessor.ScheduleAccessor;
import legendev.sovereign.registry.BrainObjectRegistry;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Schedule;
import net.minecraft.entity.ai.brain.ScheduleBuilder;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public final class AccessorManager {

    private AccessorManager() {
    }

    /*
     * Just a note based on experience:
     * (MANY hours were wasted because of ignorance towards this)
     *
     * Even with @Mutable, mixin accessors are NOT able to get()
     * from PRIVATE FINAL fields. They can get/set from PUBLIC
     * FINAL, but for the former, only set() works. Thus, you may
     * as well just @Shadow the field directly.
     */

    public static void run() {
        addSchedules();
    }

    private static void addSchedules() {
        ScheduleAccessor.setVillagerDefault(
                new ScheduleBuilder(Registry.register(Registries.SCHEDULE, "villager_default", new Schedule()))
                        .withActivity(10, BrainObjectRegistry.PRAY)
                        .withActivity(2000, Activity.WORK)
                        .withActivity(9000, Activity.MEET)
                        .withActivity(11000, Activity.IDLE)
                        .withActivity(12000, Activity.REST)
                        .build());
    }
}
